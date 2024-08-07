import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Store store = new Store(5, 5);
        Child child = new Child(20);
        Scanner scanner = new Scanner(System.in);
        int opCount = Integer.parseInt(scanner.nextLine()); // 指令条数
        for (int i = 0; i < opCount; ++i) {
            String instr = scanner.nextLine();
            if (instr.startsWith("eat")) {
                child.eatOneFruit(instr.substring(4));
            } else {
                child.buyFromStore(instr.substring(4), store);
            }
        }
        scanner.close(); // 关闭scanner
    }
}
