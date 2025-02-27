import java.math.BigInteger;

public class Mono {
    private BigInteger coef;
    private int exp;
    
    public Mono(String coefStr,String expStr) {
        this.coef = new BigInteger(coefStr);
        this.exp = Integer.parseInt(expStr);
    }
    
    public Mono(BigInteger coefStr,int expStr) {
        this.coef = coefStr;
        this.exp = expStr;
    }
    
    public String toString() {
        if (exp == 0) {
            if (coef.compareTo(new BigInteger("1")) == 0) {
                return "1";
            }
            else if (coef.compareTo(new BigInteger("-1")) == 0) {
                return "-1";
            }
            else {
                return coef.toString();
            }
        }
        else if (exp == 1) {
            if (coef.compareTo(new BigInteger("1")) == 0) {
                return "x";
            }
            else if (coef.compareTo(new BigInteger("-1")) == 0) {
                return "-x";
            }
            else {
                return coef.toString() + "*" + "x";
            }
        }
        else {
            if (coef.compareTo(new BigInteger("1")) == 0) {
                return "x" + "^" + String.valueOf(exp);
            }
            else if (coef.compareTo(new BigInteger("-1")) == 0) {
                return "-x" + "^" + String.valueOf(exp);
            }
            return coef.toString() + "*" + "x" + "^" + String.valueOf(exp);
        }
    }
    
    public BigInteger getCoef() {
        return this.coef;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public void setCoef(BigInteger newCoef) {
        this.coef = newCoef;
    }
    
    public int getSign() {
        if (coef.toString().charAt(0) == '-') {
            return -1;
        }
        return 1;
    }
}
