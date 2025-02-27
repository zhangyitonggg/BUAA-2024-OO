import com.oocourse.spec1.main.Person;
import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getAge() {
        return this.age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        Person person = (Person)obj;
        return this.id == person.getId();
    }
    
    @Override
    public boolean isLinked(Person person) {
        int tempId = person.getId();
        return (acquaintance.containsKey(tempId) || tempId == this.id);
    }
    
    @Override
    public int queryValue(Person person) {
        int tempId = person.getId();
        if (acquaintance.containsKey(tempId)) {
            return value.get(tempId);
        }
        return 0;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void addRelation(int  tempId, Person person, int value) {
        this.acquaintance.put(tempId, person);
        this.value.put(tempId, value);
    }
    
    public void deleteRelation(int tempId) {
        this.acquaintance.remove(tempId);
        this.value.remove(tempId);
    }
    
    public void changeValue(int tempId, int nextValue) {
        value.put(tempId, nextValue);
    }
    
    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }
    
    public HashMap<Integer, Integer> getValue() {
        return value;
    }
    
    public boolean strictEquals(Person person) {
        return true;
    }
}
