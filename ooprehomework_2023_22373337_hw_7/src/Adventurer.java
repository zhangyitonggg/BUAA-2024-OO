import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Adventurer {
    private int id;
    private String name;
    private int hitPoint;
    private int level;
    private long money;
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
        this.money = 0;
    }
    
    public void buyObject(ArrayList<String> line) {
        Store store = Store.getInstance();
        int id = Integer.parseInt(line.get(2));
        String name = line.get(3);
        String type = line.get(4);
        if (type.contains("Bottle")) {
            if (this.money >= store.BottlePrice()) {
                bottles.put(id,store.buyBottle(id,name,type,line));
                this.money -= store.BottlePrice();;
                System.out.println("successfully buy " + name + " for " + store.BottlePrice());
            }
            else {
                System.out.println("failed to buy " + name + " for " + store.BottlePrice());
            }
        }
        else if (type.contains("Equipment")) {
            if (this.money >= store.EquipmentPrice()) {
                equipments.put(id,store.buyEquipment(id,name,type,line));
                this.money -= store.EquipmentPrice();;
                System.out.println("successfully buy " + name + " for " + store.EquipmentPrice());
            }
            else {
                System.out.println("failed to buy " + name + " for " + store.EquipmentPrice());
            }
        }
        else {
            if (this.money >= store.FoodPrice()) {
                foods.put(id, store.buyFood(id, name, type));
                this.money -= store.FoodPrice();
                System.out.println("successfully buy " + name + " for " + store.FoodPrice());
            } else {
                System.out.println("failed to buy " + name + " for " + store.FoodPrice());
            }
        }
    }
    
    public void sellAll() {
        long allPrice = 0;
        Store store = Store.getInstance();
        Iterator<Map.Entry<Integer,Bottle>> iteratorBot = bottles.entrySet().iterator();
        while (iteratorBot.hasNext()) {
            Bottle bottle = iteratorBot.next().getValue();
            if (!bag.haveBottle(bottle)) {
                continue;
            }
            bag.removeBottle(bottle);
            allPrice += bottle.getPrice();
            store.sellBottle(bottle.getMaxCapacity(), bottle.getPrice());
            iteratorBot.remove();
        }
        Iterator<Map.Entry<Integer,Equipment>> iteratorEqui = equipments.entrySet().iterator();
        while (iteratorEqui.hasNext()) {
            Equipment equipment = iteratorEqui.next().getValue();
            if (!bag.haveEquipment(equipment)) {
                continue;
            }
            bag.removeEquipment(equipment);
            allPrice += equipment.getPrice();
            store.sellEquipment(equipment.getStar(), equipment.getPrice());
            iteratorEqui.remove();
        }
        Iterator<Map.Entry<Integer,Food>> iteratorFood = foods.entrySet().iterator();
        while (iteratorFood.hasNext()) {
            Food food = iteratorFood.next().getValue();
            if (!bag.haveFood(food)) {
                continue;
            }
            bag.removeFood(food);
            allPrice += food.getPrice();
            store.sellFood(food.getEnergy(), food.getPrice());
            iteratorFood.remove();
        }
        System.out.println(this.name + " emptied the backpack " + allPrice);
        this.money += allPrice;
    }
    
    public void askHelp(int initHitPoint) {
        if (hitPoint <= (int)(initHitPoint / 2)) {
            for (Adventurer adventurer: adventurers.values()) {
                this.money += adventurer.helpEmployer(initHitPoint - this.hitPoint);
            }
        }
    }
    
    public long helpEmployer(int decrease) {
        long moneyInNeed = (long) (decrease) * 10000;
        long moneyInReal = 0;
        if (this.money >= moneyInNeed) {
            this.money -= moneyInNeed;
            moneyInReal = moneyInNeed;
        }
        else {
            moneyInReal = this.money;
            this.money = 0;
        }
        return moneyInReal;
    }
    
    public long getMoney() {
        return money;
    }
    
    public void employAdventurer(int advId, Adventurer employee) {
        if (!adventurers.containsKey(advId)) {
            adventurers.put(advId, employee);
        }
    }
    
    public long getPrice() {
        long value = money;
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
    
    public int getHitPoint() {
        return this.hitPoint;
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
    
    public void sellBottle(int id) {
        Bottle bottle = bottles.get(id);
        this.money += bottle.getPrice();
        Store store = Store.getInstance();
        store.sellBottle(bottle.getMaxCapacity(),bottle.getPrice());
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
    
    public void sellEquipment(int id) {
        Equipment equipment = equipments.get(id);
        this.money += equipment.getPrice();
        Store store = Store.getInstance();
        store.sellEquipment(equipment.getStar(), equipment.getPrice());
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
    
    public void sellFood(int id) {
        Food food = foods.get(id);
        this.money += food.getPrice();
        Store store = Store.getInstance();
        store.sellFood(food.getEnergy(), food.getPrice());
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
