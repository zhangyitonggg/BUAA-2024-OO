import java.math.BigInteger;
import java.util.ArrayList;

public class ConFactor extends Factor {
    private BigInteger linFactor;
    
    public ConFactor(BigInteger linFactor) {
        this.linFactor = linFactor;
    }

    @Override
    public Poly toPoly() {
        Mono mono = new Mono(linFactor,0);
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        return new Poly(monos);
    }
}
