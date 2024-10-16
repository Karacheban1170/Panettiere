import java.util.LinkedList;
import java.util.Queue;

public class Cliente implements Runnable {
    private Panificio panificio;
    private final int CAPACITA = 5;  // Massimo numero di pani nel panificio
    private final Queue<String> pani = new LinkedList<>();

    public Cliente(Panificio panificio) {
        this.panificio = panificio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Il cliente acquista pane
                acquistarePane();
                // Attende un po' prima di ritornare
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metodo sincronizzato per acquistare pane
    public synchronized void acquistarePane() throws InterruptedException {
        while (pani.isEmpty()) {
            // Se non c'è pane, il cliente attende
            System.out.println("Niente pane! Attesa del cliente...");
            wait();
        }
        // Il cliente prende un pane
        pani.poll();
        System.out.println("Cliente: Pane acquistato!");
        notifyAll();
    }
}