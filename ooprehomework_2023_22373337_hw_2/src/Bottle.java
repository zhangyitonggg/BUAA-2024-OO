public class Bottle {
    private int id;
    private String name;
    private int capacity;
    
    public Bottle(int id,String name,int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
    
    public String getName() {
        return this.name;
    }
}
