public class ExprFactor extends Factor {
    private Expr base;
    
    public ExprFactor(Expr base) {
        this.base = base;
    }

    @Override
    public Poly toPoly() {
        Poly poly = base.toPoly();
        poly = poly.powPoly(this.getPowExp());
        return poly;
    }
    
    @Override
    public String toString() {
        return "(" + base.toString() + ")" + "^" + getPowExp();
    }
}
