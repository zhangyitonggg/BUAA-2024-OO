import org.junit.Test;

import java.util.ArrayList;

public class StoreTest {
    @Test
    public void test() {
        Store store = Store.getInstance();
        store.sellBottle(100,200);
        store.sellEquipment(100,200);
        store.sellFood(100,100);
        store.FoodPrice();
        store.EquipmentPrice();
        store.BottlePrice();
        ArrayList<String> line = new ArrayList<>();
        line.add("0");
        line.add("1");
        line.add("2");
        line.add("3");
        line.add("4");
        line.add(5,"0.2");
        store.buyBottle(1,"name","RegularBottle",line);
        store.buyBottle(1,"name","ReinforcedBottle",line);
        store.buyBottle(1,"name","sssReinforcedBottle",line);
        store.buyEquipment(1,"name","RegularEquipment",line);
        store.buyEquipment(1,"name","RssssegularEquipment",line);
        line.remove(5);
        line.add(5,"123");
        store.buyEquipment(1,"name","CritEquipment",line);
        store.buyFood(1,"name","food");
    }
}