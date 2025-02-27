import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;
import com.oocourse.library2.annotation.Trigger;
import javafx.util.Pair;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private final Bookshelf bs;
    private final BorrowAndReturnOffice bro;
    private final AppointmentOffice ao;
    private final BookDriftCorner bdc;
    private final UserTable userTable;
    
    public Library() {
        this.bs = new Bookshelf();
        this.bro = new BorrowAndReturnOffice();
        this.ao = new AppointmentOffice();
        this.bdc = new BookDriftCorner();
        this.userTable = new UserTable();
    }
    
    // 只在开门后立刻开始整理
    public List<LibraryMoveInfo> arrangeBook(LocalDate date) {
        ArrayList<LibraryMoveInfo> moveList = new ArrayList<>();
        // 借还处上所有书 -> 书架/漂流角
        ArrayList<Book> broBooks = bro.removeAllBook();
        for (Book book : broBooks) {
            LibraryBookId bookId = book.getBookId();
            if (bookId.isFormal()) {
                // 借还处上正式书 -> 书架
                moveList.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                bs.addBook(bookId);
            } else {
                // 借还处上非正式书 -> 书架/漂流角
                if (book.getCount() >= 2) { // 实际上等于2即可
                    moveList.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                    bs.addBook(bookId.toFormal()); // 转化成正式的，爷入关了
                } else {
                    moveList.add(new LibraryMoveInfo(bookId, "bro", "bdc"));
                    bdc.addBook(book);
                }
            }
        }
        // 预约处上过期书 -> 书架
        ArrayList<LibraryBookId> aoBooks = ao.removeOutDateBook(date);
        for (LibraryBookId bookId : aoBooks) {
            moveList.add(new LibraryMoveInfo(bookId, "ao", "bs"));
            bs.addBook(bookId);
        }
        // 书架上预约的书 -> 预约处
        ArrayList<Pair<String, LibraryBookId>> bsBooks = bs.removeAppointedBook();
        for (Pair<String, LibraryBookId> pair : bsBooks) {
            moveList.add(new LibraryMoveInfo(pair.getValue(), "bs", "ao", pair.getKey()));
            Book book = new Book(pair.getValue());
            book.setDueDate(date.plusDays(5));
            ao.addBook(pair.getKey(), book);
        }
        return moveList;
    }
    
    public int queryBook(LibraryBookId bookId) {
        if (bookId.isFormal()) {
            return bs.queryBook(bookId);
        } else {
            return bdc.queryBook(bookId);
        }
    }
    
    public boolean borrowBook(LocalDate date, String userId, LibraryBookId bookId) {
        Book book;
        if (bookId.isFormal()) {
            if (!bs.hasBook(bookId)) {
                return false;
            } else if (bookId.isTypeA()) {
                return false;
            }
            book = bs.removeAndGetBook(bookId);
        } else {
            if (!bdc.hasBook(bookId)) {
                return false;
            } else if (bookId.isTypeAU()) {
                return false;
            }
            book = bdc.removeAndGetBook(bookId);
        }
        if (userTable.checkCanGet(userId, bookId)) {
            long daysMax = 0;
            if (bookId.isTypeB()) {
                daysMax = 30;
            } else if (bookId.isTypeC()) {
                daysMax = 60;
            } else if (bookId.isTypeBU()) {
                daysMax = 7;
            } else if (bookId.isTypeCU()) {
                daysMax = 14;
            }
            book.setDueDate(date.plusDays(daysMax));
            userTable.addBook2User(userId, book);
            return true;
        } else {
            bro.addBook(book);
            return false;
        }
    }
    
    public boolean isOutDate(LocalDate date, String userId, LibraryBookId bookId) {
        return userTable.isOutDate(date, userId, bookId);
    }
    
    @Trigger(from = "out", to = "in")
    public void returnBook(String userId, LibraryBookId bookId) {
        bro.addBook(userTable.removeAndGetBook(userId, bookId));
    }
    
    public boolean orderBook(String userId, LibraryBookId bookId) {
        // 首先排除非正式书籍的可能
        if (!bookId.isFormal()) {
            return false;
        }
        if (userTable.checkCanGet(userId, bookId)) {
            bs.addOrderMessage(userId, bookId);
            return true;
        }
        return false;
    }
    
    public boolean pickBook(LocalDate date, String userId, LibraryBookId bookId) {
        // 首先排除非正式书籍的可能，当然在这没必要
        if (!bookId.isFormal()) {
            return false;
        }
        if (userTable.checkCanGet(userId, bookId)) {
            Book book = ao.tryPickBook(date, userId, bookId);
            if (book != null) {
                long daysMax = 0;
                if (bookId.isTypeB()) {
                    daysMax = 30;
                } else if (bookId.isTypeC()) {
                    daysMax = 60;
                } else if (bookId.isTypeBU()) {
                    daysMax = 7;
                } else if (bookId.isTypeCU()) {
                    daysMax = 14;
                }
                book.setDueDate(date.plusDays(daysMax));
                userTable.addBook2User(userId, book);
                return true;
            }
        }
        return false;
    }
    
    public boolean renewBook(LocalDate date, String userId, LibraryBookId bookId) {
        // 首先排除非正式书籍的可能
        if (!bookId.isFormal()) {
            return false;
        }
        if (checkIsAppointed(bookId) && !bs.hasBook(bookId)) {
            return false;
        }
        return userTable.renewBook(date, userId, bookId);
    }
    
    // 检查此书是否在被人预约
    private boolean checkIsAppointed(LibraryBookId bookId) {
        if (bs.isInOrderQueue(bookId)) {
            return true;
        }
        if (ao.hasBookId(bookId)) {
            return true;
        }
        return false;
    }
    
    public void donateBook(LibraryBookId bookId) {
        bdc.addBook(new Book(bookId)); // new出来的Book的count必然为0
    }
}

