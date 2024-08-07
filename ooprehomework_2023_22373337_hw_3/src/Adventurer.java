import java.util.HashMap;

public class Adventurer {
    private int id;
    private String name;
    private int hitPoint;
    private int level;
    private Bag bag = new Bag();
    private HashMap<Integer,Bottle> bottles = new HashMap<>();
    private HashMap<Integer,Equipment> equipments = new HashMap<>();
    private HashMap<Integer,Food> foods = new HashMap<>();
    
    public Adventurer(int id,String name) {
        this.id = id;
        this.name = name;
        this.level = 1;
        this.hitPoint = 500;
    }
    
    public void addBottle(int id,String name,int capacity) {
        bottles.put(id,new Bottle(id,name,capacity));
    }
    
    public void deleteBottle(int id) {
        Bottle bottle = bottles.get(id);
        bag.removeBottle(bottle);
        System.out.println(bottles.size() - 1 + " "  + bottle.getName());
        bottles.remove(id,bottle);
    }
    
    public void addEquipment(int id,String name,int star) {
        equipments.put(id,new Equipment(id,name,star));
    }
    
    public void deleteEquipment(int id) {
        Equipment equipment = equipments.get(id);
        bag.removeEquipment(equipment);
        System.out.println(equipments.size() - 1 + " " + equipment.getName());
        equipments.remove(id,equipment);
    }
    
    public void upStar(int id) {
        Equipment equipment = equipments.get(id);
        equipment.upStar();
    }
    
    public void addFood(int id,String name,int energy) {
        foods.put(id,new Food(id,name,energy));
    }
    
    public void deleteFood(int id) {
        Food food = foods.get(id);
        bag.removeFood(food);
        System.out.println(foods.size() - 1 + " " + food.getName());
        foods.remove(id,food);
    }
    
    public  void carryEquipment(int id) {
        bag.carryEquipment(equipments.get(id));
    }
    
    public void carryBottle(int id) {
        bag.carryBottle(bottles.get(id),this.level);
    }
    
    public void carryFood(int id) {
        bag.carryFood(foods.get(id));
    }
    
    public void drinkBottle(String name) {
        Bottle bottle = bag.drinkBottle(name);
        if (bottle == null) {
            System.out.println("fail to use " + name);
        }
        else {
            int id = bottle.getId();
            if (bottle.getCapacity() == 0) {
                bottles.remove(bottle.getId(),bottle);
            }
            else {
                this.hitPoint += bottle.getCapacity();
                bottle.drinkBottle();
            }
            System.out.println(id + " " + this.hitPoint);
        }
    }
    
    public void eatFood(String name) {
        Food food = bag.eatFood(name);
        if (food == null) {
            System.out.println("fail to eat " + name);
        }
        else {
            this.level += food.getEnergy();
            System.out.println(food.getId() + " " + this.level);
            foods.remove(food.getId(),food);
        }
    }
}
