import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputHandler extends Thread {
    private final RequestQueue mainQueue;
    
    public InputHandler(RequestQueue mainQueues) {
        this.mainQueue = mainQueues;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request != null) {
                Person person = new Person(request);
                mainQueue.addRequest(person);
            }
            else {
                mainQueue.setInputEnd(true);
                break;
            }
        }
    }
}
