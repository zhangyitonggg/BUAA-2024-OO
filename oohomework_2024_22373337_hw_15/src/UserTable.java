import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.annotation.Trigger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class UserTable {
    private final HashMap<String, Book> user2AllB;
    private final HashMap<String, Book> user2AllBU;
    private final HashMap<String, HashMap<LibraryBookId, Book>> user2AllCX;
    
    public UserTable() {
        user2AllB = new HashMap<>();
        user2AllBU = new HashMap<>();
        user2AllCX = new HashMap<>();
    }
    
    private boolean hasCX(String userId, LibraryBookId bookId) {
        if (!user2AllCX.containsKey(userId)) {
            return false;
        }
        return user2AllCX.get(userId).containsKey(bookId);
    }
    
    private Book getBook(String userId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            return user2AllB.get(userId);
        } else if (bookId.isTypeBU()) {
            return user2AllBU.get(userId);
        } else {
            return user2AllCX.get(userId).get(bookId);
        }
    }
    
    private void deleteBook(String userId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            user2AllB.remove(userId);
        } else if (bookId.isTypeBU()) {
            user2AllBU.remove(userId);
        } else {
            user2AllCX.get(userId).remove(bookId);
            if (user2AllCX.get(userId).isEmpty()) {
                user2AllCX.remove(userId);
            }
        }
    }
    
    // @request: caller设置dueDate
    @Trigger(from = "in", to = "out")
    public void addBook2User(String userId, Book book) {
        LibraryBookId bookId = book.getBookId();
        if (bookId.isTypeB()) {
            user2AllB.put(userId, book);
        }
        if (bookId.isTypeBU()) {
            user2AllBU.put(userId, book);
        }
        if (bookId.isTypeC() || bookId.isTypeCU()) {
            if (!user2AllCX.containsKey(userId)) {
                HashMap<LibraryBookId, Book> temp = new HashMap<>();
                temp.put(bookId, book);
                user2AllCX.put(userId, temp);
            }
            else {
                user2AllCX.get(userId).put(bookId, book);
            }
        }
    }
    
    public Book removeAndGetBook(String userId, LibraryBookId bookId) {
        Book book = getBook(userId, bookId);
        deleteBook(userId, bookId);
        if (!bookId.isFormal()) {
            book.addCount();
        }
        return book;
    }
    
    public boolean checkCanGet(String userId, LibraryBookId bookId) {
        if (bookId.isTypeA() || bookId.isTypeAU()) {
            return false;
        }
        if (bookId.isTypeB()) {
            return !user2AllB.containsKey(userId);
        }
        if (bookId.isTypeBU()) {
            return !user2AllBU.containsKey(userId);
        }
        if (bookId.isTypeC() || bookId.isTypeCU()) {
            return !hasCX(userId, bookId);
        }
        return false;
    }
    
    // 仅适用于BX与CX图书
    public boolean isOutDate(LocalDate date, String userId, LibraryBookId bookId) {
        // 一定存在该书
        Book book = getBook(userId, bookId);
        return ChronoUnit.DAYS.between(date, book.getDueDate()) < 0;
    }
    
    public boolean renewBook(LocalDate date, String userId, LibraryBookId bookId) {
        Book book = getBook(userId, bookId);
        return book.tryRenew(date);
    }
    
    public void check(LocalDate date, HashMap<String, Integer> user2Score) {
        for (String userId : user2AllB.keySet()) {
            Book book = user2AllB.get(userId);
            if (book.getHasBeenSubScore()) {
                continue;
            }
            if (isOutDate(date, userId, book.getBookId())) {
                user2Score.put(userId, user2Score.get(userId) - 2);
                book.setHasBeenSubScore(true);
            }
        }
        for (String userId : user2AllBU.keySet()) {
            Book book = user2AllBU.get(userId);
            if (book.getHasBeenSubScore()) {
                continue;
            }
            if (isOutDate(date, userId, book.getBookId())) {
                user2Score.put(userId, user2Score.get(userId) - 2);
                book.setHasBeenSubScore(true);
            }
        }
        for (String userId : user2AllCX.keySet()) {
            HashMap<LibraryBookId, Book> map = user2AllCX.get(userId);
            for (LibraryBookId bookId : map.keySet()) {
                Book book = map.get(bookId);
                if (book.getHasBeenSubScore()) {
                    continue;
                }
                if (isOutDate(date, userId, bookId)) {
                    user2Score.put(userId, user2Score.get(userId) - 2);
                    book.setHasBeenSubScore(true);
                }
            }
        }
    }
}
