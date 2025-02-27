import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.main.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;


import static org.junit.Assert.assertEquals;

public class MyNetworkTest {
    
    @Test
    public void testQueryTripleSum() throws PersonIdNotFoundException, EqualRelationException {
        MyNetwork myNetwork = new MyNetwork();
        for (int i = 0; i <= 15; ++i) {
            try {
                myNetwork.addPerson(new MyPerson(i, "name" + i, 199));
            } catch (EqualPersonIdException e) { }
        }
        // test1
        try {
            myNetwork.addRelation(1, 2, 100);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.addRelation(2, 3, 100);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.addRelation(1, 3, 100);
        } catch (Exception e) {}
        test(myNetwork);
        // test2
        try {
            myNetwork.addRelation(3, 4, 100);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.addRelation(4, 2, 100);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.modifyRelation(4, 2, -99);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.modifyRelation(4, 2, -1);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.queryBlockSum();
        } catch (Exception e) { }
        test(myNetwork);
        try {
            myNetwork.isCircle(1, 2);
        } catch (Exception e) { }
        test(myNetwork);
        // test3
        try {
            myNetwork.addRelation(30, 3000, 1);
        } catch (Exception e) {}
        test(myNetwork);
        try {
            myNetwork.modifyRelation(2, 3210, 2);
        } catch (Exception e) {}
        // many tests
        Random random = new Random();
        for (int i = 0; i < 12000; ++i) {
            int id1 = random.nextInt(10);
            int id2 = random.nextInt(10);
            int value;
            if (random.nextBoolean()) {
                value = random.nextInt(5) + 1;
                try {
                    myNetwork.addRelation(id1, id2, value);
                } catch (Exception e) {}
            }
            else {
                value = random.nextInt(15) - 10;
                try {
                    myNetwork.modifyRelation(id1, id2, value);
                } catch (Exception e) {}
            }
            //System.out.print(i + " " + id1 + " " + id2 + " " + value);
            test(myNetwork);
        }
    }
    
    public void test(MyNetwork myNetwork) {
        Person[] oldPersons = myNetwork.getPersons();

        int cnt = 0;
        for (int i = 0; i < oldPersons.length; ++i) {
            for (int j = i + 1; j < oldPersons.length; ++j) {
                for (int k = j + 1; k < oldPersons.length; ++k) {
                    if (oldPersons[i].isLinked(oldPersons[j]) &&
                        oldPersons[j].isLinked(oldPersons[k]) &&
                        oldPersons[k].isLinked(oldPersons[i])) {
                        ++cnt;
                    }
                }
            }
        }
        //System.out.println(" " + cnt);
        assertEquals(cnt, myNetwork.queryTripleSum());
        // 开摆！aaa
        Person[] newPersons = myNetwork.getPersons();
        assertEquals(oldPersons.length, newPersons.length);
        for (int i = 0; i < oldPersons.length; ++i) {
            assertEquals(true, ((MyPerson) oldPersons[i]).strictEquals(newPersons[i]));
        }
    }
    
//    private final MyNetwork myNetwork;
    
//    public MyNetworkTest(MyNetwork myNetwork) throws Exception {
//        this.myNetwork = myNetwork;
//    }
//
//    @Parameters
//    public static Collection prepareDate() throws Exception {
//        int testNum = 30;
//        Object[][] object = new Object[testNum][];
//        for (int i = 0; i < testNum; ++i) {
//            object[i] = new Object[]{generateMyNetwork()};
//        }
//        return Arrays.asList(object);
//    }
//
//    @Test
//    public void testQueryTripleSum() {
//        Person[] oldPersons = myNetwork.getPersons();
//
//        int cnt = 0;
//        for (int i = 0; i < oldPersons.length; ++i) {
//            for (int j = i + 1; j < oldPersons.length; ++j) {
//                for (int k = j + 1; k < oldPersons.length; ++k) {
//                    if (oldPersons[i].isLinked(oldPersons[j]) &&
//                        oldPersons[j].isLinked(oldPersons[k]) &&
//                        oldPersons[k].isLinked(oldPersons[i])) {
//                        ++cnt;
//                    }
//                }
//            }
//        }
//        assertEquals(cnt, myNetwork.queryTripleSum());
//        // 开摆！
//        Person[] newPersons = myNetwork.getPersons();
//        assertEquals(oldPersons.length, newPersons.length);
//        for (int i = 0; i < oldPersons.length; ++i) {
//            assertEquals(true, ((MyPerson) oldPersons[i]).strictEquals(newPersons[i]));
//        }
//    }
    
    public static MyNetwork generateMyNetwork() {
        Random random = new Random();
        
        MyNetwork myNetwork = new MyNetwork();
        int cmdNumMax = 10000;
        int cmdNum = random.nextInt(cmdNumMax) + 1;
        
        for (int i = 0; i < cmdNum; ++i) {
            double p = random.nextDouble();
            if (p < 0.37) {   // addPerson
                int id = generateId();
                String name = generateName();
                int age = generateAge();
                Person person = new MyPerson(id, name, age);
                try {
                    myNetwork.addPerson(person);
                } catch (Exception e) { }
            }
            else if (p < 0.85) { // addRelation
                int id1 = generateId();
                int id2 = generateId();
                int value = generateValue();
//                System.out.println(id1 + " " + id2 + " " + value);
                try {
                    myNetwork.addRelation(id1, id2, value);
                } catch (Exception e) { }
            }
            else if (p < 0.90) { // modifyRelation
                int id1 = generateId();
                int id2 = generateId();
                int modifyValue = generateModifyValue();
                try {
                    myNetwork.addRelation(id1, id2, modifyValue);
                } catch (Exception e) { }
            }
            else if (p < 0.93) { // queryValue
                int id1 = generateId();
                int id2 = generateId();
                try {
                    myNetwork.queryValue(id1, id2);
                } catch (Exception e) { }
            }
            else if (p < 0.96) { // queryCircle
                int id1 = generateId();
                int id2 = generateId();
                try {
                    myNetwork.isCircle(id1, id2);
                } catch (Exception e) { }
            }
            else if (p < 0.98) { // queryBlockSum
                myNetwork.queryBlockSum();
            }
            else {
                myNetwork.queryTripleSum();
            }
        }
        return myNetwork;
    }
    
    public static int generateId() {
        Random random = new Random();
        if (random.nextDouble() < 0.75) {
            return random.nextInt(30) - 4; // -4 5
        }
        else {
            return random.nextInt(1145142206) - 999;
        }
    }
    
    public static String generateName() {
        Random random = new Random();
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        if (random.nextBoolean()) {
            return "name" + random.nextInt(10);
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < random.nextInt(10) + 1; ++i) {
                int index = random.nextInt(pool.length());
                sb.append(pool.charAt(index));
            }
            return sb.toString();
        }
    }
    
    public static int generateAge() {
        Random random = new Random();
        return random.nextInt(200) + 1;
    }
    
    public static int generateValue() {
        Random random = new Random();
        if (random.nextDouble() < 0.75) {
            return random.nextInt(10) + 1;
        }
        else {
            return random.nextInt(200) + 1;
        }
    }
    
    public static int generateModifyValue() {
        Random random = new Random();
        if (random.nextDouble() < 0.75) {
            return random.nextInt(21) - 10;
        }
        else {
            return random.nextInt(401) - 200;
        }
    }
}