public class EpicEquipment extends Equipment {
    private double ratio;
    
    public EpicEquipment(int id,String name,int star,long price,double ratio) {
        super(id, name, star, price);
        this.ratio = ratio;
    }
    
    public double getRatio() {
        return ratio;
    }
}
