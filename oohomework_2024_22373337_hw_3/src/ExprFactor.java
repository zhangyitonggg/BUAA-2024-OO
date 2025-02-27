import java.math.BigInteger;

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
    public Poly toDerivative() {
        if (getPowExp().equals(BigInteger.ZERO)) {
            return new Poly(0);
        }
        else if (getPowExp().equals(BigInteger.ONE)) {
            return base.toDerivative();
        }
        else {
            Poly polyCoe = new Poly(getPowExp());
            Poly polyDer = base.toDerivative();
            Poly polyPow = base.toPoly().powPoly(this.getPowExp().subtract(BigInteger.ONE));
            return polyCoe.multiPoly(polyDer).multiPoly(polyPow);
        }
    }
    
    @Override
    public String toString() {
        return "(" + base.toString() + ")" + "^" + getPowExp();
    }
    
}
