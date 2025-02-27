import java.math.BigInteger;
import java.util.ArrayList;

public class ConFactor extends Factor {
    private BigInteger base;
    
    public ConFactor(BigInteger base) {
        this.base = base;
    }

    @Override
    public Poly toPoly() {
        Mono mono = new Mono(base,BigInteger.ZERO,new Poly());
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        return new Poly(monos);
    }
    
    @Override
    public Poly toDerivative() {
        return new Poly(0);
    }
    
    @Override
    public String toString() {
        return base.toString();
    }
}
