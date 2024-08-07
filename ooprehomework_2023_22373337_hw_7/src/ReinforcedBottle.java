public class ReinforcedBottle extends Bottle {
    private double ratio;
    
    public ReinforcedBottle(int id,String name,int capacity,long price,double ratio) {
        super(id,name,capacity,price);
        this.ratio = ratio;
    }
    
    public int getAddCap() {
        double temp = (1 + ratio) * super.getCapacity();
        if (Math.abs(temp - Math.round(temp)) < Double.MIN_VALUE) {
            return (int)(temp + 0.5);
        }
        else {
            return (int)temp;
        }
    }
}
