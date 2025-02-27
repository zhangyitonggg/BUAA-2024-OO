import java.util.Scanner;

public class Main {
    public static  void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Transform.addFunc(scanner);

        String input = Optimizer.preProduce(scanner.nextLine());
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();

        Poly poly = expr.toPoly();
        String ans = Optimizer.laterProduce(poly);
        System.out.println(ans);
    }
}
