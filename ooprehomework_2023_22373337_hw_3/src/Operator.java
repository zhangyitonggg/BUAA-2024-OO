import java.util.ArrayList;
import java.util.HashMap;

public class Operator {
    private HashMap<Integer,Adventurer> adventurers = new HashMap<>();
    
    public void op(ArrayList<String> line) {
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
                nextId = Integer.parseInt(line.get(2));
                adventurer.addBottle(nextId,line.get(3),Integer.parseInt(line.get(4)));
                break;
            case 3:
                adventurer.deleteBottle(Integer.parseInt(line.get(2)));
                break;
            case 4:
                nextId = Integer.parseInt(line.get(2));
                adventurer.addEquipment(nextId,line.get(3),Integer.parseInt(line.get(4)));
                break;
            case 5:
                adventurer.deleteEquipment(Integer.parseInt(line.get(2)));
                break;
            case 6:
                adventurer.upStar(Integer.parseInt(line.get(2)));
                break;
            case 7:
                nextId = Integer.parseInt(line.get(2));
                adventurer.addFood(nextId,line.get(3),Integer.parseInt(line.get(4)));
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
}
