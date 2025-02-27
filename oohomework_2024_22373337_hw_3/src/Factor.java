import java.math.BigInteger;

public abstract class Factor {
    private BigInteger powExp;
    
    public Factor() {
        this.powExp = new BigInteger("1");
    }
    
    public void setPowExp(BigInteger powExp) {
        this.powExp = powExp;
    }
    
    public BigInteger getPowExp() {
        return this.powExp;
    }
    
    public abstract Poly toPoly();
    
    public abstract Poly toDerivative();
}
