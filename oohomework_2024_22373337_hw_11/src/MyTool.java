import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;
import exc.MyAcquaintanceNotFoundException;
import exc.MyEqualPersonIdException;
import exc.MyEqualRelationException;
import exc.MyEqualTagIdException;
import exc.MyPersonIdNotFoundException;
import exc.MyRelationNotFoundException;
import exc.MyTagIdNotFoundException;

import java.util.HashMap;

public class MyTool {
    private final HashMap<Integer, Person> persons;
    
    public MyTool(HashMap<Integer, Person> persons) {
        this.persons = persons;
    }
    
    public void maintainValueSum(int id1, int id2, int change, MyPerson person1,
                                 MyPerson person2) {
        if (person1.getSize() < person2.getSize()) {
            for (Person person : person1.getAcquaintance().values()) {
                if (person.isLinked(person2) && person.getId() != id2) {
                    ((MyPerson) person).maintainValueSum(id1, id2, change);
                }
            }
        } else {
            for (Person person : person2.getAcquaintance().values()) {
                if (person.isLinked(person1) && person.getId() != id1) {
                    ((MyPerson) person).maintainValueSum(id1, id2, change);
                }
            }
        }
    }
    
    public int getNumOfSameAdjacentPoints(int id1, int id2) {
        Person person1 = persons.get(id1);
        Person person2 = persons.get(id2);
        HashMap<Integer, Person> acquaintanceOfPerson1 = ((MyPerson) person1).getAcquaintance();
        HashMap<Integer, Person> acquaintanceOfPerson2 = ((MyPerson) person2).getAcquaintance();
        HashMap<Integer, Person> acquaintanceOfSmall;
        Person personBig;
        if (acquaintanceOfPerson1.size() < acquaintanceOfPerson2.size()) {
            acquaintanceOfSmall = acquaintanceOfPerson1;
            personBig = person2;
        } else {
            acquaintanceOfSmall = acquaintanceOfPerson2;
            personBig = person1;
        }
        int cnt = 0;
        for (Person person : acquaintanceOfSmall.values()) {
            if (person.isLinked(personBig)) {
                ++cnt;
            }
        }
        if (acquaintanceOfSmall.containsKey(personBig.getId())) {
            --cnt;
        }
        return cnt;
    }
    
    public void checkAddPerson(int tempId) throws MyEqualPersonIdException {
        if (persons.containsKey(tempId)) {
            throw new MyEqualPersonIdException(tempId);
        }
    }
    
    public void checkAddRelation(int id1, int id2) throws MyPersonIdNotFoundException,
            MyEqualRelationException {
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (persons.get(id1).isLinked(persons.get(id2))) { // 与本身有link,这确保了不会出现自圈
            throw new MyEqualRelationException(id1, id2);
        }
    }
    
    public void checkModifyRelation(int id1, int id2) throws MyPersonIdNotFoundException,
            MyEqualPersonIdException, MyRelationNotFoundException {
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        }
        else if (!persons.get(id1).isLinked(persons.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }
    
    public void checkQueryValue(int id1, int id2) throws MyPersonIdNotFoundException,
            MyRelationNotFoundException {
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (!persons.get(id1).isLinked(persons.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }
    
    public void checkIsCircle(int id1, int id2) throws MyPersonIdNotFoundException {
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
    }
    
    public void checkAddTag(int personId, Tag tag) throws MyPersonIdNotFoundException,
            MyEqualTagIdException {
        int tagId = tag.getId();
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (persons.get(personId).containsTag(tagId)) {
            throw new MyEqualTagIdException(tagId);
        }
    }
    
    public void checkAddPersonToTag(int personId1, int personId2, int tagId) throws
            MyPersonIdNotFoundException, MyEqualPersonIdException, MyRelationNotFoundException,
            MyTagIdNotFoundException {
        if (!persons.containsKey(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }
        else if (!persons.containsKey(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        }
        else if (personId1 == personId2) {
            throw new MyEqualPersonIdException(personId1);
        }
        else if (!(persons.get(personId1)).isLinked(persons.get(personId2))) {
            throw new MyRelationNotFoundException(personId1, personId2);
        }
        else if (!(persons.get(personId2)).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
        else if ((persons.get(personId2)).getTag(tagId).hasPerson(persons.get(personId1))) {
            throw new MyEqualPersonIdException(personId1);
        }
    }
    
    public void checkQueryTagValueSum(int personId, int tagId) throws MyPersonIdNotFoundException,
            MyTagIdNotFoundException {
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
    }
    
    public void checkQueryTagAgeVar(int personId, int tagId) throws MyPersonIdNotFoundException,
            MyTagIdNotFoundException {
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
    }
    
    public void checkDelPersonFromTag(int personId1, int personId2, int tagId) throws
            MyPersonIdNotFoundException, MyTagIdNotFoundException {
        if (!persons.containsKey(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }
        else if (!persons.containsKey(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        }
        else if (!persons.get(personId2).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
        else if (!persons.get(personId2).getTag(tagId).hasPerson(persons.get(personId1))) {
            throw new MyPersonIdNotFoundException(personId1);
        }
    }
    
    public void checkDelTag(int personId, int tagId) throws MyPersonIdNotFoundException,
            MyTagIdNotFoundException {
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
    }
    
    public void checkQueryBestAcquaintance(int id) throws MyPersonIdNotFoundException,
            MyAcquaintanceNotFoundException {
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        else if (((MyPerson) persons.get(id)).getAcquaintance().isEmpty()) {
            throw new MyAcquaintanceNotFoundException(id);
        }
    }
    
    public void checkQueryShortestPathNotAll(int id1, int id2) throws MyPersonIdNotFoundException {
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
    }
}
