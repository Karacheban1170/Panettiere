import java.util.LinkedList;
import java.util.Queue;

public class Panificio {
    private final int CAPACITA = 5;  // Massimo numero di pani nel panificio
    private final Queue<String> pani = new LinkedList<>();

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
