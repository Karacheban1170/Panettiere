import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PanificioFrame extends JFrame {
    private Panificio pnlPanificio;
    private Bancone pnlBancone;
    private Forno pnlForno;

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
        pnlPanificio = new Panificio(WIDTH_FRAME, HEIGHT_FRAME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("PnlBancone");
                pnlBancone.initClienti();
                FadingScene.fadingIn();
            }
        });

        pnlBancone = new Bancone(WIDTH_FRAME, HEIGHT_FRAME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("PnlPanificio");
                FadingScene.fadingIn();
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("PnlForno");
                FadingScene.fadingIn();
            }
        });

        pnlForno = new Forno(WIDTH_FRAME, HEIGHT_FRAME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("PnlBancone");
                FadingScene.fadingIn();
            }
        });

        // Avvia i pannelli
        pnlPanificio.start();
        pnlBancone.start();
        pnlForno.start();

        // Aggiungi i pannelli al contenitore
        getContentPane().add(pnlPanificio, "PnlPanificio");
        getContentPane().add(pnlBancone, "PnlBancone");
        getContentPane().add(pnlForno, "PnlForno");
    }

    private void showPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
        if ("PnlPanificio".equals(panelName)) {
            pnlPanificio.requestFocusInWindow();
        }
    }

    public static int getWidthFrame() {
        return WIDTH_FRAME;
    }

    public static int getHeightFrame() {
        return HEIGHT_FRAME;
    }
}