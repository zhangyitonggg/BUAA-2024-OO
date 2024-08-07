import org.junit.Test;

import static org.junit.Assert.*;

public class EpicEquipmentTest {
    
    @Test
    public void getRatio() {
        EpicEquipment epicEquipment = new EpicEquipment(1,"name",100,2000,0.1);
        epicEquipment.getRatio();
    }
}