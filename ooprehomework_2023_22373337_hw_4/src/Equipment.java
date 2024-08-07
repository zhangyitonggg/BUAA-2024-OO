public class Equipment {
    private int id;
    private int star;
    private String name;
    
    public Equipment(int id,String name,int star) {
        this.id = id;
        this.name = name;
        this.star = star;
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
}
