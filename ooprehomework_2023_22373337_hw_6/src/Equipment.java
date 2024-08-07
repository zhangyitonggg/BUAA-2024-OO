public class Equipment {
    private int id;
    private int star;
    private String name;
    
    private long price;
    
    public Equipment(int id,String name,int star,long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.price = price;
    }
    
    public void upStar() {
        this.star += 1;
        System.out.println(this.name + " " + this.star);
    }
    
    public int getStar() {
        return this.star;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public long getPrice() { return this.price; }
}
