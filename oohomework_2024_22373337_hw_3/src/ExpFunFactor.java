import java.math.BigInteger;
import java.util.ArrayList;

public class ExpFunFactor extends Factor {
    private Factor exp;
    
    public ExpFunFactor(Factor exp) {
        this.exp = exp;
    }
    
    @Override
    public Poly toPoly() {
        Mono powExpMono = new Mono(this.getPowExp(),BigInteger.ZERO,new Poly());
        ArrayList<Mono> powExpMonos = new ArrayList<>();
        powExpMonos.add(powExpMono);
        Poly powExpPoly = new Poly(powExpMonos);
        Poly expPoly = exp.toPoly().multiPoly(powExpPoly);
        
        Mono mono = new Mono(BigInteger.ONE,BigInteger.ZERO,expPoly);
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        return new Poly(monos);
    }
    
    @Override
    public Poly toDerivative() {
        if (this.getPowExp().equals(BigInteger.ZERO)) {
            return new Poly(0);
        }
        Poly polyCoe = new Poly(this.getPowExp());
        Poly polyDer = exp.toDerivative();
        
        if (this.getPowExp().equals(BigInteger.ONE)) {
            return polyCoe.multiPoly(polyDer).multiPoly(this.toPoly());
        }
        
        Mono monoPowExp = new Mono(this.getPowExp(),BigInteger.ZERO,new Poly());
        ArrayList<Mono> monoPowExps = new ArrayList<>();
        monoPowExps.add(monoPowExp);
        Poly powExpPoly = new Poly(monoPowExps);
        Poly polyTemp = exp.toPoly().multiPoly(powExpPoly);
        Mono mono = new Mono(BigInteger.ONE,BigInteger.ZERO,polyTemp);
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        Poly polyExp = new Poly(monos);
        
        return polyCoe.multiPoly(polyDer).multiPoly(polyExp);
    }
    
    @Override
    public String toString() {
        return "exp" + "(" + exp.toString() + ")" + "^" + getPowExp();
    }
}
