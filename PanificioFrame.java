import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * Classe principale che rappresenta la finestra principale del gioco "Panificio".
 * Gestisce la visualizzazione dei pannelli tramite un layout a schede (CardLayout),
 * e la gestione della musica di sottofondo. I pannelli rappresentano diverse
 * scene del gioco (Panificio, Bancone, Forno) e sono gestiti dinamicamente tramite 
 * l'uso di ActionListener e il cambio di scena.
 * 
 * @author Gruppo7
 */
public class PanificioFrame extends JFrame {
    private Panificio pnlPanificio;
    private Bancone pnlBancone;
    private Forno pnlForno;

    private final CardLayout cardLayout;
    private static final int WIDTH_FRAME = 960;
    private static final int HEIGHT_FRAME = 540;

    private final GestioneAudio sottofondo;

    /**
     * Costruttore della classe. Inizializza la finestra del gioco, il layout a schede,
     * la musica di sottofondo e i pannelli di gioco.
     */
    public PanificioFrame() {
        super("Panificio");
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        sottofondo = new GestioneAudio("audio/sottofondo.wav");
        sottofondo.setVolume(0.55f);

        initPanelli();

        showPanel("PnlPanificio");
        sottofondo.playBackgroundMusic();
    }

    /**
     * Inizializza i pannelli del gioco (Panificio, Bancone, Forno) e imposta le azioni
     * da eseguire quando l'utente interagisce con i pannelli (passare da un pannello all'altro).
     */
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

    /**
     * Mostra il pannello specificato dal nome all'interno della finestra.
     * 
     * @param panelName Il nome del pannello da visualizzare.
     */
    private void showPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
        if ("PnlPanificio".equals(panelName)) {
            pnlPanificio.requestFocusInWindow();
        }
    }

    /**
     * Restituisce la larghezza della finestra.
     * 
     * @return La larghezza della finestra.
     */
    public static int getWidthFrame() {
        return WIDTH_FRAME;
    }

    /**
     * Restituisce l'altezza della finestra.
     * 
     * @return L'altezza della finestra.
     */
    public static int getHeightFrame() {
        return HEIGHT_FRAME;
    }
}