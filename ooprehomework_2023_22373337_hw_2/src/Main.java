import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer,Adventurer> adventurers = new HashMap<>();
        ArrayList<ArrayList<String>> inputInfo = new ArrayList<>(); // 解析后的输入将会存进该容器中, 类似于c语言的二维数组
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim()); // 读取行数
        for (int i = 0; i < n; ++i) {
            String nextLine = scanner.nextLine(); // 读取本行指令
            String[] strings = nextLine.trim().split(" +"); // 按空格对行进行分割
            inputInfo.add(new ArrayList<>(Arrays.asList(strings))); // 将指令分割后的各个部分存进容器中
        }
        
        for (int i = 0; i < n; i++) {
            int t = Integer.parseInt(inputInfo.get(i).get(0));
            int id = Integer.parseInt(inputInfo.get(i).get(1));
            if (t == 1) {
                Adventurer adventurer = new Adventurer(id,inputInfo.get(i).get(2));
                adventurers.put(id,adventurer);
            }
            else if (t == 2) {
                Adventurer adventurer = adventurers.get(id);
                int botId = Integer.parseInt(inputInfo.get(i).get(2));
                int capacity = Integer.parseInt(inputInfo.get(i).get(4));
                adventurer.addBottle(botId,inputInfo.get(i).get(3),capacity);
            }
            else if (t == 3) {
                Adventurer adventurer = adventurers.get(id);
                int botId = Integer.parseInt(inputInfo.get(i).get(2));
                adventurer.deleteBottle(botId);
            }
            else if (t == 4) {
                Adventurer adventurer = adventurers.get(id);
                int equId = Integer.parseInt(inputInfo.get(i).get(2));
                int star = Integer.parseInt(inputInfo.get(i).get(4));
                adventurer.addEquip(equId,inputInfo.get(i).get(3),star);
            }
            else if (t == 5) {
                Adventurer adventurer = adventurers.get(id);
                int equId = Integer.parseInt(inputInfo.get(i).get(2));
                adventurer.deleteEquip(equId);
            }
            else {
                Adventurer adventurer = adventurers.get(id);
                int equId = Integer.parseInt(inputInfo.get(i).get(2));
                adventurer.upStar(equId);
            }
        }
    }
}
