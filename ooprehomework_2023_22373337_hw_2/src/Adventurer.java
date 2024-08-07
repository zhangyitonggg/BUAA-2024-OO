import java.util.HashMap;

public class Adventurer {
    private int id;
    private String name;
    private HashMap<Integer,Bottle> bottles = new HashMap<>();
    private HashMap<Integer,Equipment> equipments = new HashMap<>();
    
    public Adventurer(int id,String name) {
        this.id = id;
        this.name = name;
    }
    
    public void addBottle(int botId,String name,int capacity) {
        Bottle bottle = new Bottle(botId,name,capacity);
        bottles.put(botId,bottle);
    }
    
    public void deleteBottle(int botId) {
        Bottle bottle = bottles.get(botId);
        System.out.print(bottles.size() - 1);
        System.out.println(" " + bottle.getName());
        bottles.remove(botId);
    }
    
    public void addEquip(int equId,String name,int star) {
        Equipment equipment = new Equipment(equId,name,star);
        equipments.put(equId,equipment);
    }
    
    public void deleteEquip(int equId) {
        Equipment equipment = equipments.get(equId);
        System.out.print(equipments.size() - 1);
        System.out.println(" " + equipment.getName());
        equipments.remove(equId);
    }
    
    public void upStar(int equId) {
        Equipment equipment = equipments.get(equId);
        System.out.print(equipment.getName());
        System.out.println(" " + equipment.upStar());
    }
}
