import javax.swing.*;
import java.awt.*;

public class PanificioGioco extends JFrame {

    private Panificio panificio;

    public PanificioGioco() {
        setTitle("Panificio");

        setSize(800, 600);

        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setUndecorated(true); // Rimuovere bordi della finestra

        panificio = new Panificio();

        // Thread panettiereThread = new Thread(new Panettiere(panificio));
        // Thread clienteThread = new Thread(new Cliente(panificio));

        // panettiereThread.start();
        // clienteThread.start();

        PanificioPanello panello = new PanificioPanello();
        add(panello);
    }

    public static void main(String[] args) {
        PanificioGioco gioco = new PanificioGioco();
        gioco.setVisible(true);
    }
}
