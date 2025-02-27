package exc;

import com.oocourse.spec3.exceptions.EqualMessageIdException;
import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id;
    
    public MyEqualMessageIdException(int id) {
        this.id = id;
        if (counts.containsKey(id)) {
            int count = counts.get(id);
            counts.put(id, count + 1);
        }
        else {
            counts.put(id, 1);
        }
        ++allCount;
    }
    
    @Override
    public void print() {
        System.out.println("emi-" + allCount + ", " + id + "-" + counts.get(id));
    }
}
