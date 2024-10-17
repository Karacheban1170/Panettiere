public class PanificioMonitor {
	private final int capacity = 1;
	private int clientiDentro;

	public boolean panificioLibero() {
		return clientiDentro == 0;
	}

	public synchronized void enterPanificio(String nome) {
		while (clientiDentro == capacity) {
			try {
				System.out.println("Il panificio e' pieno. Cliente " + nome + " aspetta.");
				wait(); // Aspetta finché non ci sono posti disponibili
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		clientiDentro++;
		System.out.println("Cliente " + nome + " e' entrato al panificio");
	}
	

	public synchronized void exitPanificio(String nome) {
		clientiDentro--;
		System.out.println("Cliente " + nome +" e' uscito dal panificio \n");

		if (panificioLibero()) {
			System.out.println("Il panificio e' libero");
			notify();
		}

	}

}
