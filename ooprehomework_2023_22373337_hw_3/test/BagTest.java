import org.junit.Test;

import static org.junit.Assert.*;

public class BagTest {
    
    @Test
    public void carryEquipment() {
        Bag bag = new Bag();
        Equipment equipment1 = new Equipment(1,"name",1);
        Equipment equipment2 = new Equipment(2,"name",2);
        bag.carryEquipment(equipment1);
        bag.carryEquipment(equipment2);
    }
    
    @Test
    public void carryBottle() {
        Bag bag = new Bag();
        Bottle bottle = new Bottle(1,"name",1);
        bag.carryBottle(bottle,19);
    }
    
    @Test
    public void carryFood() {
        Bag bag = new Bag();
        Food food = new Food(1,"name",1);
        bag.carryFood(food);
    }
    
    @Test
    public void drinkBottle() {
        Bag bag = new Bag();
        Bottle bottle1 = new Bottle(1,"name",0);
        Bottle bottle2 = new Bottle(2,"name",0);
        bag.carryBottle(bottle1,20);
        bag.carryBottle(bottle2,20);
        bag.drinkBottle("name");
    }
    
    @Test
    public void eatFood() {
        Bag bag = new Bag();
        Food food1 = new Food(1,"name",1);
        Food food2 = new Food(2,"name",1);
        bag.carryFood(food1);
        bag.carryFood(food2);
        bag.eatFood("name");
    }
    
    @Test
    public void removeBottle() {
        Bag bag = new Bag();
        Bottle bottle = new Bottle(1,"name",1);
        bag.carryBottle(bottle,12);
        bag.removeBottle(bottle);
    }
    
    @Test
    public void removeEquipment() {
        Bag bag = new Bag();
        Equipment equipment = new Equipment(1,"name",1);
        bag.carryEquipment(equipment);
        bag.removeEquipment(equipment);
    }
    
    @Test
    public void removeFood() {
        Bag bag = new Bag();
        Food food = new Food(1,"name",1);
        bag.carryFood(food);
        bag.removeFood(food);
    }
}