import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryCloseCmd;
import com.oocourse.library3.LibraryCommand;
import com.oocourse.library3.LibraryOpenCmd;
import com.oocourse.library3.LibraryQcsCmd;
import com.oocourse.library3.LibraryReqCmd;
import com.oocourse.library3.LibraryRequest;

import java.time.LocalDate;
import java.util.HashMap;

import static com.oocourse.library3.LibrarySystem.PRINTER;
import static com.oocourse.library3.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> user2Score = new HashMap<>();
        Library library = new Library(user2Score);
        while (true) {
            LibraryCommand command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            LocalDate date = command.getDate();
            if (command instanceof LibraryOpenCmd) {
                PRINTER.move(date, library.arrangeBook(date));
            } else if (command instanceof LibraryCloseCmd) {
                // 没必要在闭馆后扣分
                // do nothing; 下班了就不该干活，我说的！！！
                PRINTER.move(date);
            } else if (command instanceof LibraryQcsCmd) {
                String userId = ((LibraryQcsCmd) command).getStudentId();
                if (!user2Score.containsKey(userId)) {
                    // 如果查询之前没出现过的人，返回10合理
                    user2Score.put(userId, 10);
                }
                PRINTER.info(date, userId, user2Score.get(userId));
            } else {
                LibraryReqCmd req = (LibraryReqCmd) command;
                LibraryRequest.Type type = req.getType();
                String userId = req.getStudentId();
                LibraryBookId bookId = req.getBookId();
                // 检查是否需要新增用户
                if (!user2Score.containsKey(userId)) {
                    user2Score.put(userId, 10);
                }
                
                if (type.equals(LibraryRequest.Type.QUERIED)) {
                    PRINTER.info(req, library.queryBook(bookId));
                } else if (type.equals(LibraryRequest.Type.BORROWED)) {
                    Main.outAcOrRej(library.borrowBook(date, userId, bookId), req);
                } else if (type.equals(LibraryRequest.Type.RETURNED)) {
                    if (library.isOutDate(date, userId, bookId)) {
                        PRINTER.accept(req, "overdue");
                    } else {
                        int score = user2Score.get(userId) + 1; // 必然存在
                        if (score > 20) {
                            score = 20;
                        }
                        user2Score.put(userId, score);
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
                    library.donateBook(userId, bookId);
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
