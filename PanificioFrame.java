import java.awt.CardLayout;
import javax.swing.JFrame;

public class PanificioFrame extends JFrame {
    private Panificio pnlPanificio;
    private Bancone pnlBancone;
    private final CardLayout cardLayout;
    private static final int WIDTH_FRAME = 960;
    private static final int HEIGHT_FRAME = 540;

    public PanificioFrame() {
        super("Panificio");
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        initPanelli();

        showPanel("PnlPanificio");
    }

    private void initPanelli() {
        pnlBancone = new Bancone(WIDTH_FRAME, HEIGHT_FRAME, e -> showPanel("PnlPanificio"));
        pnlPanificio = new Panificio(WIDTH_FRAME, HEIGHT_FRAME, e -> {
            showPanel("PnlBancone"); 
            pnlBancone.initClienti();
        });
        
        pnlBancone.start();
        pnlPanificio.start();

        getContentPane().add(pnlPanificio, "PnlPanificio");
        getContentPane().add(pnlBancone, "PnlBancone");
    }

    private void showPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
        if ("PnlPanificio".equals(panelName)) {
            pnlPanificio.requestFocusInWindow();
        }
    }
}