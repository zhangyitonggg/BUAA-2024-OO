public class Child {
    private int money;
    private int appleCount;
    private int bananaCount;

    public Child(int money) { // 该类的构造器
        this.money = money;
        this.appleCount = 0;
        this.bananaCount = 0;
    }

    public void subMoney(int count) {
        money -= count;
    }

    public  void addOneFruit(String goal) {
        if (goal.equals("apple")) {
            appleCount++;
        } else if (goal.equals("banana")) {
            bananaCount++;
        }
    }

    public void eatOneFruit(String goal) {
        if (goal.equals("apple") && appleCount > 0) {
            System.out.println("eat " + goal + " ok!");
            appleCount--;
        } else if (goal.equals("banana") && bananaCount > 0) {
            System.out.println("eat " + goal + " ok!");
            bananaCount--;
        } else {
            System.out.println("eat " + goal + " failed!");
        }
    }

    public void buyFromStore(String goal, Store store) {
        if (goal.equals("apple")) {
            store.trySellOut(this, "apple");
        } else if (goal.equals("banana")) {
            store.trySellOut(this, "banana");
        } else {
            System.out.println("buy " + goal + " failed!");
        }
    }

    public int getMoney() {
        return money;
    }
}
