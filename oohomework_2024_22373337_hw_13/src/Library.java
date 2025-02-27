import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;
import javafx.util.Pair;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private final Bookshelf bs;
    private final BorrowAndReturnOffice bro;
    private final AppointmentOffice ao;
    private final UserTable userTable;
    
    public Library() {
        this.bs = new Bookshelf();
        this.bro = new BorrowAndReturnOffice();
        this.ao = new AppointmentOffice();
        this.userTable = new UserTable();
    }
    
    // 只在开门后立刻开始整理
    public List<LibraryMoveInfo> arrangeBook(LocalDate date) {
        ArrayList<LibraryMoveInfo> moveList = new ArrayList<>();
        // 借还处上所有书 -> 书架
        ArrayList<LibraryBookId> broBooks = bro.removeAllBook();
        for (LibraryBookId bookId : broBooks) {
            moveList.add(new LibraryMoveInfo(bookId, "bro", "bs"));
            bs.addBook(bookId);
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
            ao.addBook(date, pair.getValue(), pair.getKey());
        }
        return moveList;
    }
    
    public int queryBook(LibraryBookId bookId) {
        return bs.queryBook(bookId);
    }
    
    public boolean borrowBook(String userId, LibraryBookId bookId) {
        if (!bs.hasBook(bookId)) {
            return false;
        } else if (bookId.isTypeA()) {
            return false;
        }
        bs.removeBook(bookId);
        if (userTable.checkCanGet(userId, bookId)) {
            userTable.addBook2User(userId, bookId);
            return true;
        } else {
            bro.addBook(bookId);
            return false;
        }
    }
    
    public void returnBook(String userId, LibraryBookId bookId) {
        userTable.removeBook(userId, bookId);
        bro.addBook(bookId);
    }
    
    public boolean orderBook(String userId, LibraryBookId bookId) {
        if (userTable.checkCanGet(userId, bookId)) {
            bs.addOrderMessage(userId, bookId);
            return true;
        }
        return false;
    }
    
    public boolean pickBook(LocalDate date, String userId, LibraryBookId bookId) {
        if (userTable.checkCanGet(userId, bookId)) {
            if (ao.tryPickBook(date, userId, bookId) != null) {
                userTable.addBook2User(userId, bookId);
                return true;
            }
        }
        return false;
    }
}
