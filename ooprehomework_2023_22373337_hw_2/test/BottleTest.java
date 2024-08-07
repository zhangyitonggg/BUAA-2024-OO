import org.junit.Test;
public class BottleTest {
    
    @Test
    public void getName() {
        Bottle bottle = new Bottle(101,"zhangyitong",100);
        String name = bottle.getName();
        assert (name.equals("zhangyitong"));
    }
}