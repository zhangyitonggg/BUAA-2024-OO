import com.oocourse.library3.LibraryBookId;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static com.oocourse.library3.LibrarySystem.SCANNER;

public class Bookshelf {
    // 这里允许value为0
    private final HashMap<LibraryBookId, Integer> books;
    // 将其从ao移到bs，便于维护; <userId, bookId>
    private final ArrayList<Pair<String, LibraryBookId>> orderQueue;
    
    public Bookshelf() {
        Map<LibraryBookId, Integer> map = SCANNER.getInventory();
        this.books = new HashMap<>();
        for (LibraryBookId bookId: map.keySet()) {
            books.put(bookId, map.get(bookId));
        }
        this.orderQueue = new ArrayList<>();
    }
    
    public void addOrderMessage(String userId, LibraryBookId bookId) {
        Pair<String, LibraryBookId> pair = new Pair<>(userId, bookId);
        orderQueue.add(pair);
    }
    
    public int queryBook(LibraryBookId bookId) {
        // 应该一定存在该书
        if (!books.containsKey(bookId)) {
            return 0;
        }
        return books.get(bookId);
    }
    
    public boolean hasBook(LibraryBookId bookId) {
        // 没必要
        if (!books.containsKey(bookId)) {
            return false;
        }
        return books.get(bookId) > 0;
    }
    
    public Book removeAndGetBook(LibraryBookId bookId) {
        // 保证一定存在该书并且数目非0
        books.put(bookId, books.get(bookId) - 1);
        return new Book(bookId);
    }
    
    public void addBook(LibraryBookId bookId) {
        // 没必要有这一层检查
        if (books.containsKey(bookId)) {
            books.put(bookId, books.get(bookId) + 1);
        } else {
            books.put(bookId, 1);
        }
    }
    
    public ArrayList<Pair<String, LibraryBookId>> removeAppointedBook() {
        ArrayList<Pair<String, LibraryBookId>> res = new ArrayList<>();
        Iterator<Pair<String, LibraryBookId>> iterator = orderQueue.iterator();
        while (iterator.hasNext()) {
            Pair<String, LibraryBookId> pair = iterator.next();
            LibraryBookId bookId = pair.getValue();
            // 没必要有这一层判断
            if (books.containsKey(bookId)) {
                if (books.get(bookId) > 0) {
                    res.add(new Pair<>(pair.getKey(), bookId));
                    iterator.remove(); // 更新预约信息队列
                    books.put(bookId, books.get(bookId) - 1); // 更新书架
                }
            }
        }
        return res;
    }
    
    public boolean isInOrderQueue(LibraryBookId bookId) {
        for (Pair<String, LibraryBookId> pair : orderQueue) {
            if (bookId.equals(pair.getValue())) {
                return true;
            }
        }
        return false;
    }
    
    // bookId 一定是b或者c
    public boolean check(String userId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            for (Pair<String, LibraryBookId> pair : orderQueue) {
                if (pair.getKey().equals(userId)) {
                    if (pair.getValue().isTypeB()) {
                        return false;
                    }
                }
            }
        } else if (bookId.isTypeC()) {
            for (Pair<String, LibraryBookId> pair : orderQueue) {
                if (pair.getKey().equals(userId)) {
                    if (pair.getValue().equals(bookId)) {
                        return false;
                    }
                }
            }
        }
        else { // 没必要
            return false;
        }
        return true;
    }
}
