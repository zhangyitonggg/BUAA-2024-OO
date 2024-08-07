import org.junit.Test;

import static org.junit.Assert.*;

public class FoodTest {
    
    @Test
    public void getName() {
        Food food = new Food(1,"name",1);
        assertEquals(food.getName(),"name");
    }
    
    @Test
    public void getId() {
        Food food = new Food(1,"name",1);
        assertEquals(food.getId(),1);
    }
    
    @Test
    public void getEnergy() {
        Food food = new Food(1,"name",1);
        assertEquals(food.getEnergy(),1);
    }
}