package exc;

import com.oocourse.spec3.exceptions.RelationNotFoundException;
import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id1;
    private final int id2;
    
    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        addCount();
    }
    
    private void addCount() {
        // 没必要判断是否相等，若相等必然提前抛出异常
        addCountOfOnePerson(id1);
        addCountOfOnePerson(id2);
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
        System.out.println("rnf-" + allCount + ", " + min + "-" +
                counts.get(min) + ", " + max + "-" + counts.get(max));
    }
}
