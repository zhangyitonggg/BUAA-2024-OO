import java.util.ArrayList;
import java.util.Iterator;

public class Team {
    private final ArrayList<Soldier> soldiers;

    public Team() {
        this.soldiers = new ArrayList<>();
    }

    public void addSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }

    public void screen(int standard) {
        Iterator<Soldier> iterator = soldiers.iterator();
        while (iterator.hasNext()) {
            Soldier soldier = iterator.next();
            if (soldier.notQualifiedByStandard(standard)) {
                iterator.remove();
            }
        }
    }

    public void allAddStr(String str) {
        for (Soldier soldier : soldiers) {
            soldier.appendStr2Incantation(str);
        }
    }

    public void allIntercept(int a, int b) {
        for (Soldier soldier : soldiers) {
            soldier.cutIncantation(a, b);
        }
    }

    public Team cloneSelf() {
        Team team = new Team();
        for (Soldier soldier:soldiers) {
            Soldier sol = new Soldier(soldier.getName(),soldier.getIncantation());
            team.soldiers.add(sol);
        }
        return team;
    }

    public void mergeTeam(Team team) {
        for (Soldier soldier : team.soldiers) {
            boolean repeat = false;
            for (Soldier oldSoldier : this.soldiers) {
                if (oldSoldier.equal(soldier)) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                this.soldiers.add(soldier);
            }
        }
    }

    public int getSize() {
        return this.soldiers.size();
    }

    public int getSizeOfHasStr(String str) {
        int count = 0;
        for (Soldier soldier : soldiers) {
            if (soldier.hasString(str)) {
                count++;
            }
        }
        return count;
    }
}

