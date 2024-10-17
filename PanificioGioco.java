import javax.swing.*;

public class PanificioGioco extends JFrame {

    private PanificioGUI panificioPanello;

    public PanificioGioco() {
        setTitle("Panificio");

        setSize(800, 600);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setUndecorated(true); // Rimuovere bordi della finestra

        panificioPanello = new PanificioGUI();
        setContentPane(panificioPanello);
    }

    public static void main(String[] args) {
        PanificioGioco gioco = new PanificioGioco();
        gioco.setVisible(true);
    }
}
