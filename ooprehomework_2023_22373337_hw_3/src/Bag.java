import java.util.HashSet;

public class Bag {
    private HashSet<Equipment> equipments = new HashSet<>();
    private HashSet<Bottle> bottles = new HashSet<>();
    private HashSet<Food> foods = new HashSet<>();
    
    public void carryEquipment(Equipment equipment) {
        int flag = 0;
        for (Equipment equI:equipments) {
            if (equI.getName().equals(equipment.getName())) {
                flag = 1;
                equipments.remove(equI);
                equipments.add(equipment);
                break;
            }
        }
        if (flag == 0) {
            equipments.add(equipment);
        }
    }
    
    public void carryBottle(Bottle bottle,int level) {
        int maxBottle = level / 5 + 1;
        int num = 0;
        for (Bottle botI:bottles) {
            if (botI.getName().equals(bottle.getName())) {
                num++;
            }
        }
        if (num < maxBottle) {
            bottles.add(bottle);
        }
    }
    
    public void carryFood(Food food) {
        foods.add(food);
    }
    
    public Bottle drinkBottle(String name) {
        Bottle bottle = null;
        for (Bottle botI : bottles) {
            if (botI.getName().equals(name)) {
                if (bottle == null) {
                    bottle = botI;
                }
                else if (botI.getId() < bottle.getId()) {
                    bottle = botI;
                }
            }
        }
        if (bottle != null && bottle.getCapacity() == 0) {
            bottles.remove(bottle);
        }
        return bottle;
    }
    
    public Food eatFood(String name) {
        Food food = null;
        for (Food foodI : foods) {
            if (foodI.getName().equals(name)) {
                if (food == null) {
                    food = foodI;
                }
                else if (foodI.getId() < food.getId()) {
                    food = foodI;
                }
            }
        }
        if (food != null) {
            foods.remove(food);
        }
        return food;
    }
    
    public void removeBottle(Bottle bottle) {
        bottles.remove(bottle);
    }
    
    public void removeEquipment(Equipment equipment) {
        equipments.remove(equipment);
    }
    
    public void removeFood(Food food) {
        foods.remove(food);
    }
}
