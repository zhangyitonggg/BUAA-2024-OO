public class Food {
    private int id;
    private String name;
    private int energy;
    
    private long price;
    
    public Food(int id,String name,int energy,long price) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.price = price;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getEnergy() {
        return this.energy;
    }
    
    public long getPrice() { return this.price; }
}
