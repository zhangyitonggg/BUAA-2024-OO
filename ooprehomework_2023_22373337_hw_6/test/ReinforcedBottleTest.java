import org.junit.Test;

import static org.junit.Assert.*;

public class ReinforcedBottleTest {
    
    @Test
    public void getAddCap() {
        ReinforcedBottle bottle = new ReinforcedBottle(1,"name",3,100000,0.6);
        bottle.getAddCap();
    }
}