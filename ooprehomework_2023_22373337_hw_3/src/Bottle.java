public class Bottle {
    private int id;
    private String name;
    private int capacity;
    
    public Bottle(int id,String name,int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
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
}
