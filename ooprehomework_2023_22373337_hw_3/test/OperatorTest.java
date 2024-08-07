import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperatorTest {
    
    @org.junit.Test
    public void op() {
        Operator operator = new Operator();
        
        ArrayList<String> line1 = new ArrayList<>();
        line1.add("1");
        line1.add("1");
        line1.add("name");
        operator.op(line1);
        
        ArrayList<String> line2 = new ArrayList<>();
        line2.add("2");
        line2.add("1");
        line2.add("1");
        line2.add("name");
        line2.add("100");
        operator.op(line2);
        
        ArrayList<String> line3 = new ArrayList<>();
        line3.add("3");
        line3.add("1");
        line3.add("1");
        operator.op(line3);
        
        operator.op(line2);
        
        
        ArrayList<String> line4 = new ArrayList<>();
        line4.add("4");
        line4.add("1");
        line4.add("1");
        line4.add("name");
        line4.add("1");
        operator.op(line4);
        
        ArrayList<String> line5  = new ArrayList<>();
        line5.add("5");
        line5.add("1");
        line5.add("1");
        operator.op(line5);
        
        operator.op(line4);
        
        ArrayList<String> line6 = new ArrayList<>();
        line6.add("6");
        line6.add("1");
        line6.add("1");
        operator.op(line6);
        
        ArrayList<String> line7 = new ArrayList<>();
        line7.add("7");
        line7.add("1");
        line7.add("1");
        line7.add("name");
        line7.add("1");
        operator.op(line7);
        
        ArrayList<String> line8 = new ArrayList<>();
        line8.add("8");
        line8.add("1");
        line8.add("1");
        operator.op(line8);
        
        operator.op(line7);
        
        ArrayList<String> line9 = new ArrayList<>();
        line9.add("9");
        line9.add("1");
        line9.add("1");
        operator.op(line9);
        
        ArrayList<String> line10 = new ArrayList<>();
        line10.add("10");
        line10.add("1");
        line10.add("1");
        operator.op(line10);
        
        ArrayList<String> line11 = new ArrayList<>();
        line11.add("11");
        line11.add("1");
        line11.add("1");
        operator.op(line11);
        
        ArrayList<String> line12 = new ArrayList<>();
        line12.add("12");
        line12.add("1");
        line12.add("name");
        operator.op(line12);
        
        ArrayList<String> line13 = new ArrayList<>();
        line13.add("13");
        line13.add("1");
        line13.add("name");
        operator.op(line13);
    }
    
    
    
}