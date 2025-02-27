public class ExprFactor extends Factor {
    private Expr linFactor;
    
    public ExprFactor(Expr linFactor) {
        this.linFactor = linFactor;
    }

    @Override
    public Poly toPoly() {
        Poly poly = linFactor.toPoly();
        poly = poly.powPoly(this.getExp());
        return poly;
    }
}
