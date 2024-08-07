public class Equipment {
    private int id;
    private int star;
    private String name;
    
    public Equipment(int id,String name,int star) {
        this.id = id;
        this.name = name;
        this.star = star;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int upStar() {
        this.star += 1;
        return this.star;
    }
}
