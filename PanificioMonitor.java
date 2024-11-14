/**
 * Classe che gestisce l'accesso dei clienti al panificio.
 * Utilizza meccanismi di sincronizzazione per assicurarsi che
 * solo un cliente possa entrare nel panificio alla volta. Quando
 * il panificio è pieno, i clienti devono aspettare finché non si
 * libera un posto. Una volta che un cliente esce, un altro può entrare.
 * 
 * @author Gruppo7
 */

public class PanificioMonitor {
	private final int capacity = 1;
	private int clientiDentro;
	private static boolean clientiEntrano = false;

	/**
     * Verifica se il panificio è libero (cioè se non ci sono clienti dentro).
     * 
     * @return true se il panificio è libero, false altrimenti
     */
	public boolean panificioLibero() {
		return clientiDentro == 0;
	}

	 /**
     * Consente l'ingresso di un cliente nel panificio. Se il panificio è pieno,
     * il cliente dovrà aspettare finché non si libera un posto.
     * 
     * @param nome Il nome del cliente che sta cercando di entrare.
     */
	public synchronized void enterPanificio(String nome) {
		while (clientiDentro == capacity) {
			try {
				System.out.println("Il panificio e' pieno. " + nome + " aspetta.");
				wait(); // Aspetta finché non ci sono posti disponibili
			} catch (InterruptedException e) {
				e.getMessage();
			}
		}
		clientiEntrano = true;
		clientiDentro++;

		System.out.println(nome + " e' entrato al panificio");
	}

	/**
     * Consente l'uscita di un cliente dal panificio. Se il panificio è ora libero,
     * notifica che il panificio è libero.
     * 
     * @param nome Il nome del cliente che sta uscendo.
     */
	public synchronized void exitPanificio(String nome) {
		clientiDentro--;
		System.out.println(nome + " e' uscito dal panificio \n");

		if (panificioLibero()) {
			System.out.println("Il panificio e' libero\n");
			notify();
		}
		clientiEntrano = false;

	}

	/**
     * Restituisce lo stato attuale dell'ingresso dei clienti nel panificio.
     * 
     * @return true se un cliente è entrato nel panificio, false altrimenti
     */
	public static boolean isClientiEntrano() {
		return clientiEntrano;
	}

}
