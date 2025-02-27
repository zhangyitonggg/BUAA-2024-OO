import java.util.ArrayList;
import java.util.HashMap;

public class BorrowAndReturnOffice {
    private HashMap<Book, Integer> books;
    
    public BorrowAndReturnOffice() {
        books = new HashMap<>();
    }
    
    public void addBook(Book book) {
        if (!books.containsKey(book)) {
            books.put(book, 1);
        } else {
            books.put(book, books.get(book) + 1);
        }
    }
    
    public ArrayList<Book> removeAllBook() {
        ArrayList<Book> res = new ArrayList<>();
        for (Book book : books.keySet()) {
            for (int i = 0; i < books.get(book); ++i) {
                res.add(book);
            }
        }
        books = new HashMap<>(); // 重置books
        return res;
    }
}
