import java.util.HashMap;
import java.util.Scanner;

public class Demon {
    private final Scanner scanner;
    private final HashMap<Integer, Team> teams;
    private final HashMap<String, Soldier> unformedSoldiers;

    public Demon(Scanner scanner) {
        this.scanner = scanner;
        this.teams = new HashMap<>();
        this.unformedSoldiers = new HashMap<>();
    }

    public void solve() {
        int num = scanner.nextInt();
        for (int i = 0; i < num; i++) {
            int mode = scanner.nextInt();
            if (mode == 1) {
                makeSoldier();
            } else if (mode == 2) {
                formSoldier();
            } else if (mode == 3) {
                copyTeam();
            } else if (mode == 4) {
                screenTeam();
            } else if (mode == 5) {
                mergeId2Id();
            } else if (mode == 6) {
                teamAddStr();
            } else if (mode == 7) {
                teamInterceptStr();
            } else if (mode == 8) {
                getTeamSize();
            } else if (mode == 9) {
                getTeamSizeWithStr();
            } else {
                getUnformedSoldierNum();
            }
        }
    }

    private void makeSoldier() {
        String str1 = scanner.next();
        String str2 = scanner.next();
        unformedSoldiers.put(str1, new Soldier(str1, str2));
    }

    private void formSoldier() {
        int id = scanner.nextInt();
        int num = scanner.nextInt();
        Team team = new Team();
        for (int i = 0; i < num; i++) {
            String name = scanner.next();
            Soldier soldier = unformedSoldiers.get(name);
            team.addSoldier(soldier);
            unformedSoldiers.remove(name,soldier);
        }
        teams.put(id, team);
    }

    private void copyTeam() {
        int id1 = scanner.nextInt();
        int id2 = scanner.nextInt();
        Team team = teams.get(id1);
        Team newTeam = team.cloneSelf();
        teams.put(id2,newTeam);
    }

    private void screenTeam() {
        int id = scanner.nextInt();
        int standard = scanner.nextInt();
        teams.get(id).screen(standard);
    }

    private void mergeId2Id() {
        int id1 = scanner.nextInt();
        int id2 = scanner.nextInt();
        Team team1 = teams.get(id1);
        Team team2 = teams.get(id2);
        team1.mergeTeam(team2);
    }

    private void teamAddStr() {
        int id = scanner.nextInt();
        String str = scanner.next();
        teams.get(id).allAddStr(str);
    }

    private void teamInterceptStr() {
        int id = scanner.nextInt();
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        teams.get(id).allIntercept(a, b);
    }

    private void getTeamSize() {
        int id = scanner.nextInt();
        System.out.println(teams.get(id).getSize());
    }

    private void getTeamSizeWithStr() {
        int id = scanner.nextInt();
        String str = scanner.next();
        System.out.println(teams.get(id).getSizeOfHasStr(str));
    }

    private void getUnformedSoldierNum() {
        System.out.println(unformedSoldiers.size());
    }

}

