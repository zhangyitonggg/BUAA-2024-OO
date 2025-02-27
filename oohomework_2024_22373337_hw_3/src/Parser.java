import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public Expr parseExpr() {
        Expr expr = new Expr();
        int sign = 1;
        if (lexer.getType() == Lexer.Type.ADD) {
            lexer.next();
        }
        else if (lexer.getType() == Lexer.Type.SUB) {
            lexer.next();
            sign = -1;
        }
        expr.addTerm(parseTerm(sign));
        
        while (lexer.getType() == Lexer.Type.ADD || lexer.getType() == Lexer.Type.SUB) {
            if (lexer.getType() == Lexer.Type.ADD) {
                lexer.next();
                expr.addTerm(parseTerm(1));
            }
            else {
                lexer.next();
                expr.addTerm(parseTerm(-1));
            }
        }
        return expr;
    }
    
    public Term parseTerm(int sign) {
        Term term = new Term(sign);
        
        term.addFactor(parseFactor());
        
        while (lexer.getType() == Lexer.Type.MULTI) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }
    
    public  Factor parseFactor() {
        Factor factor;
        if (lexer.getType() == Lexer.Type.L) {
            lexer.next();
            factor = new ExprFactor(parseExpr());
            lexer.next();
        }
        else if (lexer.getType() == Lexer.Type.X) {
            factor = new PowFactor(lexer.getToken());
            lexer.next();
        }
        else if (lexer.getType() == Lexer.Type.SELFFUN) {
            String funName = lexer.getToken();
            String string = Transform.replaceParas(funName, this.getArgs());
            factor = new SelfFunFactor(string);
        }
        else if (lexer.getType() == Lexer.Type.EXPFUN) {
            lexer.next();
            factor = new ExpFunFactor(parseFactor());
            lexer.next();
        }
        else if (lexer.getType() == Lexer.Type.DX) {
            lexer.next();
            factor = new DerivativeFactor(parseExpr());
            lexer.next();
        }
        else {
            factor = new ConFactor(new BigInteger(lexer.getToken()));
            lexer.next();
        }
        
        if (lexer.getType() == Lexer.Type.POWEXP) {
            lexer.next();
            factor.setPowExp(new BigInteger(lexer.getToken()));
            lexer.next();
        }
        else {
            factor.setPowExp(BigInteger.ONE);
        }
        
        return factor;
    }
    
    public ArrayList<String> getArgs() {
        ArrayList<String> args = new ArrayList<>();
        lexer.next(); // 到Factor
        args.add(parseFactor().toString());
        while (lexer.getType() == Lexer.Type.DOT) {
            lexer.next();
            args.add(parseFactor().toString());
        }
        lexer.next();
        return args;
    }
}
