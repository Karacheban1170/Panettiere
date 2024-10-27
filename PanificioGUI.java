import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanificioGUI extends JFrame {

    private JButton toPnlMain, toPnlBancone;
    private PanificioPanel pnlMain, pnlBancone;

    public PanificioGUI() {
        super("Panificio");

        setResizable(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponenti();
        initPannelli();
    }

    private void initComponenti() {
        toPnlBancone = new JButton("Vai al secondo pannello");
        toPnlBancone.addActionListener(new ChangeContentPaneListener("pnlBancone"));

        toPnlMain = new JButton("Torna al pannello principale");
        toPnlMain.addActionListener(new ChangeContentPaneListener("pnlMain"));
    }

    private void initPannelli() {
        setLayout(new CardLayout());

        pnlMain = new PanificioPanel(new ImageIcon("img/panificio_sfondo.jpg").getImage());
        pnlBancone = new PanificioPanel(new ImageIcon("img/bancone_sfondo.jpg").getImage());        

        pnlMain.add(toPnlBancone);
        pnlBancone.add(toPnlMain);
        // Aggiungi entrambi i pannelli al frame con nomi per `CardLayout`
        add(pnlMain, "pnlMain");
        add(pnlBancone, "pnlBancone");

        // Visualizza il pnlMain all'avvio
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "pnlMain");
    }

}

class PanificioPanel extends JPanel {

    private Image img;

    public PanificioPanel(String imgPath) {
        this(new ImageIcon(imgPath).getImage());
    }

    public PanificioPanel(Image img) {
        this.img = img;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Chiama il metodo di superclasse
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Ridimensiona l'immagine
    }
}
