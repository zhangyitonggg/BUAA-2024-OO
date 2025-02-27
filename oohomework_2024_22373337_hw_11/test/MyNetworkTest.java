import com.oocourse.spec3.exceptions.*;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.*;
import static org.junit.Assert.assertEquals;
import com.oocourse.spec3.main.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class MyNetworkTest {
    private final MyNetwork network;
    private final MyNetwork shadowNetwork;
    private final int testNum;
    public MyNetworkTest() {
        network = new MyNetwork();
        shadowNetwork = new MyNetwork();
        Random random = new Random();
        testNum = random.nextInt(2000) + 3000;
    }
    
    @Parameters
    public static Collection prepateData() {
        return Arrays.asList(new Object[15][0]);
    }
    
    @Test
    public void testDeleteColdEmoji() {
        init();
        Random random = new Random();
        double s = random.nextDouble();
        for (int i = 0; i < testNum; ++i) {
            double r = random.nextDouble();
            if (r < 0.25) {
                double p = random.nextDouble();
                if (p < 0.35) {
                    add_person();
                }
                else if (p < 0.87) {
                    add_relation();
                }
                else if (p < 0.92) {
                    modify_relation();
                }
                else if (p < 0.94) {
                    query_value();
                }
                else if (p < 0.96) {
                    query_circle();
                }
                else if (p < 0.98) {
                    query_block_sum();
                }
                else {
                    query_triple_sum();
                }
            }
            else if (r < 0.50) {
                double p = random.nextDouble();
                if (p < 0.33) {
                    add_tag();
                }
                else if (p < 0.35) {
                    del_tag();
                }
                else if (p < 0.88) {
                    add_to_tag();
                }
                else if (p < 0.91) {
                    del_from_tag();
                }
                else if (p < 0.93) {
                    query_tag_value_sum();
                }
                else if (p < 0.96) {
                    query_tag_age_var();
                }
                else if (p < 0.98) {
                    query_best_acquaintance();
                }
                else {
                    query_shortest_path();
                }
            }
            else {
                double p = random.nextDouble();
                if (p < 0.40) {
                    add_emoji_message();
                }
                else if (p < 0.42) {
                    add_message();
                }
                else if (p < 0.44) {
                    add_notice_message();
                }
                else if (p < 0.46) {
                    add_red_envelope_message();
                }
                else if (p < 0.62) {
                    store_emoji_id();
                }
                else if (p < 0.9) {
                    send_message();
                }
                else if (p < 0.91) {
                    clean_notices();
                }
                else if (p < 0.93) {
                    query_social_value();
                }
                else if (p < 0.945) {
                    query_received_messages();
                }
                else if (p < 0.96) {
                    query_popularity();
                }
                else if (p < 0.97) {
                    query_money();
                }
                else {
                    if (s < 0.4) {
                        for (int t = 0; t < 5; t++) {
                            store_emoji_id();
                            add_emoji_message();
                            send_message();
                        }
                    }
                    else {
                        if (i < testNum / 3) {
                            for (int t = 0; t < 5; t++) {
                                store_emoji_id();
                                add_emoji_message();
                                send_message();
                            }
                        }
                        else {
                            double q = random.nextDouble();
                            if (q < 0.98) {
                                for (int t = 0; t < random.nextInt(200) + 1; t++) {
                                    store_emoji_id();
                                    add_emoji_message();
                                    send_message();
                                }
                            }
                            else {
                                for (int t = 0; t < random.nextInt(200) + 100; t++) {
                                    store_emoji_id();
                                    add_emoji_message();
                                    send_message();
                                }
                                judgeOneDeleteColdEmoji(i);
                            }
                        }
                    }
                }
            }
        }
        judgeOneDeleteColdEmoji(testNum);
    }
    
    public void judgeOneDeleteColdEmoji(int temp) {
        // 获得shadow数据
        Message[] oldMessages = shadowNetwork.getMessages();
        int[] oldEmojiIdList = shadowNetwork.getEmojiIdList();
        int[] oldEmojiHeatList = shadowNetwork.getEmojiHeatList();
        // 得到myAns以及新的数据
        int limit = genLimit(temp);
        int myAns = network.deleteColdEmoji(limit);
        System.out.println(limit + " " + myAns);
        Message[] newMessages = network.getMessages();
        int[] newEmojiIdList = network.getEmojiIdList();
        int[] newEmojiHeatList = network.getEmojiHeatList();
        // 模拟deleteColdEmoji的过程
        HashSet<Integer> emojiToDeleted = new HashSet<>();
        for (int i = 0; i < oldEmojiIdList.length; ++i) {
            if (oldEmojiHeatList[i] < limit) {
//                System.out.println(oldEmojiIdList[i]);
                emojiToDeleted.add(oldEmojiIdList[i]);
            }
        }
        HashSet<Message> messagesToDeleted = new HashSet<>();
        for (Message oldMessage : oldMessages) {
            if (oldMessage instanceof MyEmojiMessage) {
                int emojiId = ((MyEmojiMessage) oldMessage).getEmojiId();
                if (emojiToDeleted.contains(emojiId)) {
                    messagesToDeleted.add(oldMessage);
                }
            }
        }
        int stdAns = oldEmojiIdList.length - emojiToDeleted.size();
        // 第三个ensure
//        System.out.println("\nbegin:");
//        System.out.println(newEmojiIdList.length + " " + stdAns);
        assertEquals(newEmojiIdList.length, stdAns);
        // 第四个ensure
//        System.out.println(newEmojiIdList.length + " " + newEmojiHeatList.length);
        assertEquals(newEmojiIdList.length, newEmojiHeatList.length);
        // 第七个ensure
//        System.out.println(newMessages.length + " " + (oldMessages.length - messagesToDeleted.size()));
        assertEquals(newMessages.length, oldMessages.length - messagesToDeleted.size());
        // 第八个ensure
//        System.out.println(myAns + " " + newEmojiIdList.length);
        assertEquals(myAns, newEmojiIdList.length);
        // 第一个ensure
        for (int i = 0; i < oldEmojiIdList.length; i++) {
            if (oldEmojiHeatList[i] >= limit) {
                boolean flag = false;
                for (int k : newEmojiIdList) {
                    if (oldEmojiIdList[i] == k) {
                        flag = true;
                        break;
                    }
                }
                assertEquals(true, flag);
            }
        }
//        System.out.println("first ensure check end");
        // 第二个ensure
        for (int i = 0; i < newEmojiIdList.length; i++) {
            boolean flag = false;
            for (int j = 0; j < oldEmojiIdList.length; j++) {
//                System.out.print(newEmojiIdList[i] + " ");
//                System.out.println(oldEmojiIdList[j]);
                if (newEmojiIdList[i] == oldEmojiIdList[j] && newEmojiHeatList[i] == oldEmojiHeatList[j]) {
                    flag = true;
                    break;
                }
            }
            assertEquals(true, flag);
        }
//        System.out.println("second ensure check end");
        // 第五个ensure + 第六个ensure
        for (int i = 0; i < oldMessages.length; i++) {
             if (!(oldMessages[i] instanceof MyEmojiMessage) ||
                     (oldMessages[i] instanceof MyEmojiMessage &&
                             !emojiToDeleted.contains(((MyEmojiMessage) oldMessages[i]).getEmojiId()))) {
                 boolean flag = false;
                 for (int j = 0; j < newMessages.length; j++) {
                     if (equalMessage(newMessages[j], oldMessages[i])) {
                         flag = true;
                         break;
                     }
                 }
                 assertEquals(true, flag);
             }
        }
        shadowNetwork.deleteColdEmoji(limit);
    }
    
    /*
     * 如果要比较两个Message类是否相等，需要比较其属性，对于基本类型属性使用==比较是否相等，
     * 对于对象类型属性使用equals比较是否相等。
     */
    public boolean equalMessage(Message message1, Message message2) {
        if (!(message1.getId() == message2.getId() && message1.getSocialValue() == message2.getSocialValue() &&
                message1.getType() == message2.getType() && message1.getPerson1().equals(message2.getPerson1()))) {
//            System.out.println(message1.getId() + " " + message2.getId());
            return false;
        }
        if (message1.getType() == 0) {
            if (!(message1.getTag() == null && message2.getTag() == null)) {
                return false;
            }
            if (!(message1.getPerson2().equals(message2.getPerson2()))) {
                return false;
            }
        }
        else {
            if (!(message1.getPerson2() == null && message2.getPerson2() == null)) {
                return false;
            }
            if (!(message1.getTag().equals(message2.getTag()))) {
                return false;
            }
        }
        if (message1 instanceof MyRedEnvelopeMessage) {
            return ((MyRedEnvelopeMessage) message1).getMoney() == ((MyRedEnvelopeMessage) message2).getMoney();
        }
        else if (message1 instanceof MyEmojiMessage) {
            return ((MyEmojiMessage) message1).getEmojiId() == ((MyEmojiMessage) message2).getEmojiId();
        }
        else if (message1 instanceof MyNoticeMessage) {
            return ((MyNoticeMessage) message1).getString().equals(((MyNoticeMessage) message2).getString());
        }
        return true;
    }
    
    public void init() {
        for (int i = 0; i < 100; i++) {
            try {
                network.addPerson(new MyPerson(i, "name" + i, i));
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addPerson(new MyPerson(i, "name" + i, i));
            } catch (Exception e) {
                // do nothing
            }
        }
        for (int i = 0; i < 99; i++) {
            for (int j = i + 1; j < 100; j++) {
                try {
                    network.addRelation(i, j, 199);
                } catch (Exception e) {
                    // do nothing
                }
                try {
                    shadowNetwork.addRelation(i, j, 199);
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
        for (int i = 0; i < 100; i++) {
            MyTag tag = new MyTag(1);
            MyTag shadowTag = new MyTag(1);
            try {
                network.addTag(i, tag);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addTag(i, shadowTag);
            } catch (Exception e) {
                // do nothing
            }
        }
        for (int i = 0; i < 100; i += 2) {
            try {
                network.addPersonToTag(i + 1, i, 1);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addPersonToTag(i + 1, i, 1);
            } catch (Exception e) {
                // do nothing
            }
        }
        for (int i = 0; i < 100; i += 3) {
            try {
                network.addPersonToTag(i + 2, i, 1);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addPersonToTag(i + 2, i, 1);
            } catch (Exception e) {
                // do nothing
            }
        }
        for (int i = 5; i < 85; i++) {
            try {
                network.storeEmojiId(i);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.storeEmojiId(i);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
    
    public void add_person() {
        Random random = new Random();
        double p = random.nextDouble();
        int id;
        if (p < 0.4) {
            id = random.nextInt(300) + 100;
        }
        else if (p < 0.8) {
            id = random.nextInt(1000) - 100;
        }
        else {
            id = random.nextInt();
        }
        String name = genName();
        int age = genAge();
        MyPerson person = new MyPerson(id, name, age);
        MyPerson shadowPerson = new MyPerson(id, name, age);
        try {
            network.addPerson(person);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.addPerson(shadowPerson);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void add_relation() {
        int id1 = genId();
        int id2 = genId();
        int value = genVal();
        try {
            network.addRelation(id1, id2, value);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.addRelation(id1, id2, value);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void modify_relation() {
        int id1 = genId();
        int id2 = genId();
        int mVal = genMVal();
        try {
            network.modifyRelation(id1, id2, mVal);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.modifyRelation(id1, id2, mVal);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_value() {
        int id1 = genId();
        int id2 = genId();
        try {
            network.queryValue(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryValue(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_circle() {
        int id1 = genId();
        int id2 = genId();
        try {
            network.isCircle(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.isCircle(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_block_sum() {
        try {
            network.queryBlockSum();
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryBlockSum();
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_triple_sum() {
        try {
            network.queryTripleSum();
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryTripleSum();
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void add_tag() {
        int personId = genId();
        int tagId = genTagId();
        MyTag tag = new MyTag(tagId);
        MyTag shadowTag = new MyTag(tagId);
        try {
           network.addTag(personId, tag);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.addTag(personId, shadowTag);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void del_tag() {
        int personId = genId();
        int tagId = genTagId();
        try {
            network.delTag(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.delTag(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void add_to_tag() {
        int personId1 = genId();
        int personId2 = genId();
        int tagId = genTagId();
        try {
            network.addPersonToTag(personId1, personId2, tagId);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.addPersonToTag(personId1, personId2, tagId);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void del_from_tag() {
        int personId1 = genId();
        int personId2 = genId();
        int tagId = genTagId();
        try {
            network.delPersonFromTag(personId1, personId2, tagId);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.delPersonFromTag(personId1, personId2, tagId);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_tag_value_sum() {
        int personId = genId();
        int tagId = genTagId();
        try {
            network.queryTagValueSum(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryTagValueSum(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_tag_age_var() {
        int personId = genId();
        int tagId = genTagId();
        try {
            network.queryTagAgeVar(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryTagAgeVar(personId, tagId);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_best_acquaintance() {
        int id = genId();
        try {
            network.queryBestAcquaintance(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryBestAcquaintance(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_shortest_path() {
        int id1 = genId();
        int id2 = genId();
        try {
            network.queryShortestPath(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryShortestPath(id1, id2);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void add_message() {
        int id = genId();
        int socialValue = genSocialValue();
        int id1 = genId();
        Random random = new Random();
        boolean type = random.nextBoolean();
        if (type) {
            int id2 = genId();
            if (!network.containsPerson(id1) ||!network.containsPerson(id2)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            Message message = new MyMessage(id, socialValue, person1, person2);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Person shadowPerson2 = shadowNetwork.getPerson(id2);
            Message shadowMessage = new MyMessage(id, socialValue, shadowPerson1, shadowPerson2);
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
        else {
            int id2 = genTagId();
            if (!network.containsPerson(id1)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                };
                return;
            }
            Person person1 = network.getPerson(id1);
            Tag tag = person1.getTag(id2);
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Tag shadowTag = shadowPerson1.getTag(id2);
            if (tag == null) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Message message = new MyMessage(id, socialValue, person1, tag);
            Message shadowMessage = new MyMessage(id, socialValue, shadowPerson1, shadowTag);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
    
    public void send_message() {
        int id = genId();
        try {
            network.sendMessage(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.sendMessage(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_social_value() {
        int id = genId();
        try {
            network.querySocialValue(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.querySocialValue(id);
        } catch (Exception e) {
            // do nothing
        }
    }

    public void query_received_messages() {
        int id = genId();
        try {
            network.queryReceivedMessages(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryReceivedMessages(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void add_red_envelope_message() {
        int id = genId();
        int money = genMoney();
        int id1 = genId();
        Random random = new Random();
        boolean type = random.nextBoolean();
        if (type) {
            int id2 = genId();
            if (!network.containsPerson(id1) ||!network.containsPerson(id2)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            Message message = new MyRedEnvelopeMessage(id, money, person1, person2);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Person shadowPerson2 = shadowNetwork.getPerson(id2);
            Message shadowMessage = new MyRedEnvelopeMessage(id, money, shadowPerson1, shadowPerson2);
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
        else {
            int id2 = genTagId();
            if (!network.containsPerson(id1)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Tag tag = person1.getTag(id2);
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Tag shadowTag = shadowPerson1.getTag(id2);
            if (tag == null) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Message message = new MyRedEnvelopeMessage(id, money, person1, tag);
            Message shadowMessage = new MyRedEnvelopeMessage(id, money, shadowPerson1, shadowTag);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
    
    public void add_notice_message() {
        int id = genId();
        String string = genName();
        int id1 = genId();
        Random random = new Random();
        boolean type = random.nextBoolean();
        if (type) {
            int id2 = genId();
            if (!network.containsPerson(id1) ||!network.containsPerson(id2)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            Message message = new MyNoticeMessage(id, string, person1, person2);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Person shadowPerson2 = shadowNetwork.getPerson(id2);
            Message shadowMessage = new MyNoticeMessage(id, string, shadowPerson1, shadowPerson2);
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
        else {
            int id2 = genTagId();
            if (!network.containsPerson(id1)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Tag tag = person1.getTag(id2);
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Tag shadowTag = shadowPerson1.getTag(id2);
            if (tag == null) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Message message = new MyNoticeMessage(id, string, person1, tag);
            Message shadowMessage = new MyNoticeMessage(id, string, shadowPerson1, shadowTag);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void clean_notices() {
        int id = genId();
        try {
            network.clearNotices(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.clearNotices(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
//    add_emoji_message id(int) emoji_id(int) type(int)
    public void add_emoji_message() {
        int id = genId();
        int emojiId = genId();
        int id1 = genId();
        Random random = new Random();
        boolean type = random.nextBoolean();
        if (type) {
            int id2 = genId();
            if (!network.containsPerson(id1) ||!network.containsPerson(id2)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            Message message = new MyEmojiMessage(id, emojiId, person1, person2);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Person shadowPerson2 = shadowNetwork.getPerson(id2);
            Message shadowMessage = new MyEmojiMessage(id, emojiId, shadowPerson1, shadowPerson2);
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
        else {
            int id2 = genTagId();
            if (!network.containsPerson(id1)) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Person person1 = network.getPerson(id1);
            Tag tag = person1.getTag(id2);
            Person shadowPerson1 = shadowNetwork.getPerson(id1);
            Tag shadowTag = shadowPerson1.getTag(id2);
            if (tag == null) {
                if (random.nextDouble() < 0.7) {
                    add_message();
                }
                return;
            }
            Message message = new MyEmojiMessage(id, emojiId, person1, tag);
            Message shadowMessage = new MyEmojiMessage(id,emojiId, shadowPerson1, shadowTag);
            try {
                network.addMessage(message);
            } catch (Exception e) {
                // do nothing
            }
            try {
                shadowNetwork.addMessage(shadowMessage);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
    
//    store_emoji_id id(int)
    public void store_emoji_id() {
        int id = genId();
        try {
            network.storeEmojiId(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.storeEmojiId(id);
        } catch (Exception e) {
            // do nothing
        }
    }

//    query_popularity id(int)
    public void query_popularity() {
        int id = genId();
        try {
            network.queryPopularity(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryPopularity(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void query_money() {
        int id = genId();
        try {
            network.queryMoney(id);
        } catch (Exception e) {
            // do nothing
        }
        try {
            shadowNetwork.queryMoney(id);
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public int genId() {
        Random random = new Random();
        double p = random.nextDouble();
        if (p < 0.45) {
            return random.nextInt(10);
        }
        else if (p < 0.85) {
            return random.nextInt(150);
        }
        else if (p < 0.95) {
            return random.nextInt(3000) - 100;
        }
        return random.nextInt();
    }
    
    public int genTagId() {
        Random random = new Random();
        double p = random.nextDouble();
        if (p < 0.3) {
            return 1;
        }
        else if (p < 0.6) {
            return random.nextInt(5);
        }
        else if (p < 0.9) {
            return random.nextInt(100);
        }
        return random.nextInt();
    }
    
    public String genName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int len = random.nextInt(100) + 1;
        for (int i = 0; i < len; ++i) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    public int genAge() {
        Random random = new Random();
        return random.nextInt(200) + 1;
    }
    
    public int genVal() {
        Random random = new Random();
        if (random.nextDouble() < 0.75) {
            return random.nextInt(10) + 1;
        }
        else {
            return random.nextInt(200) + 1;
        }
    }
    
    public int genMVal() {
        Random random = new Random();
        if (random.nextDouble() < 0.75) {
            return random.nextInt(21) - 15;
        }
        else {
            return random.nextInt(401) - 300;
        }
    }
    
    public int genLimit(int i) {
        Random random = new Random();
        if (i < testNum / 3 * 2) {
            if (random.nextDouble() < 0.9) {
                return random.nextInt(2) + 1;
            }
        }
        else if (i < testNum) {
            return random.nextInt(6) - 1;
        }
        return random.nextInt(10);
    }
    
    public int genSocialValue() {
        Random random = new Random();
        return random.nextInt(2001) - 1000;
    }
    
    public int genMoney() {
        Random random = new Random();
        return random.nextInt(1001);
    }
}
