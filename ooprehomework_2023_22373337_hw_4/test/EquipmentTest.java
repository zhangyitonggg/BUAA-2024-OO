import org.junit.Test;

import static org.junit.Assert.*;

public class EquipmentTest {
    
    @Test
    public void upStar() {
        Equipment equipment = new Equipment(1,"name",1);
        equipment.upStar();
    }
    
    @Test
    public void getName() {
        Equipment equipment = new Equipment(1,"name",1);
        assertEquals(equipment.getName(),"name");
    }
    
    @Test
    public void getId() {
        Equipment equipment = new Equipment(1,"name",1);
        assertEquals(equipment.getId(),1);
    }
    
    public void getStar() {
        Equipment equipment = new Equipment(1,"name",1);
        assertEquals(equipment.getStar(),1);
    }
}