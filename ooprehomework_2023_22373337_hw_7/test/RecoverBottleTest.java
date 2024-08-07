import org.junit.Test;

import static org.junit.Assert.*;

public class RecoverBottleTest {
    
    @Test
    public void getRatio() {
        RecoverBottle recoverBottle = new RecoverBottle(1,"name",100,2000,0.1);
        recoverBottle.getRatio();
    }
}