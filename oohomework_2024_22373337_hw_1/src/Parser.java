import java.math.BigInteger;

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
        else {
            factor = new ConFactor(new BigInteger(lexer.getToken()));
            lexer.next();
        }
        
        if (lexer.getType() == Lexer.Type.EXP) {
            lexer.next();
            factor.setExp(Integer.parseInt(lexer.getToken()));
            lexer.next();
        }
        else {
            factor.setExp(1);
        }
        
        return factor;
    }
}
