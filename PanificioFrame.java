import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PanificioFrame extends JFrame {
    private Panificio pnlPanificio;
    private Bancone pnlBancone;
    private JButton toPnlPanificio;
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

        initComponenti();
        initPanelli();

        showPanel("PnlPanificio");
    }

    private void initComponenti() {
        toPnlPanificio = new JButton("Torna al pannello panificio");
        toPnlPanificio.addActionListener(e -> showPanel("PnlPanificio"));
    }

    private void initPanelli() {
        pnlPanificio = new Panificio(WIDTH_FRAME, HEIGHT_FRAME, e -> showPanel("PnlBancone"));
        pnlPanificio.start();
        
        pnlBancone = new Bancone(WIDTH_FRAME, HEIGHT_FRAME, e -> showPanel("PnlPanificio"));
        pnlBancone.start();

        pnlBancone.add(toPnlPanificio, BorderLayout.CENTER);

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