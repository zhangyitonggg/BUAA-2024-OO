import java.util.ArrayList;

public class Store {
    private static Store store;
    private ArrayList<Integer> bottleLog;
    private ArrayList<Integer> equipmentLog;
    private ArrayList<Integer> foodLog;
    private long bottleMoney;
    private long equipmentMoney;
    private long foodMoney;
    
    private Store() {
        bottleLog = new ArrayList<>();
        equipmentLog = new ArrayList<>();
        foodLog = new ArrayList<>();
    }
    
    public static Store getInstance() {
        if (store == null) {
            store = new Store();
        }
        return store;
    }
    
    public void sellBottle(int maxCap,long price) {
        bottleLog.add(maxCap);
        bottleMoney += price;
    }
    
    public void sellEquipment(int star,long price) {
        equipmentLog.add(star);
        equipmentMoney += price;
    }
    
    public void sellFood(int energy,long price) {
        foodLog.add(energy);
        foodMoney += price;
    }
    
    public long BottlePrice() {
        return (long)(bottleMoney / bottleLog.size());
    }
    
    public long EquipmentPrice() {
        return (long)(equipmentMoney / equipmentLog.size());
    }
    
    public long FoodPrice() {
        return (long)(foodMoney / foodLog.size());
    }
    
    public Bottle buyBottle(int id,String name,String type,ArrayList<String> line) {
        long price = BottlePrice();
        Bottle bottle = null;
        long allCapacity = 0;
        for (int i = 0;i < bottleLog.size();i++) {
            allCapacity += (long)bottleLog.get(i);
        }
        int capacity = (int)(allCapacity / bottleLog.size());
        if (type.equals("RegularBottle")) {
            bottle = new RegularBottle(id,name,capacity,price);
        }
        else if (type.equals("ReinforcedBottle")) {
            bottle = new ReinforcedBottle(id,name,capacity,price,Double.parseDouble(line.get(5)));
        }
        else {
            bottle = new RecoverBottle(id,name,capacity,price,Double.parseDouble(line.get(5)));
        }
        return bottle;
    }
    
    public Equipment buyEquipment(int id,String name,String type,ArrayList<String> line) {
        long price = EquipmentPrice();
        Equipment equipment = null;
        long allStar = 0;
        for (int i = 0;i < equipmentLog.size();i++) {
            allStar += (long)equipmentLog.get(i);
        }
        int star = (int)(allStar / equipmentLog.size());
        if (type.equals("RegularEquipment")) {
            equipment = new RegularEquipment(id,name,star,price);
        }
        else if (type.equals("CritEquipment")) {
            equipment = new CritEquipment(id,name,star,price,Integer.parseInt(line.get(5)));
        }
        else {
            equipment = new EpicEquipment(id,name,star,price,Double.parseDouble(line.get(5)));
        }
        return equipment;
    }
    
    public Food buyFood(int id,String name,String type) {
        long price = FoodPrice();
        long allEnergy = 0;
        for (int i = 0;i < foodLog.size();i++) {
            allEnergy += (long)foodLog.get(i);
        }
        int energy = (int)(allEnergy / foodLog.size());
        return new Food(id,name,energy,price);
    }
}
