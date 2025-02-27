import java.math.BigInteger;
import java.util.ArrayList;

public class PowFactor extends Factor {
    private  String linFactor;
    
    public PowFactor(String linFactor) {
        this.linFactor = linFactor;
    }
   
    @Override
    public Poly toPoly() {
        Mono mono = new Mono(BigInteger.ONE,this.getExp());
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        return new Poly(monos);
    }
}
