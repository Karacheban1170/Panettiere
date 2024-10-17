public class PanificioMonitor {
	private int capacity = 1;
	private int clientiDentro;

	public boolean panificioLibero() {
		return clientiDentro == 0;
	}

	public synchronized void enterPanificio(String nome) {
		while (clientiDentro == capacity) {
			try {
				
				wait();
			} catch (InterruptedException e) {	
				System.out.println(e);
			}
		}

		clientiDentro++;
		System.out.println("Cliente " + nome + " e' entrato al panificio");
	}

	public synchronized void exitPanificio(String nome) {
		clientiDentro--;
		System.out.println("Cliente " + nome +" e' uscito dal panificio");

		if (panificioLibero()) {
			System.out.println("Il panificio e' libero");
			notifyAll();
		}

	}

}
