import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;
import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;
    private final HashMap<Integer, Tag> tags;
    private boolean dirtyForBestId;
    private int bestId;
    private int bestValue;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.tags = new HashMap<>();
        dirtyForBestId = true;
        bestId = 114514;
        bestValue = 0;
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
        if (!(obj instanceof Person)) {
            return false;
        }
        Person person = (Person)obj;
        return this.id == person.getId();
    }
    
    @Override
    public boolean containsTag(int tagId) {
        return tags.containsKey(tagId);
    }
    
    @Override
    public Tag getTag(int tagId) {
        if (tags.containsKey(tagId)) {
            return tags.get(tagId);
        }
        return null;
    }
    
    @Override
    public void addTag(Tag tag) {
        tags.put(tag.getId(), tag);
    }
    
    @Override
    public void delTag(int tagId) {
        tags.remove(tagId);
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
    
    public int queryValue(int tempId) {
        if (acquaintance.containsKey(tempId)) {
            return value.get(tempId);
        }
        return 0;
    }
   
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void addRelation(int tempId, Person person, int value) {
        this.acquaintance.put(tempId, person);
        this.value.put(tempId, value);
        
        if (!dirtyForBestId) { // 可以动态维护
            if (value > bestValue || (value == bestValue && tempId < bestId)) {
                bestValue = value;
                bestId = tempId;
            }
        }
    }
    
    public void deleteRelation(int tempId) {
        this.acquaintance.remove(tempId);
        this.value.remove(tempId);
        
        if (!dirtyForBestId) {
            if (tempId == bestId) {
                dirtyForBestId = true;
            }
        }
    }
    
    public void changeValue(int tempId, int nextValue) {
        value.put(tempId, nextValue);
        if (!dirtyForBestId) {
            if (tempId == bestId) {
                if (nextValue < bestValue) { // 如果变小，则需进一步判断，则置dirty为true；否则，无需处理
                    dirtyForBestId = true;
                }
                else {
                    bestValue = nextValue;
                }
            }
            else {
                if (nextValue > bestValue || (nextValue == bestValue && tempId < bestId)) {
                    bestValue = nextValue;
                    bestId = tempId;
                }
            }
        }
    }
    
    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }
    
    public void deleteAllTagsWithOnePerson(Person person) {
        for (Tag tag : tags.values()) {
            tag.delPerson(person); // 在方法内判断是否有person
        }
    }
    
    public int getBestId() {
        if (!dirtyForBestId) { // 如果dirty为false，则直接返回维护的bestId即可
            return bestId;
        }
        dirtyForBestId = false; // 重置dirty
        // 如果dirty为true，则必须重新遍历一遍
        bestValue = 0;
        bestId = 114514;
        for (int tempId : this.value.keySet()) {
            int tempValue = this.value.get(tempId);
            if (tempValue > bestValue || (tempValue == bestValue && tempId < bestId)) {
                bestValue = tempValue;
                bestId = tempId;
            }
        }
        return bestId;
    }
    
    public int getSize() {
        return acquaintance.size();
    }
    
    public boolean strictEquals(Person person) {
        return true;
    }
}

