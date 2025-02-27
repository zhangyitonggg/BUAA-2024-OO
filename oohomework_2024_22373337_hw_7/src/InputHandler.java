import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class InputHandler extends Thread {
    private final MainQueue mainQueue;
    private final AllElevators allElevators;
    
    public InputHandler() {
        this.mainQueue = MainQueue.getInstance();
        this.allElevators = AllElevators.getInstance();
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            // 如果输入未结束
            if (request != null) {
                if (request instanceof PersonRequest) {
                    PersonRequest req = (PersonRequest) request;
                    Person person = new Person(req);
                    // 增加未完成请求数量，用于作为结束标志之一
                    mainQueue.addNumNotFinished();
                    mainQueue.addRequest(person);
                }
                else if (request instanceof NormalResetRequest) {
                    mainQueue.addResetCount();
                    NormalResetRequest req = (NormalResetRequest) request;
                    allElevators.acceptReset(String.valueOf(req.getElevatorId()), req);
                }
                else if (request instanceof DoubleCarResetRequest) {
                    mainQueue.addResetCount();
                    DoubleCarResetRequest req = (DoubleCarResetRequest) request;
                    allElevators.acceptReset(String.valueOf(req.getElevatorId()), req);
                }
            }
            // 输入结束
            else {
                mainQueue.setInputEnd(true);
                break;
            }
        }
    }
}
