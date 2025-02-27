package exc;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int allCount = 0;
    private static final HashMap<Integer, Integer> counts = new HashMap<>();
    
    private final int id;
    
    public MyEmojiIdNotFoundException(int id) {
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
        System.out.println("einf-" + allCount + ", " + id + "-" + counts.get(id));
    }
}
