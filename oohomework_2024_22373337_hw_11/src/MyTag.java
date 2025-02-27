import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;
import java.util.HashMap;

public class MyTag implements Tag {
    private final int tagId;
    
    private final HashMap<Integer, Person> persons;
    
    private int ageSum;
    private int agePowSum;

    private int halfValueSum;
    
    public MyTag(int tagId) {
        this.tagId = tagId;
        this.persons = new HashMap<>();
        this.ageSum = 0;
        this.agePowSum = 0;
        this.halfValueSum = 0;
    }
    
    @Override
    public int getId() {
        return tagId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyTag)) {
            return false;
        }
        MyTag tag = (MyTag) obj;
        return this.tagId == tag.getId();
    }
    
    @Override
    public void addPerson(Person person) {
        int age = person.getAge();
        ageSum +=  age;
        agePowSum += age * age;
        
        for (int iter : persons.keySet()) {
            halfValueSum += ((MyPerson) person).queryValue(iter);
        }
        
        persons.put(person.getId(), person);
    }
    
    @Override
    public boolean hasPerson(Person person) {
        return persons.containsKey(person.getId());
    }
    
    @Override
    public int getValueSum() {
        return halfValueSum * 2;
    }
    
    public void maintainValueSum(int id1, int id2, int change) {
        if (persons.containsKey(id1) && persons.containsKey(id2)) {
            halfValueSum += change;
        }
    }
    
    @Override
    public int getAgeMean() { // 向下取整
        int num = persons.size();
        if (num == 0) {
            return 0;
        }
        return ageSum / num;
    }
    
    @Override
    public int getAgeVar() {
        int num = persons.size();
        if (num == 0) {
            return 0;
        }
        int ageMean = ageSum / num;
        // 注意getAgeMean()返回的是向下取整的平均数
        return (agePowSum - 2 * ageMean * ageSum + num * ageMean * ageMean) / num;
    }
    
    @Override
    public void delPerson(Person person) {
        int personId = person.getId();
        if (persons.containsKey(personId)) {
            persons.remove(personId);
            
            int age = person.getAge();
            ageSum -=  age;
            agePowSum -= age * age;
            
            for (int iter : persons.keySet()) {
                halfValueSum -= ((MyPerson) person).queryValue(iter);
            }
        }
    }
    
    @Override
    public int getSize() {
        return persons.size();
    }
    
    public void addAllSocialValue(int num) {
        for (Person person : persons.values()) {
            person.addSocialValue(num);
        }
    }
    
    public void addAllMoney(int num) {
        for (Person person : persons.values()) {
            person.addMoney(num);
        }
    }
}
