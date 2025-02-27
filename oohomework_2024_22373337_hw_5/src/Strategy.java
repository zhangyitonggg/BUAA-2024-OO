import java.util.ArrayList;

public class Strategy {
    private final RequestQueue requestQueue;
    private final ArrayList<Person> persons;

    public enum ComType {
        MOVE, WAIT, OPEN, REVERSE, END
    }
    
    public Strategy(RequestQueue requestQueue, ArrayList<Person> persons) {
        this.requestQueue = requestQueue;
        this.persons = persons;
    }
    
    public Com getCommand(int curPeoNum, int curFloor, int direction) {
        // 判断是否要开门
        if (canOutElevator(curPeoNum, curFloor) || canInElevator(curPeoNum, curFloor, direction)) {
            return Com.OPEN;
        }
        // 无人出且无人进 或  无人出且电梯满
        if (curPeoNum != 0) { // 电梯内有人
            return Com.MOVE;
        }
        // 电梯内有人
        if (requestQueue.getNum() != 0) {
            if (hasReqInSameDir(curFloor, direction)) {
                return Com.MOVE; // 有人在前方
            }
            else {
                return Com.REVERSE;
            }
        }
        else {
            if (requestQueue.isInputEnd()) {
                return Com.END;
            }
            else {
                return Com.WAIT;
            }
        }
        
    }
    
    private boolean canOutElevator(int curPeoNum, int curFloor) {
        if (curPeoNum == 0) {
            return false;
        }
        for (Person person : persons) {
            if (person.getToFloor() == curFloor) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canInElevator(int curPeoNum, int curFloor, int direction) {
        if (curPeoNum == 6) {
            return false;
        }
        synchronized (requestQueue) {
            ArrayList<Person> requestPersons = requestQueue.getPersons();
            for (Person person : requestPersons) {
                if (person.getFromFloor() == curFloor && person.getDirection() == direction) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private boolean hasReqInSameDir(int curFloor, int direction) {
        synchronized (requestQueue) {
            ArrayList<Person> requestPersons = requestQueue.getPersons();
            for (Person person : requestPersons) {
                if ((person.getFromFloor() - curFloor) * direction > 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
