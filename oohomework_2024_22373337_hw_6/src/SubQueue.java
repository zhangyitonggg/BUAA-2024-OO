import com.oocourse.elevator2.ResetRequest;
import com.oocourse.elevator2.ResetRequest;
import com.oocourse.elevator2.TimableOutput;
import java.util.ArrayList;
import java.util.Iterator;

public class SubQueue {
    private final String id;
    private boolean isEnd;
    private final ArrayList<Person> persons;
    private final ArrayList<Person> buffer;
    private ResetRequest resetRequest;
    
    public SubQueue(String id) {
        this.id = id;
        isEnd = false;
        persons = new ArrayList<>();
        buffer = new ArrayList<>();
        resetRequest = null;
    }
    
    // 如果此时在reset，加入buffer，否则加入persons
    public synchronized void addRequest(Person person) {
        if (resetRequest == null) {
            TimableOutput.println("RECEIVE-" + person.getPersonId() + "-" + id);
            persons.add(person);
        }
        else {
            buffer.add(person);
        }
        notifyAll();
    }
    
    public synchronized void setWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // 第一类reset结束后，将buffer中的东西移到请求队列中，并输出receive信息
    public synchronized void transFromBufferToSub() {
        Iterator<Person> iterator = buffer.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            TimableOutput.println("RECEIVE-" + person.getPersonId() + "-" + id);
            persons.add(person);
            iterator.remove();
        }
        notifyAll();
    }
   
    // 第二类reset结束后，将buffer中的东西移到主请求表之中
    public synchronized void transFromBufferToMain() {
        MainQueue mainQueue = MainQueue.getInstance();
        Iterator<Person> iterator = buffer.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            mainQueue.addRequest(person);
            iterator.remove();
        }
    }
  
    // reset开始时，将sub中的请求全部移到main中
    public synchronized void transFromSubToMain() {
        MainQueue mainQueue = MainQueue.getInstance();
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            mainQueue.addRequest(person);
            iterator.remove();
        }
    }
    
    public synchronized void acceptReset(ResetRequest resetRequest) {
        this.resetRequest = resetRequest;
        notifyAll();
    }
    
    public synchronized void deleteResetRequest() {
        resetRequest = null;
    }
    
    public synchronized boolean isResetAble() {
        return resetRequest != null;
    }
    
    public synchronized boolean resetIsNormal() {
        if (resetRequest == null) {
            return true; // 实际上这个if永远不会触发
        }
        return resetRequest instanceof ResetRequest;
    }
    
    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }
    
    public synchronized boolean isEnd() {
        return isEnd;
    }
    
    public synchronized int getTransferFloor() {
        // 前两个if永远不会触发
        if (resetRequest == null) {
            return 114514;
        }
        if (resetRequest instanceof ResetRequest) {
            return 191918;
        }
        TimableOutput.println(1);
        return 1111;
    }
    
    public synchronized int getNextCap() {
        if (resetRequest == null) {
            return 0;
        }
        if (resetRequest instanceof ResetRequest) {
            return resetRequest.getCapacity();
        }
        else {
            TimableOutput.println(1);
            return 0;
        }
    }
    
    public synchronized double getNextSpeed() {
        if (resetRequest == null) {
            return 0;
        }
        if (resetRequest instanceof ResetRequest) {
            return resetRequest.getSpeed();
        }
        else {
            TimableOutput.println(1);
            return 0;
        }
    }
    
    // 获取persons与buffer中的所有请求数量之和，防止tle
    public synchronized int getReqPeoNum() {
        return persons.size() + buffer.size();
    }
    
    public synchronized int getFarthestFloorInQue(int curFloor, int direction) {
        int desFloor = curFloor;
        for (Person person : persons) {
            if ((person.getToFloor() - desFloor) * direction > 0) {
                desFloor = person.getToFloor();
            }
        }
        int flag = 0;
        for (Person person : buffer) {
            if ((person.getToFloor() - desFloor) * direction > 0) {
                desFloor = person.getToFloor();
                flag = 1;
            }
        }
        if (flag == 1) {
            desFloor += 2; // 加一个偏移量
        }
        return desFloor;
    }
    
    public synchronized boolean isEmpty() {
        return persons.isEmpty();
    }
    
    public synchronized Person getOneRequestAndRemove(int curFloor, int direction) {
        if (persons.isEmpty()) {
            return null;
        }
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getFromFloor() == curFloor && person.getDirection() == direction) {
                iterator.remove();
                notifyAll();
                return person;
            }
        }
        return null;
    }
    
    public synchronized boolean canInElevator(int curPeoNum, int capacity,
                                              int curFloor, int direction) {
        if (curPeoNum == capacity) {
            return false;
        }
        for (Person person : persons) {
            if (person.getFromFloor() == curFloor && person.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean hasReqInSameDir(int curFloor, int direction) {
        for (Person person : persons) {
            if ((person.getFromFloor() - curFloor) * direction > 0) {
                return true;
            }
        }
        return false;
    }
}
