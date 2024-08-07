import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = parseInt(scanner.nextLine().trim());
        Operator operator = new Operator();
        for (int i = 0; i < n; i++) {
            String nextLine1 = scanner.nextLine();
            String[] strings1 = nextLine1.trim().split(" +");
            if (parseInt(strings1[0]) < 14) {
                operator.basicOp(new ArrayList<>(Arrays.asList(strings1)));
            }
            else if (parseInt(strings1[0]) == 14) {
                ArrayList<ArrayList<String>> fightBlock = new ArrayList<>();
                fightBlock.add(new ArrayList<>(Arrays.asList(strings1)));
                int k = parseInt(strings1[2]);
                for (int  j = 0; j < k; j++) {
                    String nextLine2 = scanner.nextLine();
                    String[] strings2 = nextLine2.trim().split(" +");
                    fightBlock.add(new ArrayList<>(Arrays.asList(strings2)));
                }
                operator.battleOp(fightBlock);
            }
            else {
                operator.logOp(new ArrayList<>(Arrays.asList(strings1)));
            }
        }
    }
}