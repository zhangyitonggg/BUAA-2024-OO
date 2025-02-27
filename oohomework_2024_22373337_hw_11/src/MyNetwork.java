import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.EqualTagIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.TagIdNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;
import exc.MyEmojiIdNotFoundException;
import exc.MyEqualEmojiIdException;
import exc.MyEqualMessageIdException;
import exc.MyEqualPersonIdException;
import exc.MyMessageIdNotFoundException;
import exc.MyPathNotFoundException;
import exc.MyPersonIdNotFoundException;
import exc.MyRelationNotFoundException;
import exc.MyTagIdNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons = new HashMap<>();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashMap<Integer, Integer> emojis = new HashMap<>(); // id to heat
    private int blockSum = 0;
    private int tripleSum = 0;
    private DisjointSet disjointSet = new DisjointSet();
    private boolean dirtyForDisJointSet = true;  // true时需要重建
    // 重建并查集时用，key集合为所有边的最小值之集合，value集合为对任何一个点的紧邻点集合
    private final HashMap<Integer, HashSet<Integer>> allSides = new HashMap<>();
    private boolean dirtyForCoupleSum = true; // 感觉非常鸡肋
    private int coupleSum = 0;
    private final MyTool tool;
    
    // just for testing
    private int[] emojiHeadList;
    
    public MyNetwork() {
        this.tool = new MyTool(persons);
    }
    
    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }
    
    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        int messageId = message.getId();
        if (messages.containsKey(messageId)) {
            throw new MyEqualMessageIdException(messageId);
        } else if (message instanceof EmojiMessage &&
                !emojis.containsKey(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(messageId, message);
    }
    
    @Override
    public Message getMessage(int id) {
        if (messages.containsKey(id)) {
            return messages.get(id);
        }
        return null;
    }
    
    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, TagIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage message = (MyMessage) messages.get(id);
        int socialValue = message.getSocialValue();
        MyPerson person1 = (MyPerson) message.getPerson1();
        int personId1 = person1.getId();
        if (message.getType() == 0) {
            MyPerson person2 = (MyPerson) messages.get(id).getPerson2();
            int personId2 = person2.getId();
            if (!(person1.isLinked(person2))) {
                throw new MyRelationNotFoundException(personId1, personId2);
            } // 之后代表正常，因为equal的可能性已经被addMessage排除
            messages.remove(id);
            person1.addSocialValue(socialValue);
            person2.addSocialValue(socialValue);
            if (message instanceof MyRedEnvelopeMessage) {
                int money = ((MyRedEnvelopeMessage) message).getMoney();
                person1.addMoney(-1 * money);
                person2.addMoney(money);
            }
            if (message instanceof MyEmojiMessage) {
                int emojiId = ((MyEmojiMessage) message).getEmojiId();
                int oldHeat = emojis.get(emojiId);
                emojis.put(emojiId, oldHeat + 1);
            }
            person2.addMessageOnHead(message); // 注意下面tag的情况没有
        } else {
            MyTag tag = (MyTag) message.getTag();
            int tagId = tag.getId();
            if (!(person1.containsTag(tagId))) {
                throw new MyTagIdNotFoundException(tagId);
            }
            messages.remove(id);
            person1.addSocialValue(socialValue);
            tag.addAllSocialValue(socialValue);
            if (message instanceof MyRedEnvelopeMessage && tag.getSize() > 0) {
                int perMoney = ((MyRedEnvelopeMessage) message).getMoney() / tag.getSize();
                person1.addMoney(-1 * perMoney * tag.getSize());
                tag.addAllMoney(perMoney);
            }
            if (message instanceof MyEmojiMessage) {
                int emojiId = ((MyEmojiMessage) message).getEmojiId();
                int oldHeat = emojis.get(emojiId);
                emojis.put(emojiId, oldHeat + 1);
            }
        }
    }
    
    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return persons.get(id).getSocialValue();
    }
    
    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return persons.get(id).getReceivedMessages();
    }
    
    @Override
    public boolean containsEmojiId(int id) {
        return emojis.containsKey(id);
    }
    
    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojis.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojis.put(id, 0);
    }
    
    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return persons.get(id).getMoney();
    }
    
    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojis.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojis.get(id);
    }
    
    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> iteratorEmoji = emojis.entrySet().iterator();
        while (iteratorEmoji.hasNext()) {
            Map.Entry<Integer, Integer> oneEmoji = iteratorEmoji.next();
            if (oneEmoji.getValue() < limit) {
                iteratorEmoji.remove();
            }
        }
        Iterator<Map.Entry<Integer, Message>> iteratorMessage = messages.entrySet().iterator();
        while (iteratorMessage.hasNext()) {
            Map.Entry<Integer, Message> oneMessage = iteratorMessage.next();
            if (oneMessage.getValue() instanceof MyEmojiMessage) {
                if (!emojis.containsKey(((MyEmojiMessage) oneMessage.getValue()).getEmojiId())) {
                    iteratorMessage.remove();
                }
            }
        }
        return emojis.size();
    }
    
    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!persons.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        ((MyPerson) persons.get(personId)).clearNotices();
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
        tool.checkAddPerson(tempId);
        persons.put(tempId, person);
        ++blockSum;                           // 维护极大连通图数目
        disjointSet.add(tempId);              // 加入到并查集中
    }
    
    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualRelationException {
        tool.checkAddRelation(id1, id2);
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
        tripleSum += tool.getNumOfSameAdjacentPoints(id1, id2);
        dirtyForCoupleSum = true;
        tool.maintainValueSum(id1, id2, value, person1, person2);
    }
    
    @Override
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            MyEqualPersonIdException, MyRelationNotFoundException {
        tool.checkModifyRelation(id1, id2);
        // 算出nexValue
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        int oldValue = person1.queryValue(id2);
        int nextValue =  oldValue + value;
        // 修改关系
        if (nextValue > 0) { // 如果nextValue > 0
            person1.changeValue(id2, nextValue);
            person2.changeValue(id1, nextValue);
            tool.maintainValueSum(id1, id2, value, person1, person2);
        }
        else { // 如果nextValue <= 0
            tool.maintainValueSum(id1, id2, -1 * oldValue, person1, person2);
            int min = Math.min(id1, id2); // 删除边,此时边必然存在
            int max = Math.max(id1, id2);
            HashSet<Integer> dots = allSides.get(min);
            dots.remove(max);
            if (dots.isEmpty()) {
                allSides.remove(min, dots);
            }
            person1.deleteAllTagsWithOnePerson(person2); // 互删tag
            person2.deleteAllTagsWithOnePerson(person1);
            person1.deleteRelation(id2); // 互删关系
            person2.deleteRelation(id1);
            dirtyForDisJointSet = true; // 将并查集脏位置为true
            tripleSum -= tool.getNumOfSameAdjacentPoints(id1, id2); // 维护tripleSum
        }
        dirtyForCoupleSum = true;
    }
    
    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException,
            RelationNotFoundException {
        tool.checkQueryValue(id1, id2);
        return ((MyPerson)(persons.get(id1))).queryValue(id2);
    }
    
    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        tool.checkIsCircle(id1, id2);
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
        tool.checkAddTag(personId, tag);
        persons.get(personId).addTag(tag);
    }
    
    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId) throws
            PersonIdNotFoundException, RelationNotFoundException, TagIdNotFoundException,
            EqualPersonIdException {
        tool.checkAddPersonToTag(personId1, personId2, tagId);
        Tag tag = persons.get(personId2).getTag(tagId); // add personId1 to personId2's tag
        if (tag.getSize() <= 1111) {
            tag.addPerson(persons.get(personId1));
        }
    }
    
    @Override
    public int queryTagValueSum(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        tool.checkQueryTagValueSum(personId, tagId);
        return persons.get(personId).getTag(tagId).getValueSum();
    }
    
    @Override
    public int queryTagAgeVar(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        tool.checkQueryTagAgeVar(personId, tagId);
        return persons.get(personId).getTag(tagId).getAgeVar();
    }
    
    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId) throws
            PersonIdNotFoundException, TagIdNotFoundException {
        tool.checkDelPersonFromTag(personId1, personId2, tagId);
        persons.get(personId2).getTag(tagId).delPerson(persons.get(personId1));
    }
    
    @Override
    public void delTag(int personId, int tagId) throws PersonIdNotFoundException,
            TagIdNotFoundException {
        tool.checkDelTag(personId, tagId);
        persons.get(personId).delTag(tagId);
    }
    
    @Override
    public int queryBestAcquaintance(int id) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        tool.checkQueryBestAcquaintance(id);
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
        tool.checkQueryShortestPathNotAll(id1, id2);
        if (!isCircle(id1, id2)) { //不可能触发isCircle中的异常
            throw new MyPathNotFoundException(id1, id2);
        }
        if (id1 == id2) { // 特判一下id1是否等于id2
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
    
    private void remakeDisJointSet() {
        blockSum = persons.size();
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
    
    public Message[] getMessages() {
        int size = messages.size();
        Message[] array = new Message[size];
        int iter = 0;
        for (Message message : messages.values()) {
            array[iter++] = message;
        }
        return array;
    }
    
    public int[] getEmojiIdList() {
        int size = emojis.size();
        int[] array = new int[size];
        emojiHeadList = new int[size];
        int iter = 0;
        for (int emojiId : emojis.keySet()) {
            array[iter] = emojiId;
            emojiHeadList[iter] = emojis.get(emojiId);
            iter++;
        }
        return array;
    }
    
    public int[] getEmojiHeatList() {
        return emojiHeadList;
    }
}