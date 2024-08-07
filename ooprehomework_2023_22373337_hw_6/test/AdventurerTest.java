import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdventurerTest {
    
    @Test
    public void newnew() {
        Adventurer adv1 = new Adventurer(1, "name1");
        Adventurer adv2 = new Adventurer(2, "name2");
        adv1.employAdventurer(2, adv2);
        adv1.employAdventurer(2, adv2);
        
        ArrayList<String> line3_1 = new ArrayList<>();
        line3_1.add("0");
        line3_1.add("0");
        line3_1.add("31");
        line3_1.add("name3");
        line3_1.add("100");
        line3_1.add("100");
        line3_1.add("RegularBottle");
        adv1.addBottle(line3_1);
        
        ArrayList<String> line3_2 = new ArrayList<>();
        line3_2.add("0");
        line3_2.add("0");
        line3_2.add("32");
        line3_2.add("name3");
        line3_2.add("100");
        line3_2.add("100");
        line3_2.add("ReinforcedBottle");
        line3_2.add("0.6");
        adv1.addBottle(line3_2);
        
        ArrayList<String> line3_3 = new ArrayList<>();
        line3_3.add("0");
        line3_3.add("0");
        line3_3.add("33");
        line3_3.add("name3");
        line3_3.add("100");
        line3_3.add("100");
        line3_3.add("RecoverBottle");
        line3_3.add("0.6");
        adv1.addBottle(line3_3);
        
        adv1.addFood(4, "name4", 100, 101);
        
        ArrayList<String> line5_1 = new ArrayList<>();
        line5_1.add("0");
        line5_1.add("0");
        line5_1.add("51");
        line5_1.add("name5");
        line5_1.add("100");
        line5_1.add("102");
        line5_1.add("RegularEquipment");
        adv1.addEquipment(line5_1);
        
        ArrayList<String> line5_2 = new ArrayList<>();
        line5_2.add("0");
        line5_2.add("0");
        line5_2.add("52");
        line5_2.add("name5");
        line5_2.add("100");
        line5_2.add("102");
        line5_2.add("CritEquipment");
        line5_2.add("100");
        adv1.addEquipment(line5_2);
        
        ArrayList<String> line5_3 = new ArrayList<>();
        line5_3.add("0");
        line5_3.add("0");
        line5_3.add("53");
        line5_3.add("name5");
        line5_3.add("100");
        line5_3.add("102");
        line5_3.add("EpicEquipment");
        line5_3.add("0.2");
        adv1.addEquipment(line5_3);
        
        adv1.getPrice();
        adv1.getComCount();
        adv1.getComMax();
        adv1.findClassType(2);
        adv1.findClassType(4);
        adv1.findClassType(31);
        adv1.findClassType(32);
        adv1.findClassType(33);
        adv1.findClassType(51);
        adv1.findClassType(52);
        adv1.findClassType(53);
    }
}