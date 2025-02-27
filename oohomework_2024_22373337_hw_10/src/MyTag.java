import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;
import java.util.HashMap;
import java.util.HashSet;

public class MyTag implements Tag {
    private final int tagId;
    
    private final HashMap<Integer, Person> persons;
    
    private int ageSum;
    private int agePowSum;

    public MyTag(int tagId) {
        this.tagId = tagId;
        this.persons = new HashMap<>();
        ageSum = 0;
        agePowSum = 0;
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
        
        persons.put(person.getId(), person);
    }
    
    @Override
    public boolean hasPerson(Person person) {
        return persons.containsKey(person.getId());
    }
    
    @Override
    public int getValueSum() {
        int halfValueSum = 0;
        HashSet<Integer> visited = new HashSet<>(); // 记录已访问的id
        for (int id1 : persons.keySet()) {
            visited.add(id1);
            MyPerson person1 = (MyPerson) persons.get(id1);
            if (person1.getSize() < persons.size()) { // 当person1的熟人数量比tag的persons数量少时
                for (int id2 : person1.getAcquaintance().keySet()) {
                    if (persons.containsKey(id2) && !visited.contains(id2)) {
                        halfValueSum += person1.queryValue(id2); // queryValue在二者不认识时返回0
                    }
                }
            }
            else { // 当person1的熟人数量比tag的persons数量多时
                for (int id2 : persons.keySet()) {
                    if (!visited.contains(id2)) {
                        halfValueSum += person1.queryValue(id2);
                    }
                }
            }
        }
        return halfValueSum * 2; // 乘以2
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
        }
    }
    
    @Override
    public int getSize() {
        return persons.size();
    }
}
