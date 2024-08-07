import org.junit.Test;

import java.util.ArrayList;

public class BagTest {
    @Test
    public void test() {
        Bag bag = new Bag();
        Bottle bottle = new Bottle(1,"name",1,1);
        Equipment equipment = new RegularEquipment(2,"name",1,1);
        Food food = new Food(3,"name",1,1);
        bag.haveBottle(bottle);
        bag.haveFood(food);
        bag.haveEquipment(equipment);
        bag.carryEquipment(equipment);
        bag.carryFood(food);
        bag.carryBottle(bottle,100);
        bag.haveBottle(bottle);
        bag.haveFood(food);
        bag.haveEquipment(equipment);
    }
    
}