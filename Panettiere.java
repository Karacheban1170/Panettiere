import java.util.LinkedList;

public class Panettiere implements Runnable {

    private final int CAPACITA = 5; 
    private final LinkedList<String> pani = new LinkedList<>();

    @Override
    public void run() {
        try {
            while (true) {
                produrrePane();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void produrrePane() throws InterruptedException {
        while (pani.size() == CAPACITA) {
            System.out.println("Panificio pieno! Attesa...");
            wait();
        }

        pani.add("Pane");
        System.out.println("Panettiere: Pane prodotto!");
        notifyAll();
    }
}
