import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanificioGUI extends JFrame {

    private JPanel pnlMain;
    private JLabel mainBackground, mainBancone;
    private JPanel mainOverlayPanel;

    private JPanel pnlBancone;
    private JLabel banconeBackground;
    private JPanel banconeOverlayPanel;

    private JButton toPnlMain, toPnlBancone;

    private Panettiere panettiere;
    private CardLayout cardLayout;

    public PanificioGUI() {
        super("Panificio");

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setFocusable(true);
        setResizable(false);
        requestFocusInWindow();

        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        initComponenti();
        initPannelli();

        // Aggiunge un listener per il ridimensionamento della finestra
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBackgroundImages();
            }
        });

        // Aggiunge un listener per la tastiera
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) { // Tasto 'A' per muovere a sinistra
                    panettiere.muoviSinistra();
                } else if (e.getKeyCode() == KeyEvent.VK_D) { // Tasto 'D' per muovere a destra
                    panettiere.muoviDestra();
                }
            }
        });

        showPanel("MainPanel");
    }

    private void initComponenti() {
        mainBackground = new JLabel();
        mainBancone = new JLabel();
        toPnlBancone = new JButton("Vai al secondo pannello");
        toPnlBancone.addActionListener(e -> showPanel("BanconePanel"));
        
        panettiere = new Panettiere();

        banconeBackground = new JLabel();
        toPnlMain = new JButton("Torna al pannello principale");
        toPnlMain.addActionListener(e -> showPanel("MainPanel"));

        resizeBackgroundImages();
    }

    private void initPannelli() {
        pnlMain = new JPanel(new BorderLayout());
        mainOverlayPanel = new JPanel();
        mainOverlayPanel.setLayout(new OverlayLayout(mainOverlayPanel));
        
        mainOverlayPanel.add(toPnlBancone);
        
        mainOverlayPanel.add(panettiere);
        mainOverlayPanel.add(mainBackground);
        mainOverlayPanel.add(mainBancone);

        pnlBancone = new JPanel(new BorderLayout());
        banconeOverlayPanel = new JPanel();
        banconeOverlayPanel.setLayout(new OverlayLayout(banconeOverlayPanel));
        banconeOverlayPanel.add(toPnlMain);
        banconeOverlayPanel.add(banconeBackground);

        pnlMain.add(mainOverlayPanel, BorderLayout.CENTER);
        pnlBancone.add(banconeOverlayPanel, BorderLayout.CENTER);

        getContentPane().add(pnlMain, "MainPanel");
        getContentPane().add(pnlBancone, "BanconePanel");
    }

    private void showPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
    }

    private Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {
        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
    }

    private void resizeBackgroundImages() {
        Dimension boundary = getSize();
        Dimension boundaryPanettiere = new Dimension();
        boundaryPanettiere.setSize(700, 600);

        resizeImage(mainBackground, "img/panificio_sfondo.jpg", boundary);

        resizeImage(banconeBackground, "img/bancone_sfondo.jpg", boundary);

        resizeImage(mainBancone, "img/panificio_bancone.jpg", boundary);

        resizeImage(panettiere, "img/panettiere.png", boundaryPanettiere);
    }

    private void resizeImage(JLabel label, String imagePath, Dimension boundary) {
        ImageIcon icon = new ImageIcon(imagePath);
        Dimension imgSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
        Dimension scaledSize = getScaledDimension(imgSize, boundary);
        Image img = icon.getImage().getScaledInstance(scaledSize.width, scaledSize.height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    }
}
