import java.util.LinkedList;

public class Cliente implements Runnable {
    private final int CAPACITA = 5;
    private final LinkedList<String> pani = new LinkedList<>();

    @Override
    public void run() {
        try {
            while (true) {
                acquistarePane();
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void acquistarePane() throws InterruptedException {
        while (pani.isEmpty()) {
            System.out.println("Niente pane! Attesa del cliente...");
            wait();
        }
        pani.poll();
        System.out.println("Cliente: Pane acquistato!");
        notifyAll();
    }
}