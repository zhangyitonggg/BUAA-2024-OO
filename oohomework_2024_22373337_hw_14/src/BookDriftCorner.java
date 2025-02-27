import com.oocourse.library2.LibraryBookId;

import java.util.HashMap;

public class BookDriftCorner {
    private final HashMap<LibraryBookId, Book> books;
    
    public BookDriftCorner() {
        this.books = new HashMap<>();
    }
    
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }
    
    // 输出非0即1
    public int queryBook(LibraryBookId bookId) {
        if (books.containsKey(bookId)) {
            return 1;
        }
        return 0;
    }
    
    public boolean hasBook(LibraryBookId bookId) {
        return books.containsKey(bookId);
    }
    
    public Book removeAndGetBook(LibraryBookId bookId) {
        Book book = books.get(bookId);
        books.remove(bookId);
        return book;
    }
}
