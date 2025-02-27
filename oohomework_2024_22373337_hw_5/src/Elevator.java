import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private final int id;
    private final RequestQueue requestQueue;
    private int curFloor;
    private int curPeoNum;
    private final ArrayList<Person> persons;
    private int direction;
    private final Strategy strategy;
    
    public Elevator(int id, RequestQueue requestQueue) {
        this.id = id;
        this.requestQueue = requestQueue;
        this.curFloor = 1;
        this.curPeoNum = 0;
        persons = new ArrayList<>();
        direction = 1;
        strategy = new Strategy(requestQueue, persons);
    }
    
    @Override
    public void run() {
        while (true) {
            Com com = strategy.getCommand(curPeoNum, curFloor, direction);
            if (com == Com.OPEN) {
                openThenClose();
            }
            else if (com == Com.MOVE) {
                move();
            }
            else if (com == Com.WAIT) {
                synchronized (requestQueue) {
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (com == Com.REVERSE) {
                direction *= -1;
            }
            else if (com == Com.END) {
                break;
            }
            else {
                System.out.println("error");
            }
        }
    }
    
    private void openThenClose() {
        TimableOutput.println("OPEN-" + curFloor + "-" + id);
        outElevator();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Com com = strategy.getCommand(curPeoNum, curFloor, direction);
        if (com == Com.REVERSE) {
            direction *= -1;
        }
        inElevator();
        TimableOutput.println("CLOSE-" + curFloor + "-" + id);
    }
    
    private void outElevator() {
        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getToFloor() == curFloor) {
                TimableOutput.println("OUT-" + person.getPersonId() + "-" + curFloor + "-" + id);
                --curPeoNum;
                iterator.remove();
            }
        }
    }
    
    private void inElevator() {
        while (curPeoNum < 6) {
            Person person = requestQueue.getOneRequestAndRemove(curFloor, direction);
            if (person == null) {
                return;
            }
            TimableOutput.println("IN-" + person.getPersonId() + "-" + curFloor + "-" + id);
            persons.add(person);
            ++curPeoNum;
        }
    }
    
    private void move() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Com com = strategy.getCommand(curPeoNum, curFloor, direction);
        if (com == Com.OPEN) {
            openThenClose();
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        curFloor += direction;
        TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
    }
}
