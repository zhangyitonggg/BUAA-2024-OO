import java.util.ArrayList;

public class MainQueue {
    private static MainQueue mainQueue;
    private boolean isInputEnd;            // 此处的inputEnd只表示是否会有新请求
    private final ArrayList<Person> persons;
    private int numNotFinished;
    private int numInMain;
    private int resetCount;
    
    private MainQueue() {
        resetCount = 0;
        isInputEnd = false;
        numInMain = 0;
        numNotFinished = 0;
        persons = new ArrayList<>();
    }
    
    public static synchronized MainQueue getInstance() {
        if (mainQueue == null) {
            mainQueue = new MainQueue();
        }
        return mainQueue;
    }
    
    public synchronized void addRequest(Person person) {
        numInMain++;
        persons.add(person);
        notifyAll();
    }
    
    public synchronized void setInputEnd(boolean inputEnd) {
        this.isInputEnd = inputEnd;
        notifyAll();
    }
    
    public synchronized void addNumNotFinished() {
        numNotFinished++;
    }
    
    public synchronized void subNumNotFinished() {
        numNotFinished--;
        notifyAll();
    }
    
    public synchronized void addResetCount() {
        ++resetCount;
    }
    
    public synchronized void subResetCount() {
        --resetCount;
        notifyAll();
    }
    
    public synchronized boolean isRealEnd() {
        return (this.isInputEnd) & (this.numNotFinished == 0) & (this.resetCount == 0);
    }
    
    public synchronized Person transferFromMainToSub() {
        while (true) {
            if (this.numInMain == 0 && !isRealEnd()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (isRealEnd()) {
                return null;
            }
            if (numInMain != 0) {
                break;
            }
        }
        Person person = persons.get(0);
        persons.remove(0);
        --numInMain;
        return  person;
    }
}
