import com.oocourse.elevator3.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        MainQueue mainQueue = MainQueue.getInstance();
        AllElevators allElevators = AllElevators.getInstance();
        
        for (int i = 1; i <= 6; ++i) {
            SubQueue tempQueue = new SubQueue(String.valueOf(i));
            // 默认是A轿厢，将transferFloor设置为114514
            Elevator elevator = new Elevator(String.valueOf(i), tempQueue, 1, 1, 6, 400,
                                114514, true, new Coordinate());
            elevator.start();
            allElevators.put(String.valueOf(i), elevator, tempQueue);
        }
        
        Schedule schedule = new Schedule();
        schedule.start();
        
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
    }
}
