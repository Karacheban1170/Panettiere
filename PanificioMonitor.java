public class PanificioMonitor {
	private final int capacity = 1;
	private int clientiDentro;
	private static boolean clientiEntrano = false;

	public boolean panificioLibero() {
		return clientiDentro == 0;
	}

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

	public synchronized void exitPanificio(String nome) {
		clientiDentro--;
		System.out.println(nome + " e' uscito dal panificio \n");

		if (panificioLibero()) {
			System.out.println("Il panificio e' libero\n");
			notify();
		}
		clientiEntrano = false;

	}

	public static boolean isClientiEntrano() {
		return clientiEntrano;
	}

}
