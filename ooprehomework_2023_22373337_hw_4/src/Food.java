public class Food {
    private int id;
    private String name;
    private int energy;
    
    public Food(int id,String name,int energy) {
        this.id = id;
        this.name = name;
        this.energy = energy;
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
}
