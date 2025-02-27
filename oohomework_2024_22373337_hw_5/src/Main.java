import com.oocourse.elevator1.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueue mainQueue = new RequestQueue();
        // 每个电梯一个单独的请求表
        HashMap<Integer, RequestQueue> subQueues = new HashMap<>();
        
        for (int i = 1; i <= 6; ++i) {
            RequestQueue tempQueue = new RequestQueue();
            subQueues.put(i, tempQueue);
            Elevator elevator = new Elevator(i, tempQueue);
            elevator.start();
        }
        
        Schedule schedule = new Schedule(mainQueue, subQueues);
        schedule.start();
        
        InputHandler inputHandler = new InputHandler(mainQueue);
        inputHandler.start();
    }
}
