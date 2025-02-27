import java.util.ArrayList;
import java.util.Iterator;

public class RequestQueue {
    private boolean inputEnd;
    private ArrayList<Person> persons;
    private int num;
    
    public RequestQueue() {
        inputEnd = false;
        persons = new ArrayList<>();
        num = 0;
    }
    
    public synchronized ArrayList<Person> getPersons() {
        // notifyAll();
        return persons;
    }

    public synchronized void addRequest(Person person) {
        persons.add(person);
        ++num;
        notifyAll();
    }
    
    public synchronized int getNum() {
        // notifyAll();
        return num;
    }
    
    public synchronized void setInputEnd(Boolean inputEnd) {
        this.inputEnd = inputEnd;
        notifyAll();
    }
    
    public synchronized boolean isInputEnd() {
        // notifyAll();
        return inputEnd;
    }
    
    public synchronized Person transferFromMainToSub() {
        if (this.num == 0 && !this.inputEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.num == 0) {
            // notifyAll();
            return null;
        }
        
        --num;
        Person person = persons.get(0);
        persons.remove(0);
        // notifyAll();
        return person;
    }
    
    public synchronized Person getOneRequestAndRemove(int curFloor, int direction) {
        if (this.num == 0) {
            // notifyAll();
            return null;
        }
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getFromFloor() == curFloor && person.getDirection() == direction) {
                iterator.remove();
                --num;
                notifyAll();
                return person;
            }
        }
        // notifyAll();
        return null;
    }
}
