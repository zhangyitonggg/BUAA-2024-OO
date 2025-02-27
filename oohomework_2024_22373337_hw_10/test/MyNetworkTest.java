import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.main.Person;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MyNetworkTest {
    private final MyNetwork network;
    private final MyNetwork shadowNetwork;
    
    public MyNetworkTest() {
        network = new MyNetwork();
        shadowNetwork = new MyNetwork();
    }
    
    
    @Test
    public void testQueryCoupleSum() {
        init();
        int testNum = 10000;
        for (int i = 0; i < testNum; ++i) {
            Random random = new Random();
            boolean b = random.nextBoolean();
            if (b) {
                double p = random.nextDouble();
                if (p < 0.2) {
                    add_person();
                }
                else if (p < 0.65) {
                    add_relation();
                }
                else if (p < 0.85) {
                    modify_relation();
                }
                else if (p < 0.9) {
                    query_value();
                }
                else if (p < 0.94) {
                    query_circle();
                }
                else if (p < 0.96) {
                    query_block_sum();
                }
                else {
                    query_triple_sum();
                }
            }
            else {
                double p = random.nextDouble();
                if (p < 0.25) {
                    add_tag();
                }
                else if (p < 0.35) {
                    del_tag();
                }
                else if (p < 0.72) {
                    add_to_tag();
                }
                else if (p < 0.91) {
                    del_from_tag();
                }
                else if (p < 0.93) {
                    query_tag_value_sum();
                }
                else if (p < 0.95) {
                    query_tag_age_var();
                }
                else if (p < 0.98) {
                    query_best_acquaintance();
                }
                else {
                    query_shortest_path();
                }
            }
            judgeOneQueryCoupleSum();
        }
        System.out.println(network.queryCoupleSum());
    }
    
    public void judgeOneQueryCoupleSum() {
        // 获得正常值
        Person[] shadowPersons = shadowNetwork.getPersons();
        int cnt = 0;
        for (int i = 0; i < shadowPersons.length; ++i) {
            int bestId1 = 0;
            try {
                bestId1 = shadowNetwork.queryBestAcquaintance(shadowPersons[i].getId());
            } catch (PersonIdNotFoundException | AcquaintanceNotFoundException e) {
                continue;
            }
            int bestId2 = 0;
            try {
                bestId2 = shadowNetwork.queryBestAcquaintance(bestId1);
            } catch (PersonIdNotFoundException | AcquaintanceNotFoundException e) {
                continue;
            }
            if (shadowPersons[i].getId() == bestId2) {
                ++cnt;
            }
        }
        cnt = cnt / 2;
        // 比较结果是否对
        assertEquals(cnt, network.queryCoupleSum());
        // 比较是否pure
        Person[] persons = network.getPersons();
        assertEquals(persons.length, shadowPersons.length);
        for (Person person : persons) {
            boolean flag = false;
            for (Person shadowPerson : shadowPersons) {
                if (((MyPerson) person).strictEquals(shadowPerson)) {
                    flag = true;
                    break;
                }
            }
            assertEquals(true, flag);
        }
    }
    
    public void init() {
        for (int i = 0; i < 10; ++i) {
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
            judgeOneQueryCoupleSum();
        }
    }
    
    public void add_person() {
        int id = genId();
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
        int tagId = genId();
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
        int tagId = genId();
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
        int tagId = genId();
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
        int tagId = genId();
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
        int tagId = genId();
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
        int tagId = genId();
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
    
    public int genId() {
        Random random = new Random();
        double p = random.nextDouble();
        if (p < 0.2) {
            return random.nextInt(10);
        }
        else if (p < 0.8) {
            return random.nextInt(300);
        }
        else if (p < 0.95) {
            return random.nextInt(3000) - 100;
        }
        return random.nextInt();
    }
    
    public String genName() {
        Random random = new Random();
        return "name" + random.nextInt(1000);
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
    
}
