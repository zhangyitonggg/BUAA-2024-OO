import com.oocourse.library1.LibraryBookId;
import java.time.LocalDate;

public class AppointedBook {
    private final LibraryBookId bookId;
    private final LocalDate beginDate;
    private final String userId;
    
    public AppointedBook(LocalDate beginDate, LibraryBookId bookId, String userId) {
        this.beginDate = beginDate;
        this.bookId = bookId;
        this.userId = userId;
    }
    
    public LibraryBookId getBookId() {
        return bookId;
    }
    
    public LocalDate getBeginDate() {
        return beginDate;
    }
    
    public String getUserId() {
        return userId;
    }
}
