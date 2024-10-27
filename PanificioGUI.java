import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanificioGUI extends JFrame {

    private JButton toPnlMain, toPnlBancone;
    private PanificioPanel pnlMain, pnlBancone;

    private Panettiere panettiere;

    public PanificioGUI() {
        super("Panificio");

        // setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impostare la finestra a schermo intero
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setFocusable(true);
        requestFocusInWindow();

        initComponenti();
        initPannelli();
        initAscoltatori();

    }

    private void initComponenti() {
        setLayout(new BorderLayout());
        toPnlBancone = new JButton("Vai al secondo pannello");
        toPnlBancone.addActionListener(new ChangeContentPaneListener("pnlBancone"));

        toPnlMain = new JButton("Torna al pannello principale");
        toPnlMain.addActionListener(new ChangeContentPaneListener("pnlMain"));

        panettiere = new Panettiere("img/panettiere.png");
        panettiere.setLocation(0, 10);
    }

    private void initPannelli() {
        setLayout(new CardLayout());

        pnlMain = new PanificioPanel("img/panificio_sfondo.jpg");
        pnlBancone = new PanificioPanel("img/bancone_sfondo.jpg");

        pnlMain.add(panettiere);
        pnlMain.add(toPnlBancone);

        pnlBancone.add(toPnlMain);

        add(pnlMain, "pnlMain");
        add(pnlBancone, "pnlBancone");

        // Visualizza il pnlMain all'avvio
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "pnlMain");
    }

    private void initAscoltatori() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    panettiere.muoviSinistra();
                    System.out.println("A");
                    System.out.println(panettiere.getLocation());

                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    panettiere.muoviDestra();
                    System.out.println(panettiere.getLocation());
                    System.out.println("D");
                }
                panettiere.repaint();
            }
        });
    }

}

class PanificioPanel extends JPanel {

    private Image img;

    public PanificioPanel(String imgPath) {
        img = new ImageIcon(imgPath).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Chiama il metodo di superclasse
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Ridimensiona l'immagine
    }
}
