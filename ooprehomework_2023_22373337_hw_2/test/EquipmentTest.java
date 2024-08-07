import org.junit.Test;

public class EquipmentTest {
    
    @Test
    public void getName() {
        Equipment equipment = new Equipment(22373337,"buaa123",4);
        assert(equipment.getName().equals("buaa123"));
    }
    
    @Test
    public void upStar(){
        Equipment equipment = new Equipment(22333,"tsinghua",5);
        assert(equipment.upStar() == 6);
    }
}