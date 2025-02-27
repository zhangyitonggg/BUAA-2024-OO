public class Coordinate {
    private boolean isOccupied;
    
    public Coordinate() {
        this.isOccupied = false;
    }

    // 抢夺换乘层
    public synchronized void robTransferFloor() {
        if (isOccupied) {
            try {
                wait(); // 一定能被notifyAll
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        isOccupied = true;
    }
  
    // 释放换乘层
    public synchronized void releaseTransferFloor() {
        isOccupied = false;
        notifyAll();
    }
}