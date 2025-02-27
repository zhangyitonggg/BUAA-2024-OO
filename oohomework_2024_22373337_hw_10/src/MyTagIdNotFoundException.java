import com.oocourse.spec2.exceptions.TagIdNotFoundException;

import java.util.HashMap;

public class MyTagIdNotFoundException extends TagIdNotFoundException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id;
    
    public MyTagIdNotFoundException(int id) {
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
        System.out.println("tinf-" + allCount + ", " + id + "-" + counts.get(id));
    }
}
