import com.oocourse.library1.LibraryBookId;
import java.util.HashMap;
import java.util.HashSet;

public class UserTable {
    private final HashSet<String> usersHasB;
    private final HashMap<String, HashSet<LibraryBookId>> user2AllC;
    
    public UserTable() {
        usersHasB = new HashSet<>();
        user2AllC = new HashMap<>();
    }
    
    private boolean hasB(String userId) {
        return usersHasB.contains(userId);
    }
    
    private boolean hasC(String userId, LibraryBookId bookId) {
        if (!user2AllC.containsKey(userId)) {
            return false;
        }
        return user2AllC.get(userId).contains(bookId);
    }
    
    public void addBook2User(String userId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            usersHasB.add(userId);
        }
        if (bookId.isTypeC()) {
            if (!user2AllC.containsKey(userId)) {
                HashSet<LibraryBookId> temp = new HashSet<>();
                temp.add(bookId);
                user2AllC.put(userId, temp);
            }
            else {
                user2AllC.get(userId).add(bookId);
            }
        }
    }
    
    public void removeBook(String userId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            usersHasB.remove(userId);
        } else if (bookId.isTypeC()) {
            user2AllC.get(userId).remove(bookId);
            if (user2AllC.get(userId).isEmpty()) {
                user2AllC.remove(userId);
            }
        }
    }
    
    public boolean checkCanGet(String userId, LibraryBookId bookId) {
        if (bookId.isTypeA()) {
            return false;
        }
        if (bookId.isTypeB()) {
            return !hasB(userId);
        }
        if (bookId.isTypeC()) {
            return !hasC(userId, bookId);
        }
        return false;
    }
}
