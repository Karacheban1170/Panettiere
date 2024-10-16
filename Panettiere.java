import java.util.LinkedList;
import java.util.Queue;

public class Panettiere implements Runnable {
    private final Panificio panificio;
    
    private final int CAPACITA = 5;  // Massimo numero di pani nel panificio
    private final Queue<String> pani = new LinkedList<>();

    public Panettiere(Panificio panificio) {
        this.panificio = panificio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Il panettiere produce pane
                produrrePane();
                // Attende un po' prima di produrre altro pane
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metodo sincronizzato per produrre pane
    public synchronized void produrrePane() throws InterruptedException {
        while (pani.size() == CAPACITA) {
            // Se il panificio è pieno, il panettiere attende
            System.out.println("Panificio pieno! Attesa...");
            wait();
        }
        // Aggiungiamo pane e notifichiamo i clienti
        pani.add("Pane");
        System.out.println("Panettiere: Pane prodotto!");
        notifyAll();
    }
}


