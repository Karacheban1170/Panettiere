import javax.swing.*;

public class PanificioGioco extends JFrame {

    private final static PanificioGUI panificioPanello = new PanificioGUI();;

    public PanificioGioco() {
        setTitle("Panificio");
        setSize(1366, 768);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setUndecorated(true); // Rimuovere bordi della finestra

        setContentPane(panificioPanello);
    }

    public static void main(String[] args) {
        PanificioGioco gioco = new PanificioGioco();

        gioco.setVisible(true);

        panificioPanello.avviaClienti();
        
    }
}
