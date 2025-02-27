import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private int personNum;

    private int blockSum;
    private int tripleSum;
    private DisjointSet disjointSet;

    private boolean dirtyForDisJointSet;  // true时需要重建
    private final HashMap<Integer, HashSet<Integer>> allSides;
    // 重建并查集时用，key集合为所有边的最小值之集合，value集合为对任何一个点的紧邻点集合

    public static ArrayList<Integer> produceSide(int min, int max) {
        ArrayList<Integer> side = new ArrayList<>();
        side.add(min);
        side.add(max);
        return side;
    }

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.personNum = 0;
        this.blockSum = 0;
        this.tripleSum = 0;
        this.disjointSet = new DisjointSet();
        this.allSides = new HashMap<>();
        this.dirtyForDisJointSet = false;
    }

    @Override
    public boolean containsPerson(int id) {
        return persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (persons.containsKey(id)) {
            return persons.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int tempId = person.getId();
        if (persons.containsKey(tempId)) {
            throw new MyEqualPersonIdException(tempId);
        }
        else {
            ++personNum;
            persons.put(tempId, person);
            ++blockSum;                           // 维护极大连通图数目
            disjointSet.add(tempId);              // 加入到并查集中
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualRelationException {
        // 检查异常
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (getPerson(id1).isLinked(getPerson(id2))) { // 与本身有link,这确保了不会出现自圈
            throw new MyEqualRelationException(id1, id2);
        }

        // 建立关系
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        ((MyPerson)person1).addRelation(id2, person2, value);
        ((MyPerson)person2).addRelation(id1, person1, value);

        // merge并更新blockSum
        if (disjointSet.merge(id1, id2)) {
            --blockSum;
        }
        // 增加边
        int max = Math.max(id1, id2);
        int min = Math.min(id1, id2);
        if (allSides.containsKey(min)) {
            allSides.get(min).add(max);
        }
        else {
            HashSet<Integer> dots = new HashSet<>();
            dots.add(max);
            allSides.put(min, dots);
        }

        tripleSum += getNumOfSameAdjacentPointsV2(id1, id2);
    }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws MyPersonIdNotFoundException,
            MyEqualPersonIdException, MyRelationNotFoundException {
        // 检查异常
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        }
        else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }

        // 算出nexValue
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        int nextValue =  person1.queryValue(person2) + value;

        // 修改关系
        if (nextValue > 0) { // 如果nextValue > 0
            ((MyPerson) person1).changeValue(id2, nextValue);
            ((MyPerson) person2).changeValue(id1, nextValue);
        }
        else { // 如果nextValue <= 0
            // 删除边,此时边必然存在
            int max = Math.max(id1, id2);
            int min = Math.min(id1, id2);
            HashSet<Integer> dots = allSides.get(min);
            dots.remove(max);
            if (dots.isEmpty()) {
                allSides.remove(min, dots);
            }
            // 互删关系
            ((MyPerson) person1).deleteRelation(id2);
            ((MyPerson) person2).deleteRelation(id1);
            // 将并查集脏位置为true
            dirtyForDisJointSet = true;
            // 维护tripleSum
            tripleSum -= getNumOfSameAdjacentPointsV2(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException,
            RelationNotFoundException {
        // 检测异常
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        // 返回答案
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        // 检测异常
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        if (dirtyForDisJointSet) {
            remakeDisJointSet();
        }
        return disjointSet.find(id1) == disjointSet.find(id2);
    }

    @Override
    public int queryBlockSum() {
        if (dirtyForDisJointSet) {
            remakeDisJointSet();
        }
        return blockSum;
    }

    public int queryTripleSum() {
        return tripleSum;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int getNumOfSameAdjacentPointsV1(int id1, int id2) {  // 已经弃用
        int cnt = 0;
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        for (Person person : persons.values()) {
            if (person.isLinked(person1) &&  person.isLinked(person2)) {
                ++cnt;
            }
        }
        // 考虑到id1与id2一定与自身相邻
        if (person1.isLinked(person2)) {
            --cnt;
        }
        if (person2.isLinked(person1)) {
            --cnt;
        }
        return cnt;
    }

    private int getNumOfSameAdjacentPointsV2(int id1, int id2) {
        int cnt = 0;
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        HashMap<Integer, Person> acquaintanceOfPerson1 = ((MyPerson) person1).getAcquaintance();
        for (Person person : acquaintanceOfPerson1.values()) {  // 实际上还可以进一步优化，即遍历熟人少的
            if (person.isLinked(person2)) {
                ++cnt;
            }
        }
        if (acquaintanceOfPerson1.containsKey(id2)) {
            --cnt;
        }
        return cnt;
    }

    private void remakeDisJointSet() {
        blockSum = personNum;
        disjointSet = new DisjointSet();
        for (Person person : persons.values()) {
            disjointSet.add(person.getId());
        }

        for (int min : allSides.keySet()) {
            for (int max : allSides.get(min)) {
                if (disjointSet.merge(min, max)) {
                    --blockSum;
                }
            }
        }

        dirtyForDisJointSet = false;
    }

    public Person[] getPersons() {
        return persons.values().toArray(new Person[0]);
    }
}
