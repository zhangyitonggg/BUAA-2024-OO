import com.oocourse.elevator2.TimableOutput;
import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private final String id;
    private final MainQueue mainQueue;
    private final AllElevators allElevators;
    private final SubQueue requestQueue;
    private int capacity;
    private int speed;
    private int curFloor;
    private int curPeoNum;
    private final ArrayList<Person> persons;
    private int direction;
    private final Strategy strategy;
    // 默认为A电梯
    private final int transferFloor;
    private final boolean isA;
    private final Coordinate coordinate;
    
    public Elevator(String id, SubQueue requestQueue, int curFloor, int direction, int capacity,
                    int speed, int transferFloor, boolean isA, Coordinate coordinate) {
        this.id = id;
        this.mainQueue = MainQueue.getInstance();
        this.allElevators = AllElevators.getInstance();
        this.requestQueue = requestQueue;
        this.capacity = capacity;
        this.speed = speed;
        this.curFloor = curFloor;
        this.curPeoNum = 0; // 初始化的电梯人数必然为0
        this.persons = new ArrayList<>();
        this.direction = direction;
        this.strategy = new LookStrategy(requestQueue, persons);
        this.transferFloor = transferFloor;
        this.isA = isA;
        this.coordinate = coordinate;
    }
    
    public synchronized int getCapacity() {
        return this.capacity;
    }
    
    public synchronized int getSpeed() {
        return this.speed;
    }
    
    public synchronized int getCurFloor() {
        return this.curFloor;
    }
    
    public synchronized int getCurPeoNum() {
        return this.curPeoNum;
    }
    
    public synchronized int getDirection() {
        return this.direction;
    }
    
    public synchronized int getTransferFloor() {
        return this.transferFloor;
    }
    
    public synchronized boolean getIsA() {
        return this.isA;
    }
    
    public synchronized int getFarthestFloorInEle() {
        int desFloor = curFloor;
        for (Person person : persons) {
            if ((person.getToFloor() - desFloor) * direction > 0) {
                desFloor = person.getToFloor();
            }
        }
        return desFloor;
    }
    
    public void run() {
        while (true) {
            int com = strategy.getCommand(curPeoNum, capacity, curFloor, direction,
                                            isA, transferFloor);
            
            if (com == Constants.RESET) {
                // 如果是第二类reset，则返回1，否则返回0，如果是1的话，因为新开了两个线程，结束本线程即可
                int flag = reset();
                if (flag == 1) {
                    break;
                }
            }
            else if (com == Constants.OPEN) {
                openThenClose();
            }
            else if (com == Constants.MOVE) {
                move();
            }
            else if (com == Constants.WAIT) {
                // 对于普通电梯，transferFloor很大，所以不会触发这个if条件
                // 对于双轿厢电梯，如果等于transferFloor，并且是wait，也就是开关门之后，立刻离开；而此版代码六个电梯会同时END，所以无需考虑
                if (curFloor == transferFloor) {
                    if (isA) {
                        synchronized (this) {
                            direction = -1;
                        }
                        move();
                    }
                    else {
                        synchronized (this) {
                            direction = 1;
                        }
                        move();
                    }
                    continue;
                }
                requestQueue.setWait();
            }
            else if (com == Constants.REVERSE) {
                reverse();
            }
            else if (com == Constants.END) {
                break;
            }
        }
    }
    
    public synchronized void reverse() {
        direction *= -1;
    }
    
    public int reset() {
        if (curPeoNum != 0) {
            TimableOutput.println("OPEN-" + curFloor + "-" + id);
            transFromEleToMain();
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimableOutput.println("CLOSE-" + curFloor + "-" + id);
        }
        TimableOutput.println("RESET_BEGIN-" + id);
        requestQueue.transFromSubToMain();
        try {
            sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            capacity = requestQueue.getNextCap();
            speed = (int) (requestQueue.getNextSpeed() * 1000);
        }
        TimableOutput.println("RESET_END-" + id);
        if (requestQueue.resetIsNormal()) {
            requestQueue.deleteResetRequest();
            requestQueue.transFromBufferToSub();
            mainQueue.subResetCount();
            return 0;
        }
        else {
            Coordinate coordinate = new Coordinate();
            int transferFloor = requestQueue.getTransferFloor();
            SubQueue subQueueA = new SubQueue(id + "-A");
            // direction设置为向下
            Elevator elevatorA = new Elevator(id + "-A", subQueueA, transferFloor - 1, -1, capacity,
                                              speed, transferFloor, true, coordinate);
            elevatorA.start();
            SubQueue subQueueB = new SubQueue(id + "-B");
            // direction设置为向上
            Elevator elevatorB = new Elevator(id + "-B", subQueueB, transferFloor + 1, 1, capacity,
                                              speed, transferFloor, false, coordinate);
            elevatorB.start();
            synchronized (allElevators) {
                allElevators.remove(id);
                allElevators.put(id + "-A", elevatorA, subQueueA);
                allElevators.put(id + "-B", elevatorB, subQueueB);
                requestQueue.transFromBufferToMain();
                mainQueue.subResetCount();
                return 1;
            }
        }
    }
    
    private int openThenClose() {
        TimableOutput.println("OPEN-" + curFloor + "-" + id);
        outElevator();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int com = strategy.getCommand(curPeoNum, capacity, curFloor, direction, isA, transferFloor);
        if (com == Constants.REVERSE) {
            reverse(); // 防止同一层开两次门
        }
        if (com == Constants.RESET) {
            transFromEleToMain();
            TimableOutput.println("CLOSE-" + curFloor + "-" + id);
            return 1; // 如果是reset，则返回1，在move中调用此方法时汇需要用到
        }
        inElevator();
        TimableOutput.println("CLOSE-" + curFloor + "-" + id);
        return 0;
    }
    
    private void move() {
        try {
            sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int com = strategy.getCommand(curPeoNum, capacity, curFloor, direction, isA, transferFloor);
        if (com == Constants.OPEN) {
            int flag = openThenClose();
            if (flag == 1) {
                return; // 如果此时需要reset，立刻返回
            }
            // 如果不需要reset，继续move
            try {
                sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (this) {
            curFloor += direction;
        }
        if (curFloor == transferFloor) {
            coordinate.robTransferFloor();
            try {
                sleep(5); // 睡上5ms吧，虽然不睡也没错
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
        if (curFloor == transferFloor + direction) {
            coordinate.releaseTransferFloor();
        }
    }
    
    private synchronized void transFromEleToMain() {
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            Person newPerson = new Person(person.getPersonId(), curFloor, person.getToFloor());
            TimableOutput.println("OUT-" + person.getPersonId() + "-" + curFloor + "-" + id);
            if (newPerson.getToFloor() != curFloor) {
                mainQueue.addRequest(newPerson);
            }
            else {
                mainQueue.subNumNotFinished();
            }
            --curPeoNum;
            iterator.remove();
        }
    }
    
    private synchronized void outElevator() {
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getToFloor() == curFloor) {
                TimableOutput.println("OUT-" + person.getPersonId() + "-" + curFloor + "-" + id);
                mainQueue.subNumNotFinished();
                --curPeoNum;
                iterator.remove();
            }
            else if (isA && curFloor == transferFloor && person.getToFloor() > transferFloor ||
                     !isA && curFloor == transferFloor && person.getToFloor() < transferFloor) {
                TimableOutput.println("OUT-" + person.getPersonId() + "-" + curFloor + "-" + id);
                iterator.remove();
                Person newPerson = new Person(person.getPersonId(), curFloor, person.getToFloor());
                mainQueue.addRequest(newPerson);
                --curPeoNum;
            }
        }
    }
    
    private synchronized void inElevator() {
        while (curPeoNum < capacity) {
            Person person = requestQueue.getOneRequestAndRemove(curFloor, direction);
            if (person == null) {
                return;
            }
            TimableOutput.println("IN-" + person.getPersonId() + "-" + curFloor + "-" + id);
            persons.add(person);
            ++curPeoNum;
        }
    }
}
