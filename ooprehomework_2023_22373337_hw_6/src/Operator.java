import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operator {
    private HashMap<Integer,Adventurer> adventurers = new HashMap<>();
    private Log log = new Log();
    private String pattern01 = "([^-]{7})-([^-]+?)-(.+)";
    private String pattern02 = "([^-]{7})-([^@]+?)@([^-]+?)-(.+)";
    private String pattern03 = "([^-]{7})-([^@]+?)@#-(.+)";
    private Pattern pattern1 = Pattern.compile(pattern01);
    private Pattern pattern2 = Pattern.compile(pattern02);
    private Pattern pattern3 = Pattern.compile(pattern03);

    public void basicOp(ArrayList<String> line) {
        int t = Integer.parseInt(line.get(0));
        int advId = Integer.parseInt(line.get(1));
        int nextId;
        Adventurer adventurer = null;
        if (t != 1) {
            adventurer = adventurers.get(advId);
        }
        
        switch (t) {
            case 1:
                adventurers.put(advId,new Adventurer(advId, line.get(2)));
                break;
            case 2:
                adventurer.addBottle(line);
                break;
            case 3:
                adventurer.deleteBottle(Integer.parseInt(line.get(2)));
                break;
            case 4:
                adventurer.addEquipment(line);
                break;
            case 5:
                adventurer.deleteEquipment(Integer.parseInt(line.get(2)));
                break;
            case 6:
                adventurer.upStar(Integer.parseInt(line.get(2)));
                break;
            case 7:
                nextId = Integer.parseInt(line.get(2));
                long price = Long.parseLong(line.get(5));
                adventurer.addFood(nextId,line.get(3),Integer.parseInt(line.get(4)),price);
                break;
            case 8:
                adventurer.deleteFood(Integer.parseInt(line.get(2)));
                break;
            case 9:
                adventurer.carryEquipment(Integer.parseInt(line.get(2)));
                break;
            case 10:
                adventurer.carryBottle(Integer.parseInt(line.get(2)));
                break;
            case 11:
                adventurer.carryFood(Integer.parseInt(line.get(2)));
                break;
            case 12:
                adventurer.drinkBottle(line.get(2));
                break;
            case 13:
                adventurer.eatFood(line.get(2));
                break;
            default: break;
        }
    }
    
    public void battleOp(ArrayList<ArrayList<String>> fightBlock) {
        System.out.println("Enter Fight Mode");
        int m = Integer.parseInt(fightBlock.get(0).get(1));
        int k = Integer.parseInt(fightBlock.get(0).get(2));
        HashMap<String,Adventurer> fightPersons = new HashMap<>();   //**lianggerongqi
        ArrayList<Adventurer> fightPersonsInOrder = new ArrayList<>();
        for (int i = 0;i < m;i++) {
            String name = fightBlock.get(0).get(3 + i);
            for (Adventurer adventurer: adventurers.values()) {
                if (name.equals(adventurer.getName())) {
                    fightPersons.put(name,adventurer);
                    fightPersonsInOrder.add(adventurer);
                    break;
                }
            }
        }
        for (int i = 0;i < k;i++) {
            Matcher matcher1 = pattern1.matcher(fightBlock.get(1 + i).get(0));
            Matcher matcher2 = pattern2.matcher(fightBlock.get(1 + i).get(0));
            Matcher matcher3 = pattern3.matcher(fightBlock.get(1 + i).get(0));
            if (matcher3.find()) {
                String name = matcher3.group(3);
                if (fightPersons.containsKey(matcher3.group(2))) {
                    Equipment equip = fightPersons.get(matcher3.group(2)).getEquipment(name);
                    if (equip != null) {
                        fightPersons.get(matcher3.group(2)).attackAll(fightPersonsInOrder,equip);
                        //inorder
                        log.attackAll(matcher3.group(1),matcher3.group(2),fightPersons,name);
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
            else if (matcher2.find()) {
                String data = matcher2.group(1);
                String name1 = matcher2.group(2);
                String name2 = matcher2.group(3);
                String name = matcher2.group(4);
                if (fightPersons.containsKey(name1) && fightPersons.containsKey(name2)) {
                    Equipment equip = fightPersons.get(name1).getEquipment(matcher2.group(4));
                    if (equip != null) {
                        fightPersons.get(name1).attackOne(fightPersons.get(name2),equip);
                        log.attackOne(data,fightPersons.get(name1),fightPersons.get(name2),name);
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
            else if (matcher1.find()) {
                String name = matcher1.group(3);
                if (fightPersons.containsKey(matcher1.group(2))) {
                    if (fightPersons.get(matcher1.group(2)).getBottle(name)) {
                        log.drinkBottle(matcher1.group(1),matcher1.group(2),name);
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
        }
    }
    
    public void logOp(ArrayList<String> line) {
        int t = Integer.parseInt(line.get(0));
        switch (t) {
            case 15:
                log.findDate(line.get(1));
                break;
            case 16:
                log.findAttack(Integer.parseInt(line.get(1)));
                break;
            case 17:
                log.findBeAttack(Integer.parseInt(line.get(1)));
                break;
            default: break;
        }
    }
    
    public void employOp(ArrayList<String> line) {
        int t = Integer.parseInt(line.get(0));
        int id = Integer.parseInt(line.get(1));
        Adventurer adv = adventurers.get(id);
        switch (t) {
            case 18:
                int employeeId = Integer.parseInt(line.get(2));
                adv.employAdventurer(employeeId,adventurers.get(employeeId));
                break;
            case 19:
                System.out.println(adv.getComCount() + " " + adv.getPrice());
                break;
            case 20:
                System.out.println(adv.getComMax());
                break;
            case 21:
                int comId = Integer.parseInt(line.get(2));
                adv.findClassType(comId);
                break;
            default: break;
        }
    }
}
