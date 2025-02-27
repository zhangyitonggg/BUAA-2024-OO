public interface Strategy {
    public int getCommand(int curPeoNum, int capacity, int curFloor, int direction,
                          boolean isA, int transferFloor);
}
