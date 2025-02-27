import java.math.BigInteger;
import java.util.ArrayList;

public class PowFactor extends Factor {
    private  String base;
    
    public PowFactor(String base) {
        this.base = base;
    }
   
    @Override
    public Poly toPoly() {
        Mono mono = new Mono(BigInteger.ONE,this.getPowExp(),new Poly());
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        return new Poly(monos);
    }
    
    @Override
    public String toString() {
        return base + "^" + getPowExp();
    }
}
