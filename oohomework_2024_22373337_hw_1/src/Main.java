import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static  void main(String[] args) {
        // get initStr
        Scanner scanner = new Scanner(System.in);
        String initStr = scanner.nextLine();
        // preProcess
        Optimizer optimizer = new Optimizer();
        String input = optimizer.preProduce(initStr);
        // lexer && parser
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        // get normal poly
        Poly poly = expr.toPoly();
        ArrayList<Mono> monos = poly.getMonos();
        // laterProduce
        String ans = optimizer.laterProduce(monos);
        System.out.println(ans);
    }
}
