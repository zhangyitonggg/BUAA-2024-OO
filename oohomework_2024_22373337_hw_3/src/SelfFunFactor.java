public class SelfFunFactor extends  Factor {
    private Expr base = new Expr();
    
    public SelfFunFactor(String string) {
        Lexer lexer = new Lexer(string);
        Parser parser = new Parser(lexer);
        base = parser.parseExpr();
    }
    
    @Override
    public Poly toPoly() {
        return base.toPoly();
    }
    
    @Override
    public Poly toDerivative() {
        return base.toDerivative();
    }

    @Override
    public String toString() {
        return "(" + base.toString() + ")" + "^" + this.getPowExp();
    }
}
