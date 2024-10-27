import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class PanificioGUI extends JFrame {

    private JButton toPnlMain, toPnlBancone;
    private PanificioPanel pnlMain;
    private BanconePanel bancone;

    private Panettiere panettiere;

    public PanificioGUI() {
        super("Panificio");

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
        toPnlBancone = new JButton("Vai al secondo pannello");
        toPnlBancone.addActionListener(e -> showPanel(bancone));

        toPnlMain = new JButton("Torna al pannello principale");
        toPnlMain.addActionListener(e -> showPanel(pnlMain));

        panettiere = new Panettiere("img/panettiere.png");
        bancone = new BanconePanel("img/bancone_sfondo.jpg", "img/bancone.png"); // Usa le due immagini
    }

    private void initPannelli() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        pnlMain = new PanificioPanel("img/panificio_sfondo.jpg");

        pnlMain.add(panettiere);
        pnlMain.add(toPnlBancone);

        // Aggiungi il pannello principale
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.8; // Imposta il peso per il pannello principale
        add(pnlMain, gbc);

        // Aggiungi il pannello del bancone sotto il pannello principale
        gbc.gridy = 1;
        gbc.weighty = 0.2; // Imposta il peso per il pannello del bancone
        add(bancone, gbc);

        // Visualizza il pnlMain all'avvio
        showPanel(pnlMain);
    }

    private void showPanel(JPanel panelToShow) {
        pnlMain.setVisible(false);
        bancone.setVisible(false);
        panelToShow.setVisible(true);
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

class BanconePanel extends JPanel {

    private Image backgroundImg;
    private Image banconeImg;

    public BanconePanel(String backgroundImgPath, String banconeImgPath) {
        backgroundImg = new ImageIcon(backgroundImgPath).getImage();
        banconeImg = new ImageIcon(banconeImgPath).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna l'immagine di sfondo
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);

        // Disegna l'immagine del bancone
        g.drawImage(banconeImg, 0, getHeight() - banconeImg.getHeight(this), getWidth(), banconeImg.getHeight(this), this);
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
