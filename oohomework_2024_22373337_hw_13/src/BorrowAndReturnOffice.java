import com.oocourse.library1.LibraryBookId;
import java.util.ArrayList;
import java.util.HashMap;

public class BorrowAndReturnOffice {
    private HashMap<LibraryBookId, Integer> books;
    
    public BorrowAndReturnOffice() {
        books = new HashMap<>();
    }
    
    public void addBook(LibraryBookId bookId) {
        if (!books.containsKey(bookId)) {
            books.put(bookId, 1);
        } else {
            books.put(bookId, books.get(bookId) + 1);
        }
    }
    
    public ArrayList<LibraryBookId> removeAllBook() {
        ArrayList<LibraryBookId> res = new ArrayList<>();
        for (LibraryBookId bookId : books.keySet()) {
            for (int i = 0; i < books.get(bookId); ++i) {
                res.add(bookId);
            }
        }
        books = new HashMap<>(); // 重置books
        return res;
    }
}
