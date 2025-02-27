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
    
    public Poly toDerivative() {
        Poly poly = new Poly(0);
        int num = factors.size();
        for (int iter1 = 0; iter1 < num; ++iter1) {
            Poly temp = new Poly(this.sign);
            for (int iter2 = 0; iter2 < num; ++iter2) {
                if (iter1 == iter2) {
                    temp = temp.multiPoly(factors.get(iter2).toDerivative());
                }
                else {
                    temp = temp.multiPoly(factors.get(iter2).toPoly());
                }
            }
            poly = poly.addPoly(temp);
        }
        return poly;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(factors.get(0).toString());
        for (int i = 1; i < factors.size(); i++) {
            sb.append("*");
            sb.append(factors.get(i).toString());
        }
        return sb.toString();
    }
}
