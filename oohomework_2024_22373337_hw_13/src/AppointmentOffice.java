import com.oocourse.library1.LibraryBookId;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

public class AppointmentOffice {
    private final ArrayList<AppointedBook> books;
    
    public AppointmentOffice() {
        books = new ArrayList<>();
    }
    
    public void addBook(LocalDate beginDate, LibraryBookId bookId, String userId) {
        books.add(new AppointedBook(beginDate, bookId, userId));
    }
    
    // 返回null表示pick失败
    public LibraryBookId tryPickBook(LocalDate date, String userId, LibraryBookId bookId) {
        int temp = -1; // 待删除索引
        long maxDaysBetween = -1;
        for (int i = 0; i < books.size(); ++i) {
            AppointedBook book = books.get(i);
            long daysBet = Math.abs(ChronoUnit.DAYS.between(date, book.getBeginDate()));
            if (daysBet < 5 && book.getUserId().equals(userId) && book.getBookId().equals(bookId)) {
                if (maxDaysBetween < daysBet) {
                    temp = i;
                    maxDaysBetween = daysBet;
                }
            }
        }
        LibraryBookId res = null;
        if (temp >= 0) {
            res = books.get(temp).getBookId();
            books.remove(temp);
        }
        return res;
    }
    
    public ArrayList<LibraryBookId> removeOutDateBook(LocalDate date) {
        ArrayList<LibraryBookId> res = new ArrayList<>();
        Iterator<AppointedBook> iterator = books.iterator();
        while (iterator.hasNext()) {
            AppointedBook book = iterator.next();
            if (Math.abs(ChronoUnit.DAYS.between(date, book.getBeginDate())) >= 5) {
                res.add(book.getBookId());
                iterator.remove();
            }
        }
        return res;
    }
}
