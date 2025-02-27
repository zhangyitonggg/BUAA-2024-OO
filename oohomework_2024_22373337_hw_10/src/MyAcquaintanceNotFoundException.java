import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id;
    
    public MyAcquaintanceNotFoundException(int id) {
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
        System.out.println("anf-" + allCount + ", " + id + "-" + counts.get(id));
    }
}
