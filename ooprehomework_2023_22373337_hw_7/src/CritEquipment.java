public class CritEquipment extends Equipment {
    private int critical;
    
    public CritEquipment(int id,String name,int star,long price,int critical) {
        super(id,name,star,price);
        this.critical = critical;
    }
    
    public int getCritical() {
        return this.critical;
    }
}
