import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualTagIdException;
import com.oocourse.spec2.exceptions.TagIdNotFoundException;
import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.PathNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private int personNum;
    
    private int blockSum;
    private int tripleSum;
    private DisjointSet disjointSet;
    
    private boolean dirtyForDisJointSet;  // true时需要重建
    private final HashMap<Integer, HashSet<Integer>> allSides;
    // 重建并查集时用，key集合为所有边的最小值之集合，value集合为对任何一个点的紧邻点集合
    
    private boolean dirtyForCoupleSum; // 感觉非常鸡肋
    private int coupleSum;
    
    public MyNetwork() {
        this.persons = new HashMap<>();
        this.personNum = 0;
        this.blockSum = 0;
        this.tripleSum = 0;
        this.disjointSet = new DisjointSet();
        this.dirtyForDisJointSet = true;
        this.allSides = new HashMap<>();
        this.dirtyForCoupleSum = true;
        this.coupleSum = 0;
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
        
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        // 建立关系
        person1.addRelation(id2, person2, value);
        person2.addRelation(id1, person1, value);
        
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
        
        tripleSum += getNumOfSameAdjacentPoints(id1, id2);
        dirtyForCoupleSum = true;
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
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        int oldValue = person1.queryValue(id2);
        int nextValue =  oldValue + value;
        
        // 修改关系
        if (nextValue > 0) { // 如果nextValue > 0
            person1.changeValue(id2, nextValue);
            person2.changeValue(id1, nextValue);
        }
        else { // 如果nextValue <= 0
            // 删除边,此时边必然存在
            int min = Math.min(id1, id2);
            int max = Math.max(id1, id2);
            HashSet<Integer> dots = allSides.get(min);
            dots.remove(max);
            if (dots.isEmpty()) {
                allSides.remove(min, dots);
            }
            // 互删tag
            person1.deleteAllTagsWithOnePerson(person2);
            person2.deleteAllTagsWithOnePerson(person1);
            // 互删关系
            person1.deleteRelation(id2);
            person2.deleteRelation(id1);
            // 将并查集脏位置为true
            dirtyForDisJointSet = true;
            // 维护tripleSum
            tripleSum -= getNumOfSameAdjacentPoints(id1, id2);
        }
        dirtyForCoupleSum = true;
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
        return ((MyPerson)(persons.get(id1))).queryValue(id2);
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
    
    @Override
    public int queryTripleSum() {
        return tripleSum;
    }
    
    @Override
    public void addTag(int personId, Tag tag)  throws PersonIdNotFoundException,
            EqualTagIdException {
        // 检测异常
        int tagId = tag.getId();
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (persons.get(personId).containsTag(tagId)) {
            throw new MyEqualTagIdException(tagId);
        }
        // 加入tag
        persons.get(personId).addTag(tag);
    }
    
    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId) throws
            PersonIdNotFoundException, RelationNotFoundException, TagIdNotFoundException,
            EqualPersonIdException {
        // 检测异常
        if (!persons.containsKey(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }
        else if (!containsPerson(personId2)) {
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
        // add personId1 to personId2's tag
        Tag tag = persons.get(personId2).getTag(tagId);
        // tag.getSize <= 1111
        if (tag.getSize() <= 1111) {
            tag.addPerson(persons.get(personId1));
        }
    }
    
    @Override
    public int queryTagValueSum(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        // 检测异常
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
        // 查询valueSum
        return persons.get(personId).getTag(tagId).getValueSum();
    }
    
    @Override
    public int queryTagAgeVar(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        // 检测异常
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
        // 查询ageVar
        return persons.get(personId).getTag(tagId).getAgeVar();
    }
    
    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId) throws
            PersonIdNotFoundException, TagIdNotFoundException {
        // 检测异常
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
        // delPersonId1FromTag
        persons.get(personId2).getTag(tagId).delPerson(persons.get(personId1));
    }
    
    @Override
    public void delTag(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        // 检测异常
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        else if (!persons.get(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }
        // delTag
        persons.get(personId).delTag(tagId);
    }
    
    @Override
    public int queryBestAcquaintance(int id) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        // 检查异常
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        else if (((MyPerson) persons.get(id)).getAcquaintance().isEmpty()) {
            throw new MyAcquaintanceNotFoundException(id);
        }
        
        return ((MyPerson) persons.get(id)).getBestId();
    }
    
    @Override
    public int queryCoupleSum() {
        if (!dirtyForCoupleSum) {
            return coupleSum;
        }
        dirtyForCoupleSum = false;
        int cnt = 0; // 记得除以2
        for (Integer id : persons.keySet()) {
            if (((MyPerson) persons.get(id)).getAcquaintance().isEmpty()) {
                continue;
            }
            int bestId1 = ((MyPerson) persons.get(id)).getBestId();
            int bestId2 = ((MyPerson) persons.get(bestId1)).getBestId();
            if (id == bestId2) {
                ++cnt;
            }
        }
        coupleSum = cnt / 2;
        return coupleSum;
    }
    
    @Override
    public int queryShortestPath(int id1, int id2) throws PersonIdNotFoundException,
            PathNotFoundException {
        // 检测异常
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!persons.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (!isCircle(id1, id2)) { //不可能触发isCircle中的异常
            throw new MyPathNotFoundException(id1, id2);
        }
        // 特判一下id1是否等于id2
        if (id1 == id2) {
            return 0;
        }
        // 利用BFS求单源最短路径长度
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(id1);
        HashMap<Integer, Integer> distance = new HashMap<>(); // 记录从id1到各个顶点最短路径上边的个数
        distance.put(id1, 1);
        HashSet<Integer> visited = new HashSet<>(); // 标记是否被访问
        visited.add(id1); // 把id1加入visited，因为已经被访问

        while (!queue.isEmpty()) {
            int headId = queue.remove(0); // 删除并返回队列的第一个元素
            // 以随机的顺序遍历所有熟人,防止hack
            for (int linkedId : ((MyPerson) persons.get(headId)).getAcquaintance().keySet()) {
                if (!visited.contains(linkedId)) { // 防止形成圈
                    visited.add(linkedId); // 将linkedId标记为已访问
                    queue.addLast(linkedId); // 将linkedId入队
                    distance.put(linkedId, distance.get(headId) + 1); // 更新distance
                    if (linkedId == id2) { // 当扫描到id2时终止
                        return distance.get(id2) - 2; // 减去起点与终点
                    }
                }
            }
        }
        return distance.get(id2) - 2; // 没任何用处
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int getNumOfSameAdjacentPoints(int id1, int id2) {
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        HashMap<Integer, Person> acquaintanceOfPerson1 = ((MyPerson) person1).getAcquaintance();
        HashMap<Integer, Person> acquaintanceOfPerson2 = ((MyPerson) person2).getAcquaintance();
        HashMap<Integer, Person> acquaintanceOfSmall;
        Person personBig;
        if (acquaintanceOfPerson1.size() < acquaintanceOfPerson2.size()) {
            acquaintanceOfSmall = acquaintanceOfPerson1;
            personBig = person2;
        }
        else {
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