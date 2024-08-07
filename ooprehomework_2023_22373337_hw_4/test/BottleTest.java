import org.junit.Test;

import static org.junit.Assert.*;

public class BottleTest {
    
    @Test
    public void drinkBottle() {
        Bottle bottle = new Bottle(1,"name",1);
        bottle.drinkBottle();
        assertEquals(bottle.getCapacity(),0);
    }
    
    @Test
    public void getName() {
        Bottle bottle = new Bottle(1,"name",1);
        assertEquals(bottle.getName(),"name");
    }
    
    @Test
    public void getId() {
        Bottle bottle = new Bottle(1,"name",1);
        assertEquals(bottle.getId(),1);
    }
    
    @Test
    public void getCapacity() {
        Bottle bottle = new Bottle(1,"name",1);
        assertEquals(bottle.getCapacity(),1);
    }
}