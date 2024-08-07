import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperatorTest {
    
    @org.junit.Test
    public void basicOp() {
        Operator operator = new Operator();
        
        ArrayList<String> line1 = new ArrayList<>();
        line1.add("1");
        line1.add("1");
        line1.add("name");
        operator.basicOp(line1);
        
        ArrayList<String> line2 = new ArrayList<>();
        line2.add("2");
        line2.add("1");
        line2.add("1");
        line2.add("name");
        line2.add("100");
        line2.add("100");
        line2.add("RegularBottle");
        operator.basicOp(line2);
        
        ArrayList<String> line2_2 = new ArrayList<>();
        line2_2.add("2");
        line2_2.add("1");
        line2_2.add("12");
        line2_2.add("name");
        line2_2.add("100");
        line2_2.add("100");
        line2_2.add("ReinforcedBottle");
        line2_2.add("0.4");
        operator.basicOp(line2_2);
        
        ArrayList<String> line2_3 = new ArrayList<>();
        line2_3.add("2");
        line2_3.add("1");
        line2_3.add("13");
        line2_3.add("name");
        line2_3.add("100");
        line2_3.add("100");
        line2_3.add("RecoverBottle");
        line2_3.add("0.4");
        operator.basicOp(line2_3);
        
        ArrayList<String> line3 = new ArrayList<>();
        line3.add("3");
        line3.add("1");
        line3.add("1");
        operator.basicOp(line3);
        
        operator.basicOp(line2);
        
        
        ArrayList<String> line4 = new ArrayList<>();
        line4.add("4");
        line4.add("1");
        line4.add("1");
        line4.add("name");
        line4.add("1");
        line4.add("100");
        line4.add("RegularEquipment");
        operator.basicOp(line4);
        
        ArrayList<String> line4_2 = new ArrayList<>();
        line4_2.add("4");
        line4_2.add("1");
        line4_2.add("13");
        line4_2.add("name");
        line4_2.add("1");
        line4_2.add("100");
        line4_2.add("CritEquipment");
        line4_2.add("100");
        operator.basicOp(line4_2);
        
        ArrayList<String> line4_3 = new ArrayList<>();
        line4_3.add("4");
        line4_3.add("1");
        line4_3.add("13");
        line4_3.add("name");
        line4_3.add("1");
        line4_3.add("100");
        line4_3.add("EpicEquipment");
        line4_3.add("0.5");
        operator.basicOp(line4_3);
        
        ArrayList<String> line5  = new ArrayList<>();
        line5.add("5");
        line5.add("1");
        line5.add("1");
        operator.basicOp(line5);
        
        operator.basicOp(line4);
        
        ArrayList<String> line6 = new ArrayList<>();
        line6.add("6");
        line6.add("1");
        line6.add("1");
        operator.basicOp(line6);
        
        ArrayList<String> line7 = new ArrayList<>();
        line7.add("7");
        line7.add("1");
        line7.add("1");
        line7.add("name");
        line7.add("1");
        line7.add("123");
        operator.basicOp(line7);
        
        ArrayList<String> line8 = new ArrayList<>();
        line8.add("8");
        line8.add("1");
        line8.add("1");
        operator.basicOp(line8);
        
        operator.basicOp(line7);
        
        ArrayList<String> line9 = new ArrayList<>();
        line9.add("9");
        line9.add("1");
        line9.add("1");
        operator.basicOp(line9);
        
        ArrayList<String> line10 = new ArrayList<>();
        line10.add("10");
        line10.add("1");
        line10.add("1");
        operator.basicOp(line10);
        
        ArrayList<String> line11 = new ArrayList<>();
        line11.add("11");
        line11.add("1");
        line11.add("1");
        operator.basicOp(line11);
        
        ArrayList<String> line12 = new ArrayList<>();
        line12.add("12");
        line12.add("1");
        line12.add("name");
        operator.basicOp(line12);
        
        ArrayList<String> line13 = new ArrayList<>();
        line13.add("13");
        line13.add("1");
        line13.add("name");
        operator.basicOp(line13);
    }
    
    @org.junit.Test
    public void battleOp_and_logOp() {
        Operator operator = new Operator();
       
        ArrayList<String> line1 = new ArrayList<>();
        line1.add("1");
        line1.add("1");
        line1.add("advname1");
        ArrayList<String> line2 = new ArrayList<>();
        line2.add("1");
        line2.add("2");
        line2.add("advname2");
        operator.basicOp(line1);
        operator.basicOp(line2);
        ArrayList<String> line3 = new ArrayList<>();
        line3.add("2");
        line3.add("1");
        line3.add("1");
        line3.add("botname");
        line3.add("100");
        line3.add("1000");
        line3.add("RegularBottle");
        operator.basicOp(line3);
        ArrayList<String> line4 = new ArrayList<>();
        line4.add("10");
        line4.add("1");
        line4.add("1");
        operator.basicOp(line4);
        ArrayList<String> line5 = new ArrayList<>();
        line5.add("4");
        line5.add("1");
        line5.add("1");
        line5.add("equipname");
        line5.add("5");
        line5.add("1000");
        line5.add("RegularEquipment");
        operator.basicOp(line5);
        ArrayList<String> line6 = new ArrayList<>();
        line6.add("9");
        line6.add("1");
        line6.add("1");
        operator.basicOp(line6);
        
        
        ArrayList<ArrayList<String>> fightBlock = new ArrayList<>();
        
        ArrayList<String> line = new ArrayList<>();
        line.add("14");
        line.add("2");
        line.add("9");
        line.add("advname1");
        line.add("advname2");
        fightBlock.add(line);
        
        ArrayList<String> log1_1 = new ArrayList<>();
        log1_1.add("2023/10-advname1-botname");
        fightBlock.add(log1_1);
        fightBlock.add(log1_1);
        ArrayList<String> log1_2 = new ArrayList<>();
        log1_2.add("2023/10-advname1-botname_");
        fightBlock.add(log1_2);
        
        ArrayList<String> log2_1 = new ArrayList<>();
        log2_1.add("2023/11-advname1@advname2-equipname");
        fightBlock.add(log2_1);
        fightBlock.add(log2_1);
        ArrayList<String> log2_2 = new ArrayList<>();
        log2_2.add("2023/11-advname1@advname2-equipname_");
        fightBlock.add(log2_2);
        
        ArrayList<String> log3_1 = new ArrayList<>();
        log3_1.add("2023/12-advname1@#-equipname");
        fightBlock.add(log3_1);
        fightBlock.add(log3_1);
        ArrayList<String> log3_2 = new ArrayList<>();
        log3_2.add("2023/12-advname1@#-equipname_");
        fightBlock.add(log3_2);
        
        operator.battleOp(fightBlock);
        
        
        
        ArrayList<String> line15_1 = new ArrayList<>();
        line15_1.add("15");
        line15_1.add("2023/10");
        operator.logOp(line15_1);
        ArrayList<String> line15_2 = new ArrayList<>();
        line15_2.add("15");
        line15_2.add("2023/09");
        operator.logOp(line15_2);
        
        ArrayList<String> line16_1 = new ArrayList<>();
        line16_1.add("16");
        line16_1.add("1");
        operator.logOp(line16_1);
        ArrayList<String> line16_2 = new ArrayList<>();
        line16_2.add("16");
        line16_2.add("2");
        operator.logOp(line16_2);
        
        ArrayList<String> line17_1 = new ArrayList<>();
        line17_1.add("17");
        line17_1.add("2");
        operator.logOp(line17_1);
        ArrayList<String> line17_2 = new ArrayList<>();
        line17_2.add("17");
        line17_2.add("1");
        operator.logOp(line17_2);
    }
    
  
}