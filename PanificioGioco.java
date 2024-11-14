/**
 * Classe principale che avvia il gioco "Panificio".
 * Questo programma crea e visualizza la finestra del gioco,
 * utilizzando la classe PanificioFrame per gestire l'interfaccia utente.
 * 
 * @author Gruppo7
 */
public class PanificioGioco {
    /**
     * Punto di ingresso principale del gioco.
     * Crea un'istanza della finestra del gioco e la rende visibile.
     */
    public static void main(String[] args) {
        PanificioFrame panificio = new PanificioFrame();
        panificio.setVisible(true);
    }
}