import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanificioFrame extends JFrame implements Runnable {
    private Panificio pnlPanificio;
    private JPanel pnlBancone;
    private JButton toPnlPanificio, toPnlBancone;
    private final CardLayout cardLayout;
    private Thread runner;

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

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    @Override
    public void dispose() {
        runner = null;
        super.dispose();
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();

        while (runner == currentThread) {
            try {
                Thread.sleep(25);
                pnlPanificio.repaint(); // Rinfresca il pannello principale
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private void initComponenti() {
        toPnlBancone = new JButton("Vai al pannello bancone");
        toPnlBancone.addActionListener(e -> showPanel("PnlBancone"));

        toPnlPanificio = new JButton("Torna al pannello panificio");
        toPnlPanificio.addActionListener(e -> showPanel("PnlPanificio"));
    }

    private void initPanelli() {
        pnlPanificio = new Panificio(WIDTH_FRAME, HEIGHT_FRAME);
        pnlBancone = new JPanel();
        pnlBancone.setBackground(Color.BLACK);

        pnlPanificio.add(toPnlBancone, BorderLayout.CENTER);
        pnlBancone.add(toPnlPanificio, BorderLayout.CENTER);

        getContentPane().add(pnlPanificio, "PnlPanificio");
        getContentPane().add(pnlBancone, "PnlBancone");
    }

    private void showPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
    }
}