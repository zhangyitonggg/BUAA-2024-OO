import com.oocourse.library3.LibraryBookId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class AppointmentOffice {
    private final HashMap<String, ArrayList<Book>> books;
    
    public AppointmentOffice() {
        books = new HashMap<>();
    }
    
    // @request:由caller设置beginDate
    public void addBook(String userId, Book book) {
        if (books.containsKey(userId)) {
            books.get(userId).add(book);
        } else {
            ArrayList<Book> temp = new ArrayList<>();
            temp.add(book);
            books.put(userId, temp);
        }
    }
    
    // 返回null表示pick失败
    public Book tryPickBook(LocalDate date, String userId, LibraryBookId bookId) {
        if (!books.containsKey(userId)) {
            return null;
        }
        ArrayList<Book> booksOfOnePerson = books.get(userId);
        int temp = -1; // 待删除索引
        long maxDaysBetween = -1;
        for (int i = 0; i < booksOfOnePerson.size(); ++i) {
            Book book = booksOfOnePerson.get(i);
            // dueDate - 5是beginDate，所以这里实际上是date - beginDate
            long daysBet = Math.abs(ChronoUnit.DAYS.between(book.getDueDate().plusDays(-5), date));
            if (daysBet < 5 && book.getBookId().equals(bookId)) {
                if (maxDaysBetween < daysBet) {
                    temp = i;
                    maxDaysBetween = daysBet;
                }
            }
        }
        Book res = null;
        if (temp >= 0) {
            res = booksOfOnePerson.get(temp);
            booksOfOnePerson.remove(temp);
            if (booksOfOnePerson.isEmpty()) {
                books.remove(userId);
            }
        }
        return res;
    }
    
    public ArrayList<LibraryBookId> removeOutDateBook(LocalDate date,
                                                      HashMap<String, Integer> user2Score) {
        ArrayList<LibraryBookId> res = new ArrayList<>();
        HashSet<String> userId2Deleted = new HashSet<>();
        for (String userId : books.keySet()) {
            ArrayList<Book> booksOfOnePerson = books.get(userId);
            Iterator<Book> iterator = booksOfOnePerson.iterator();
            while (iterator.hasNext()) {
                Book book = iterator.next();
                // 这里用<=
                if (ChronoUnit.DAYS.between(date, book.getDueDate()) <= 0) {
                    res.add(book.getBookId());
                    iterator.remove();
                    user2Score.put(userId, user2Score.get(userId) - 3);
                }
            }
            if (booksOfOnePerson.isEmpty()) {
                userId2Deleted.add(userId);
            }
        }
        for (String userId : userId2Deleted) {
            books.remove(userId);
        }
        return res;
    }
    
    public boolean hasBookId(LibraryBookId bookId) {
        for (ArrayList<Book> list : books.values()) {
            for (Book book : list) {
                if (book.getBookId().equals(bookId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // bookId 一定是b或者c
    // 逾期的书必然被清理
    public boolean check(String userId, LibraryBookId bookId) {
        if (!books.containsKey(userId)) {
            return true;
        }
        ArrayList<Book> array = books.get(userId);
        if (bookId.isTypeB()) {
            for (Book book : array) {
                if (book.getBookId().isTypeB()) {
                    return false;
                }
            }
        } else if (bookId.isTypeC()) {
            for (Book book : array) {
                if (book.getBookId().equals(bookId)) {
                    return false;
                }
            }
        } else { // 没必要
            return false;
        }
        return true;
    }
}
