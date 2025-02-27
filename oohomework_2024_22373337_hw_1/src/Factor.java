public abstract class Factor {
    private int exp;
    
    public Factor() {
        this.exp = 1;
    }
    
    public void setExp(int exp) {
        this.exp = exp;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public abstract Poly toPoly();
}
