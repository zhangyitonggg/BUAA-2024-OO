package exc;

import com.oocourse.spec3.exceptions.EqualRelationException;
import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id1;
    private final int id2;
    
    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        addCount();
    }
    
    private void addCount() {
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
        int min = Math.min(id1, id2);
        int max = Math.max(id1, id2);
        System.out.println("er-" + allCount + ", " + min + "-" +
                counts.get(min) + ", " + max + "-" + counts.get(max));
    }
}
