import java.util.ArrayList;
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
    private HashMap<Integer,Adventurer> adventurers = new HashMap<>();
    
    public Adventurer(int id,String name) {
        this.id = id;
        this.name = name;
        this.level = 1;
        this.hitPoint = 500;
    }
    
    public void employAdventurer(int advId,Adventurer employee) {
        if (!adventurers.containsKey(advId)) {
            adventurers.put(advId, employee);
        }
    }
    
    public long getPrice() {
        long value = 0;
        for (Bottle bottle:bottles.values()) {
            value += bottle.getPrice();
        }
        for (Food food:foods.values()) {
            value += food.getPrice();
        }
        for (Equipment equipment:equipments.values()) {
            value += equipment.getPrice();
        }
        for (Adventurer adventurer:adventurers.values()) {
            value += adventurer.getPrice();
        }
        return value;
    }
    
    public int getComCount() {
        return bottles.size() + foods.size() + equipments.size() + adventurers.size();
    }
    
    public long getComMax() {
        long max = 0;
        for (Bottle temp:bottles.values()) {
            if (max < temp.getPrice()) {
                max = temp.getPrice();
            }
        }
        for (Food temp:foods.values()) {
            if (max < temp.getPrice()) {
                max = temp.getPrice();
            }
        }
        for (Equipment temp:equipments.values()) {
            if (max < temp.getPrice()) {
                max = temp.getPrice();
            }
        }
        for (Adventurer temp:adventurers.values()) {
            if (max < temp.getPrice()) {
                max = temp.getPrice();
            }
        }
        return max;
    }
    
    public void findClassType(int comId) {
        System.out.print("Commodity whose id is " + comId + " belongs to ");
        for (Adventurer adventurer:adventurers.values()) {
            if (adventurer.getId() == comId) {
                System.out.println("Adventurer");
                return;
            }
        }
        for (Food food: foods.values()) {
            if (food.getId() == comId) {
                System.out.println("Food");
                return;
            }
        }
        for (Bottle bottle: bottles.values()) {
            if (bottle.getId() == comId) {
                if (bottle instanceof RegularBottle) {
                    System.out.println("RegularBottle");
                }
                else if (bottle instanceof ReinforcedBottle) {
                    System.out.println("ReinforcedBottle");
                }
                else {
                    System.out.println("RecoverBottle");
                }
                return;
            }
        }
        for (Equipment equipment: equipments.values()) {
            if (equipment.getId() == comId) {
                if (equipment instanceof RegularEquipment) {
                    System.out.println("RegularEquipment");
                }
                else if (equipment instanceof  CritEquipment) {
                    System.out.println("CritEquipment");
                }
                else {
                    System.out.println("EpicEquipment");
                }
                return;
            }
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean getBottle(String name) {
        Bottle bottle = bag.drinkBottle(name);
        if (bottle != null) {
            System.out.print(bottle.getId() + " ");
            if (bottle.getCapacity() == 0) {
                bottles.remove(bottle.getId(),bottle);
            }
            else {
                if (bottle instanceof RegularBottle) {
                    this.hitPoint += bottle.getCapacity();
                }
                else if (bottle instanceof ReinforcedBottle) {
                    this.hitPoint += ((ReinforcedBottle) bottle).getAddCap();
                }
                else if (bottle instanceof RecoverBottle) {
                    double ratio = ((RecoverBottle) bottle).getRatio();
                    double temp = ratio * this.hitPoint;
                    if (Math.abs(temp - Math.round(temp)) < Double.MIN_VALUE) {
                        this.hitPoint += (int)(temp + 0.5);
                    }
                    else {
                        this.hitPoint += (int)temp;
                    }
                }
                bottle.drinkBottle();
            }
            System.out.println(this.hitPoint);
            return true;
        }
        return false;
    }
    
    public Equipment getEquipment(String name) {
        return bag.getEquipment(name);
    }
    
    public void addBottle(ArrayList<String> line) {
        int botId = Integer.parseInt(line.get(2));
        String botName = line.get(3);
        int capacity = Integer.parseInt(line.get(4));
        long price = Long.parseLong(line.get(5));
        String type = line.get(6);
        if (type.equals("RegularBottle")) {
            bottles.put(botId,new RegularBottle(botId,botName,capacity,price));
        }
        else if (type.equals("ReinforcedBottle")) {
            double ratio = Double.parseDouble(line.get(7));
            bottles.put(botId,new ReinforcedBottle(botId,botName,capacity,price,ratio));
        }
        else  {
            double ratio = Double.parseDouble(line.get(7));
            bottles.put(botId,new RecoverBottle(botId,botName,capacity,price,ratio));
        }
    }
    
    public void deleteBottle(int id) {
        Bottle bottle = bottles.get(id);
        bag.removeBottle(bottle);
        System.out.println(bottles.size() - 1 + " "  + bottle.getName());
        bottles.remove(id,bottle);
    }
    
    public void addEquipment(ArrayList<String> line) {
        int botId = Integer.parseInt(line.get(2));
        String botName = line.get(3);
        int star = Integer.parseInt(line.get(4));
        long price = Long.parseLong(line.get(5));
        String type = line.get(6);
        if (type.equals("RegularEquipment")) {
            equipments.put(botId,new RegularEquipment(botId,botName,star,price));
        }
        else if (type.equals("CritEquipment")) {
            int critical = Integer.parseInt(line.get(7));
            equipments.put(botId,new CritEquipment(botId,botName,star,price,critical));
        }
        else  {
            double ratio = Double.parseDouble(line.get(7));
            equipments.put(botId,new EpicEquipment(botId,botName,star,price,ratio));
        }
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
    
    public void addFood(int id,String name,int energy,long price) {
        foods.put(id,new Food(id,name,energy,price));
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
                if (bottle instanceof RegularBottle) {
                    this.hitPoint += bottle.getCapacity();
                }
                else if (bottle instanceof ReinforcedBottle) {
                    this.hitPoint += ((ReinforcedBottle) bottle).getAddCap();
                }
                else if (bottle instanceof RecoverBottle) {
                    double ratio = ((RecoverBottle) bottle).getRatio();
                    double temp = ratio * this.hitPoint;
                    if (Math.abs(temp - Math.round(temp)) < Double.MIN_VALUE) {
                        this.hitPoint += (int)(temp + 0.5);
                    }
                    else {
                        this.hitPoint += (int)temp;
                    }
                }
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
    
    public int beAttacked(int decrease) {
        this.hitPoint -= decrease;
        return this.hitPoint;
    }
    
    public  void attackOne(Adventurer fighter,Equipment equipment) {
        int decrease = this.getDecrease(equipment,fighter);
        System.out.print(fighter.getId() + " ");
        System.out.println(fighter.beAttacked(decrease));
    }
    
    public void attackAll(ArrayList<Adventurer> fightPersons, Equipment equipment) {
        for (Adventurer fighter:fightPersons) {
            if (fighter.getId() != this.id) {
                int decrease = this.getDecrease(equipment,fighter);
                System.out.print(fighter.beAttacked(decrease) + " ");
            }
        }
        System.out.print("\n");
    }
    
    public int getDecrease(Equipment equipment,Adventurer fighter) {
        if (equipment instanceof RegularEquipment) {
            return this.level * equipment.getStar();
        }
        else if (equipment instanceof CritEquipment) {
            return this.level * equipment.getStar() + ((CritEquipment) equipment).getCritical();
        }
        else {
            double temp = fighter.hitPoint * ((EpicEquipment) equipment).getRatio();
            if (Math.abs(temp - Math.round(temp)) < Double.MIN_VALUE) {
                return (int)(temp + 0.1);
            }
            else {
                return (int)temp;
            }
        }
    }
}
