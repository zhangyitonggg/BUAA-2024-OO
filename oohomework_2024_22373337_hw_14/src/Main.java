import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryCloseCmd;
import com.oocourse.library2.LibraryCommand;
import com.oocourse.library2.LibraryOpenCmd;
import com.oocourse.library2.LibraryReqCmd;
import com.oocourse.library2.LibraryRequest;
import java.time.LocalDate;

import static com.oocourse.library2.LibrarySystem.PRINTER;
import static com.oocourse.library2.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        while (true) {
            LibraryCommand command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            LocalDate date = command.getDate();
            if (command instanceof LibraryOpenCmd) {
                PRINTER.move(date, library.arrangeBook(date));
            } else if (command instanceof LibraryCloseCmd) {
                // do nothing; 下班了就不该干活，我说的！！！
                PRINTER.move(date);
            } else {
                LibraryReqCmd req = (LibraryReqCmd) command;
                LibraryRequest.Type type = req.getType();
                String userId = req.getStudentId();
                LibraryBookId bookId = req.getBookId();
                if (type.equals(LibraryRequest.Type.QUERIED)) {
                    PRINTER.info(req, library.queryBook(bookId));
                } else if (type.equals(LibraryRequest.Type.BORROWED)) {
                    Main.outAcOrRej(library.borrowBook(date, userId, bookId), req);
                } else if (type.equals(LibraryRequest.Type.RETURNED)) {
                    if (library.isOutDate(date, userId, bookId)) {
                        PRINTER.accept(req, "overdue");
                    } else {
                        PRINTER.accept(req, "not overdue");
                    }
                    library.returnBook(userId, bookId);
                } else if (type.equals(LibraryRequest.Type.ORDERED)) {
                    Main.outAcOrRej(library.orderBook(userId, bookId), req);
                } else if (type.equals(LibraryRequest.Type.PICKED)) {
                    Main.outAcOrRej(library.pickBook(date, userId, bookId), req);
                } else if (type.equals(LibraryRequest.Type.RENEWED)) {
                    Main.outAcOrRej(library.renewBook(date, userId, bookId), req);
                } else if (type.equals(LibraryRequest.Type.DONATED)) {
                    library.donateBook(bookId);
                    Main.outAcOrRej(true, req);
                }
            }
        }
        
    }
    
    private static void outAcOrRej(Boolean flag, LibraryReqCmd req) {
        if (flag) {
            PRINTER.accept(req);
        } else {
            PRINTER.reject(req);
        }
    }
}
