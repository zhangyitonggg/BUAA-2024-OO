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
    public String toString() {
        return "exp" + "(" + exp.toString() + ")" + "^" + getPowExp();
    }
}
