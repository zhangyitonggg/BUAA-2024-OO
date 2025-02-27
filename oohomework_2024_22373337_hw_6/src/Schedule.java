public class Schedule extends Thread {
    private final MainQueue mainQueue;
    private final AllElevators allElevators;
    
    public Schedule() {
        this.mainQueue = MainQueue.getInstance();
        this.allElevators = AllElevators.getInstance();
    }
    
    @Override
    public void run() {
        while (true) {
            if (mainQueue.isRealEnd()) {
                allElevators.setEnd();
                return;
            }
            Person person = mainQueue.transferFromMainToSub();
            if (person == null) {
                continue;
            }
            allElevators.dispatch(person);
        }
    }
}
