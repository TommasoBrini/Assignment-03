package pcd.ass01.monitor;

public class CyclicBarrier {

    private final int nPartecipants;
    private int counter = 0;
    private Runnable action;
    private boolean isPass = false;

    public CyclicBarrier(int nPartecipants, Runnable action) {
        this.nPartecipants = nPartecipants;
        this.action = action;
    }

    public synchronized void await() throws InterruptedException{
        while(isPass){
            wait();
        }
        counter++;
        if(counter == nPartecipants){
            isPass = true;
            action.run();
            counter = 0;
            notifyAll();
        }
        while(!isPass){
            wait();
        }
        counter++;
        if(counter == nPartecipants){
            reset();
        }
    }

    private void reset() {
        isPass = false;
        counter = 0;
        notifyAll();
    }


}
