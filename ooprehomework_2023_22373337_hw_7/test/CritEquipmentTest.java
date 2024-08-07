import org.junit.Test;

import static org.junit.Assert.*;

public class CritEquipmentTest {
    
    @Test
    public void getCritical() {
        CritEquipment critEquipment = new CritEquipment(1,"name",100,2000,1);
        critEquipment.getCritical();
    }
    
}