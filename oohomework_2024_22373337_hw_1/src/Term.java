import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;
    private int sign;
    
    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }
    
    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }
    
    public void setSign(int sign) {
        this.sign = sign;
    }
    
    public int getSign() {
        return this.sign;
    }
    
    public Poly toPoly() {
        // 得到1或者-1的基项，无须后续根据符号取反
        Poly poly = new Poly(this.sign);
        for (Factor factor : factors) {
            poly = poly.multiPoly(factor.toPoly());
        }
        return poly;
    }
}
