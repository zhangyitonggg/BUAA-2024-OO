import java.util.ArrayList;
import java.util.HashMap;

public class Log {
    private HashMap<String, ArrayList<String>> log15 = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> log16 = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> log17 = new HashMap<>();
    
    public void drinkBottle(String data,String advName,String name) {
        String str = data + " " + advName + " used " + name;
        if (log15.containsKey(data)) {
            log15.get(data).add(str);
        }
        else {
            log15.put(data,new ArrayList<>());
            log15.get(data).add(str);
        }
    }
    
    public void attackOne(String data,Adventurer adv1,Adventurer adv2,String name) {
        String str = data + " " + adv1.getName() + " attacked " + adv2.getName() + " with " + name;
        if (log15.containsKey(data)) {
            log15.get(data).add(str);
        }
        else {
            log15.put(data,new ArrayList<>());
            log15.get(data).add(str);
        }
        if (log16.containsKey(adv1.getId())) {
            log16.get(adv1.getId()).add(str);
        }
        else {
            log16.put(adv1.getId(),new ArrayList<>());
            log16.get(adv1.getId()).add(str);
        }
        if (log17.containsKey(adv2.getId())) {
            log17.get(adv2.getId()).add(str);
        }
        else {
            log17.put(adv2.getId(),new ArrayList<>());
            log17.get(adv2.getId()).add(str);
        }
    }
    
    public void attackAll(String data, String advName, HashMap<String,Adventurer> adv, String name)
    {
        String str = data + " " + advName + " AOE-attacked with " + name;
        if (log15.containsKey(data)) {
            log15.get(data).add(str);
        }
        else {
            log15.put(data,new ArrayList<>());
            log15.get(data).add(str);
        }
        if (log16.containsKey(adv.get(advName).getId())) {
            log16.get(adv.get(advName).getId()).add(str);
        }
        else {
            log16.put(adv.get(advName).getId(),new ArrayList<>());
            log16.get(adv.get(advName).getId()).add(str);
        }
        for (Adventurer fighter:adv.values()) {
            if (!fighter.getName().equals(advName)) {
                if (log17.containsKey(fighter.getId())) {
                    log17.get(fighter.getId()).add(str);
                }
                else {
                    log17.put(fighter.getId(),new ArrayList<>());
                    log17.get(fighter.getId()).add(str);
                }
            }
        }
    }
    
    public void findDate(String data) {
        if (log15.containsKey(data)) {
            for (String log: log15.get(data)) {
                System.out.println(log);
            }
        }
        else {
            System.out.println("No Matched Log");
        }
    }
    
    public void findAttack(int id) {
        if (log16.containsKey(id)) {
            for (String log: log16.get(id)) {
                System.out.println(log);
            }
        }
        else {
            System.out.println("No Matched Log");
        }
    }
    
    public void findBeAttack(int id) {
        if (log17.containsKey(id)) {
            for (String log: log17.get(id)) {
                System.out.println(log);
            }
        }
        else {
            System.out.println("No Matched Log");
        }
    }
}
