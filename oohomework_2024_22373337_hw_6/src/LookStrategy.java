import java.util.ArrayList;

public class LookStrategy implements Strategy {
    private final SubQueue requestQueue;
    private final ArrayList<Person> persons;
    
    public LookStrategy(SubQueue requestQueue, ArrayList<Person> persons) {
        this.requestQueue = requestQueue;
        this.persons = persons;
    }
    
    @Override
    public int getCommand(int curPeoNum, int capacity,int curFloor, int direction,
                          boolean isA, int transferFloor) {
        synchronized (requestQueue) {
            if (requestQueue.isResetAble()) {
                return Constants.RESET;
            }
            // 判断是否要开门
            if (canOutElevator(curPeoNum, curFloor, isA, transferFloor) ||
                    requestQueue.canInElevator(curPeoNum, capacity, curFloor, direction)) {
                return Constants.OPEN;
            }
            // 无人出且无人进 或 无人出且电梯满
            if (curPeoNum != 0) { // 电梯内有人
                // 不会出现走过transferFloor的情况，因为会在transferFloor全部走出去
                return Constants.MOVE;
            }
            // 电梯内有人
            if (!requestQueue.isEmpty()) {
                if (requestQueue.hasReqInSameDir(curFloor, direction)) {
                    return Constants.MOVE; // 有人在前方
                }
                else {
                    return Constants.REVERSE;
                }
            }
            else {
                if (requestQueue.isEnd()) {
                    return Constants.END;
                }
                else {
                    return Constants.WAIT;
                }
            }
        }
    }
    
    private boolean canOutElevator(int curPeoNum, int curFloor, boolean isA, int transferFloor) {
        if (curPeoNum == 0) {
            return false;
        }
        for (Person person : persons) {
            if (person.getToFloor() == curFloor) {
                return true;
            }
        }
        if (curFloor == transferFloor) {
            if (isA) {
                for (Person person : persons) {
                    if (person.getToFloor() > curFloor) {
                        return true;
                    }
                }
            }
            else {
                for (Person person : persons) {
                    if (person.getToFloor() < curFloor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
