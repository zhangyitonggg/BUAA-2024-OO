import com.oocourse.library2.LibraryBookId;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Book {
    private final LibraryBookId bookId;
    private LocalDate dueDate;
    private int count;
    private boolean isRenewed;
    
    public Book(LibraryBookId bookId) {
        this.bookId = bookId;
        this.dueDate = LocalDate.MIN;
        this.count = 0;
        this.isRenewed = false;
    }
    
    public LibraryBookId getBookId() {
        return bookId;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public void addCount() {
        this.count++;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public boolean tryRenew(LocalDate date) {
        // 没必要检查
        if (isRenewed) {
            return false;
        }
        long daysBet = ChronoUnit.DAYS.between(date, dueDate);
        if (daysBet < 0 || daysBet >= 5) { // 逾期办理 || 提前办理
            return false;
        }
        dueDate = dueDate.plusDays(30);
        isRenewed = true;
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof  Book)) {
            return false;
        } else {
            Book book = (Book) o;
            return this.bookId.equals(book.getBookId());
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.bookId);
    }
}
