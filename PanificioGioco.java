import javax.swing.*;
import java.awt.*;

public class PanificioGioco extends JFrame {

    private Panificio panificioPanello;

    public PanificioGioco() {
        setTitle("Panificio");

        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setUndecorated(true); // Rimuovere bordi della finestra

        // Thread panettiereThread = new Thread(new Panettiere(panificio));
        // Thread clienteThread = new Thread(new Cliente(panificio));
        // panettiereThread.start();
        // clienteThread.start();

        panificioPanello = new Panificio();
        setContentPane(panificioPanello);
    }

    public static void main(String[] args) {
        PanificioGioco gioco = new PanificioGioco();
        gioco.setVisible(true);
    }
}
