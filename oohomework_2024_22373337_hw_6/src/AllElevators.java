import com.oocourse.elevator2.ResetRequest;
import java.util.HashMap;

public class AllElevators {
    private static AllElevators allElevators;
    private final HashMap<String, Elevator> elevators;
    private final HashMap<String, SubQueue> subQueues;
    
    private AllElevators() {
        elevators = new HashMap<>();
        subQueues = new HashMap<>();
    }
    
    // 单例模式
    public static synchronized AllElevators getInstance() {
        if (allElevators == null) {
            allElevators = new AllElevators();
        }
        return allElevators;
    }
    
    // 放入一个新的电梯及其请求表,初始化以及第二类reset时会调用
    public synchronized void put(String id, Elevator elevator, SubQueue subQueue) {
        elevators.put(id,elevator);
        subQueues.put(id, subQueue);
        notifyAll();
    }
   
    // 输入线程调用，无需同步控制
    public void acceptReset(String id, ResetRequest resetRequest) {
        subQueues.get(id).acceptReset(resetRequest);
    }
   
    // 调度线程调用
    public synchronized void setEnd() {
        for (SubQueue subQueue : subQueues.values()) {
            subQueue.setEnd(true);
        }
    }
    
    // 第二类reset调用，以移除原有电梯
    public synchronized void remove(String id) {
        elevators.remove(id);
        subQueues.remove(id);
    }
    
    // 分发请求
    public synchronized void dispatch(Person person) {
        String maxIndex = "0";
        double maxValue = -1 * Double.MAX_VALUE;
        while (elevators.isEmpty()) {
            try {
                wait(); // 只有put会唤醒它，而wait之后必然会有put
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (String iter : elevators.keySet()) {
            double temp = calc(person, iter);
            if (temp >= maxValue) {
                maxValue = temp;
                maxIndex = iter;
            }
        }
        subQueues.get(maxIndex).addRequest(person);
    }
    
    private synchronized double calc(Person person, String iter) {
        int perFromFloor = person.getFromFloor();
        int perToFloor = person.getToFloor();
        int eleCurFloor = elevators.get(iter).getCurFloor();
        int capacity = elevators.get(iter).getCapacity();
        int direction = elevators.get(iter).getDirection();
        int curPeoNum = elevators.get(iter).getCurPeoNum();
        int reqPeoNum = subQueues.get(iter).getReqPeoNum();
        int speed = elevators.get(iter).getSpeed();
        int transferFloor = elevators.get(iter).getTransferFloor();
        boolean isA = elevators.get(iter).getIsA();
        int distance = getDistance(eleCurFloor, direction, person, iter);
        
        int dirIsSame;
        if (person.getDirection() == direction) {
            dirIsSame = 1;
        }
        else {
            dirIsSame = -1;
        }
        
        double result = -1 * distance + 2 * capacity + 1 * dirIsSame - 2.5 * curPeoNum
                - 2.5 * reqPeoNum - (double)speed / 100;
        // 接不到的返回最小值
        if (isA && perFromFloor > transferFloor ||
                !isA && perFromFloor < transferFloor) {
            return -1 * Double.MAX_VALUE;
        }
        // 可以接到但是根本没法向目标层方向运的返回最小值+1
        else if (isA && perFromFloor == transferFloor && person.getDirection() > 0 ||
                !isA && perFromFloor == transferFloor && person.getDirection() < 0) {
            return -1 * Double.MAX_VALUE + 1;
        }
        // 送不到的加一个passive value
        if (isA && transferFloor < perToFloor ||
                !isA && transferFloor > perToFloor) {
            result += -3;
        }
        return result;
    }
    
    private synchronized int getDistance(int eleCurFloor, int direction,
                                         Person person, String  iter) {
        if (person.getFromFloor() == eleCurFloor) {
            return 0;
        }
        
        if ((person.getFromFloor() - eleCurFloor) * direction > 0) {
            return Math.abs(person.getFromFloor() - eleCurFloor);
        }
        
        int farthestFloorInEle = elevators.get(iter).getFarthestFloorInEle();
        int farthestFloorInQue = subQueues.get(iter).getFarthestFloorInQue(eleCurFloor, direction);
        int tempFloor;
        if ((farthestFloorInEle - farthestFloorInQue) * direction > 0) {
            tempFloor = farthestFloorInEle;
        }
        else {
            tempFloor = farthestFloorInQue;
        }
        return Math.abs(tempFloor - person.getFromFloor()) +
                Math.abs(tempFloor - person.getToFloor());
    }
}
