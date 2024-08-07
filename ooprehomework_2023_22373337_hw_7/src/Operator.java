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
                adventurer.sellBottle(Integer.parseInt(line.get(2)));
                break;
            case 4:
                adventurer.addEquipment(line);
                break;
            case 5:
                adventurer.sellEquipment(Integer.parseInt(line.get(2)));
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
                adventurer.sellFood(Integer.parseInt(line.get(2)));
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
        ArrayList<Integer> allHitPoint = new ArrayList<>();
        for (int i = 0;i < m;i++) {
            for (Adventurer adventurer: adventurers.values()) {
                if (fightBlock.get(0).get(3 + i).equals(adventurer.getName())) {
                    fightPersons.put(fightBlock.get(0).get(3 + i),adventurer);
                    fightPersonsInOrder.add(adventurer);
                    allHitPoint.add(adventurer.getHitPoint());
                    break;
                }
            }
        }
        for (int i = 0;i < k;i++) {
            Matcher mt1 = pattern1.matcher(fightBlock.get(1 + i).get(0));
            Matcher mt2 = pattern2.matcher(fightBlock.get(1 + i).get(0));
            Matcher mt3 = pattern3.matcher(fightBlock.get(1 + i).get(0));
            if (mt3.find()) {
                if (fightPersons.containsKey(mt3.group(2))) {
                    Equipment equip = fightPersons.get(mt3.group(2)).getEquipment(mt3.group(3));
                    if (equip != null) {
                        fightPersons.get(mt3.group(2)).attackAll(fightPersonsInOrder,equip);
                        log.attackAll(mt3.group(1),mt3.group(2),fightPersons,mt3.group(3));
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
            else if (mt2.find()) {
                String nm1 = mt2.group(2);
                String nm2 = mt2.group(3);
                String nm = mt2.group(4);
                if (fightPersons.containsKey(nm1) && fightPersons.containsKey(nm2)) {
                    Equipment equip = fightPersons.get(nm1).getEquipment(mt2.group(4));
                    if (equip != null) {
                        fightPersons.get(nm1).attackOne(fightPersons.get(nm2),equip);
                        log.attackOne(mt2.group(1),fightPersons.get(nm1),fightPersons.get(nm2),nm);
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
            else if (mt1.find()) {
                if (fightPersons.containsKey(mt1.group(2))) {
                    if (fightPersons.get(mt1.group(2)).getBottle(mt1.group(3))) {
                        log.drinkBottle(mt1.group(1),mt1.group(2),mt1.group(3));
                        continue;
                    }
                }
                System.out.println("Fight log error");
            }
        }
        for (int i = 0;i < allHitPoint.size();i++) {
            fightPersonsInOrder.get(i).askHelp(allHitPoint.get(i));
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
                long price = adv.getPrice() - adv.getMoney();
                System.out.println(adv.getComCount() + " " + price);
                break;
            case 20:
                System.out.println(adv.getComMax());
                break;
            case 21:
                int comId = Integer.parseInt(line.get(2));
                adv.findClassType(comId);
                break;
            case 22:
                adv.sellAll();
                break;
            case 23:
                adv.buyObject(line);
                break;
            default: break;
        }
    }
}
