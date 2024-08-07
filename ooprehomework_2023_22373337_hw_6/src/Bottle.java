public class Bottle {
    private int id;
    private String name;
    private int capacity;
    
    private long price;
    
    public Bottle(int id,String name,int capacity,long price) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
    }
    
    public void drinkBottle() {
        this.capacity = 0;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public long getPrice() { return this.price; }
}
