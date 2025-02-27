import java.math.BigInteger;

public class Mono {
    private BigInteger coef;
    private BigInteger powExp;
    private Poly exp;
    private BigInteger polyExp;
    
    public Mono(String coefStr,String powExpStr, Poly poly) {
        exp = poly;
        this.coef = new BigInteger(coefStr);
        this.powExp = new BigInteger(powExpStr);
        this.polyExp = BigInteger.ONE;
    }
    
    public Mono(BigInteger coefStr,BigInteger powExpStr, Poly poly) {
        exp = poly;
        this.coef = coefStr;
        this.powExp = powExpStr;
        this.polyExp = BigInteger.ONE;
    }
    
    public void setPolyExp(BigInteger polyExp) {
        this.polyExp = polyExp;
    }
    
    public boolean canAdd(Mono other) {
        if (!other.powExp.equals(this.powExp) || !this.getExp().equals(other.getExp())) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        if (powExp.equals(BigInteger.ZERO) && exp.getMonos().isEmpty()) {
            return coef.toString();
        }
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        if (coef.compareTo(new BigInteger("-1")) == 0) {
            sb.append("-");
            flag = 1;
        }
        else if (coef.compareTo(new BigInteger("1")) == 0) {
            flag = 1;
        }
        else {
            sb.append(coef.toString() + "*");
        }
        
        if (powExp.equals(BigInteger.ONE)) {
            sb.append("x");
        }
        else if (powExp.compareTo(BigInteger.ONE) > 0) {
            sb.append("x^" + powExp);
        }
        
        if (!powExp.equals(BigInteger.ZERO) && !exp.getMonos().isEmpty()) {
            sb.append("*");
        }
        
        if (!exp.getMonos().isEmpty()) {
            sb.append("exp(");
            Boolean isOneTerm = (exp.getMonos().size() == 1);
            Mono mono = exp.getMonos().get(0);
            Boolean isOneFactor = false;
            if (mono.getPowExp().equals(BigInteger.ZERO) && mono.getExp().getMonos().isEmpty()) {
                isOneFactor = true;
            }
            else if (mono.getCoef().compareTo(BigInteger.ONE) == 0
                    && mono.getExp().getMonos().isEmpty()) {
                isOneFactor = true;
            }
            else if (mono.getCoef().compareTo(BigInteger.ONE) == 0 &&
                    mono.getPowExp().compareTo(BigInteger.ZERO) == 0) {
                isOneFactor = true;
            }
            
            if (isOneTerm && isOneFactor) {
                sb.append(Optimizer.laterProduce(exp));
            }
            else {
                sb.append("(");
                sb.append(Optimizer.laterProduce(exp));
                sb.append(")");
            }
            sb.append(")");
            
            if (polyExp.compareTo(BigInteger.ONE) != 0) {
                sb.append("^" + polyExp);
            }
        }
        return sb.toString();
    }

    public BigInteger getCoef() {
        return this.coef;
    }
    
    public void setCoef(BigInteger newCoef) {
        this.coef = newCoef;
    }
    
    public BigInteger getPowExp() {
        return this.powExp;
    }
    
    public Poly getExp() {
        return this.exp;
    }
    
    public void setExp(Poly poly) {
        this.exp = poly;
    }
    
    public int getSign() {
        if (coef.toString().charAt(0) == '-') {
            return -1;
        }
        return 1;
    }
}
