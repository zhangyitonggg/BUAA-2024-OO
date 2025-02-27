import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryCommand;
import com.oocourse.library1.LibraryRequest;
import java.time.LocalDate;
import static com.oocourse.library1.LibrarySystem.PRINTER;
import static com.oocourse.library1.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        while (true) {
            LibraryCommand<?> oneInput = SCANNER.nextCommand();
            if (oneInput == null) {
                break;
            } else if (oneInput.getCmd().equals("OPEN")) {
                PRINTER.move(oneInput.getDate(), library.arrangeBook(oneInput.getDate()));
            } else if (oneInput.getCmd().equals("CLOSE")) {
                // do nothing; 下班了就不该干活，我说的！！！
                PRINTER.move(oneInput.getDate());
            } else {
                LocalDate date = oneInput.getDate();
                LibraryRequest request = (LibraryRequest) oneInput.getCmd();
                LibraryRequest.Type type = request.getType();
                String userId = request.getStudentId();
                LibraryBookId bookId = request.getBookId();
                if (type.equals(LibraryRequest.Type.QUERIED)) {
                    PRINTER.info(date, bookId, library.queryBook(bookId));
                } else if (type.equals(LibraryRequest.Type.BORROWED)) {
                    Main.outAcOrRej(library.borrowBook(userId, bookId), date, request);
                } else if (type.equals(LibraryRequest.Type.RETURNED)) {
                    library.returnBook(userId, bookId);
                    Main.outAcOrRej(true, date, request);
                } else if (type.equals(LibraryRequest.Type.ORDERED)) {
                    Main.outAcOrRej(library.orderBook(userId, bookId), date, request);
                } else if (type.equals(LibraryRequest.Type.PICKED)) {
                    Main.outAcOrRej(library.pickBook(date, userId, bookId), date, request);
                }
            }
        }
    }
    
    private static void outAcOrRej(Boolean flag, LocalDate date, LibraryRequest request) {
        if (flag) {
            PRINTER.accept(date, request);
        } else {
            PRINTER.reject(date, request);
        }
    }
}
