import java.util.HashMap;

public class Schedule extends Thread {
    private final RequestQueue mainQueue;
    private final HashMap<Integer, RequestQueue> subQueues;
    
    public Schedule(RequestQueue mainQueue, HashMap<Integer, RequestQueue> subQueues) {
        this.mainQueue = mainQueue;
        this.subQueues = subQueues;
    }
    
    @Override
    public void run() {
        while (true) {
            if (mainQueue.getNum() == 0 && mainQueue.isInputEnd()) {
                for (RequestQueue subQueue : subQueues.values()) {
                    subQueue.setInputEnd(true);
                }
                return;
            }
            Person person = mainQueue.transferFromMainToSub();
            if (person == null) {
                continue;
            }
            subQueues.get(person.getElevatorId()).addRequest(person);
        }
    }
}
