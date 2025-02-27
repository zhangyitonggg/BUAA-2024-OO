import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int allCount = 0;
    private static HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id1;
    private final int id2;
    
    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        addCount();
    }
    
    private void addCount() {
        // 不等概率大
        if (id1 != id2) {
            addCountOfOnePerson(id1);
            addCountOfOnePerson(id2);
        }
        else {
            addCountOfOnePerson(id1);
        }
        ++allCount;
    }
    
    private void addCountOfOnePerson(int id) {
        if (counts.containsKey(id)) {
            int count = counts.get(id);
            counts.put(id, count + 1);
        }
        else {
            counts.put(id, 1);
        }
    }
    
    @Override
    public void print() {
        if (id1 < id2) {
            System.out.println("er-" + allCount + ", " + id1 + "-" +
                    counts.get(id1) + ", " + id2 + "-" + counts.get(id2));
        }
        else {
            System.out.println("er-" + allCount + ", " + id2 + "-" +
                    counts.get(id2) + ", " + id1 + "-" + counts.get(id1));
            
        }
    }
}
