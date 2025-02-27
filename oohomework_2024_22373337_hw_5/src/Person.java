import com.oocourse.elevator1.PersonRequest;

public class Person {
    private final int personId;
    private final int fromFloor;
    private final int toFloor;
    private final int elevatorId;
    private final int direction;
    
    public Person(PersonRequest request) {
        this.personId = request.getPersonId();
        this.fromFloor = request.getFromFloor();
        this.toFloor = request.getToFloor();
        this.elevatorId = request.getElevatorId();
        if (this.toFloor > this.fromFloor) {
            direction = 1;
        }
        else {
            direction = -1;
        }
    }
    
    public int getPersonId() {
        return personId;
    }
    
    public int getFromFloor() {
        return fromFloor;
    }
    
    public int getToFloor() {
        return toFloor;
    }
    
    public int getElevatorId() {
        return elevatorId;
    }
    
    public int getDirection() {
        return direction;
    }
}
