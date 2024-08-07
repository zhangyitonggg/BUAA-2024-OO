import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Demon demon = new Demon(scanner);
        demon.solve();
    }
}
