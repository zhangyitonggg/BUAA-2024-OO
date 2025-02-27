public class DerivativeFactor extends Factor {
    private Expr base;
    
    public DerivativeFactor(Expr base) {
        this.base = base;
    }
    
    @Override
    public Poly toPoly() {
        return base.toDerivative();
    }
    
    @Override
    public Poly toDerivative() {
        Poly poly = this.toPoly().deepClone();
        String ans = Optimizer.laterProduce(poly);

        Lexer lexer = new Lexer(ans);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        
        return expr.toDerivative();
    }
    
    @Override
    public String toString() {
        return "dx(" + base.toString() + ")";
    }

}
