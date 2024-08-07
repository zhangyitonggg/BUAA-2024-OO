public class Store {
    private int appleCount; // 3 元 1 个
    private int bananaCount; // 2 元 1 个
    private static final int APPLE_PRICE = 3;
    private static final int BANANA_PRICE = 2;

    public Store(int appleCount, int bananaCount) { // 该类的构造器
        this.appleCount = appleCount;
        this.bananaCount = bananaCount;
    }

    public void trySellOut(Child child, String goal) {
        if (goal.equals("apple") && appleCount > 0 && child.getMoney() >= APPLE_PRICE) {
            appleCount--;
            child.addOneFruit(goal);
            child.subMoney(APPLE_PRICE);
            System.out.println("buy " + goal + " ok!");
        } else if (goal.equals("banana") && bananaCount > 0 && child.getMoney() >= BANANA_PRICE) {
            bananaCount--;
            child.addOneFruit(goal);
            child.subMoney(BANANA_PRICE);
            System.out.println("buy " + goal + " ok!");
        } else {
            System.out.println("buy " + goal + " failed!");
        }
    }
}
