import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * La classe Panettiere gestisce il movimento e la rappresentazione visiva del personaggio panettiere all'interno
 * del gioco. Permette di spostarsi a sinistra e a destra tramite i tasti direzionali o le lettere 'A' e 'D', 
 * e visualizza l'immagine del panettiere in una posizione specificata.
 * 
 * Il panettiere può essere rappresentato sia nel suo stato normale che riflesso orizzontalmente quando si muove verso sinistra.
 * 
 * @author Gruppo7
 */
public class Panettiere implements KeyListener {
    private int centroX;
    private final int centroY;
    private final int width;
    private final int height;
    private static final int VELOCITA_PANETTIERE = 10;
    private final Panificio pnlPanificio;
    private final BufferedImage imgPanettiere;

    private boolean movimentoSinistra = false;

    private static Rectangle panettiereBounds;

    /**
     * Costruttore che inizializza il panettiere con le sue dimensioni e posizione iniziale.
     * 
     * @param pnlPanificio Il pannello del panificio in cui il panettiere si muove.
     */
    public Panettiere(Panificio pnlPanificio) {
        this.imgPanettiere = ImageLoader.loadImage("img/panettiere.png");
        this.pnlPanificio = pnlPanificio;

        this.width = 200;
        this.height = 300;

        this.centroX = 370;
        this.centroY = 140;

        Panettiere.panettiereBounds = new Rectangle(centroX + 100, centroY, width - 50, height);
    }

    /**
     * Disegna il panettiere nel pannello, considerando la sua posizione e direzione di movimento.
     * Se il panettiere si sta muovendo a sinistra, l'immagine viene riflessa orizzontalmente.
     * 
     * @param g2d L'oggetto Graphics2D utilizzato per disegnare il panettiere.
     */
    public void disegnaPanettiere(Graphics2D g2d) {
        if (imgPanettiere != null) {
            Panettiere.panettiereBounds = new Rectangle(centroX + 100, centroY, 1, height);
            if (movimentoSinistra) {
                AffineTransform originalTransform = g2d.getTransform(); // Salva lo stato originale
                g2d.translate(centroX + width, centroY); // Posizionare il panettiere
                g2d.scale(-1, 1); // Riflettere orizzontalmente
                g2d.drawImage(imgPanettiere, 0, 0, width, height, null);
                g2d.setTransform(originalTransform); // Ripristina lo stato originale
            } else {
                g2d.drawImage(imgPanettiere, centroX, centroY, width, height, null);
            }
        } else {
            Panettiere.panettiereBounds = new Rectangle(centroX + 100, centroY, 1, height);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(centroX, centroY, width, height);
        }
    }

    /**
     * Gestisce la pressione dei tasti. Il panettiere si muove a sinistra con il tasto "LEFT" o "A" 
     * e a destra con il tasto "RIGHT" o "D".
     * 
     * @param e L'evento di pressione del tasto.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            if (centroX > 50) { // Limite a sinistra
                centroX -= VELOCITA_PANETTIERE; // Muove a sinistra
                movimentoSinistra = true; // Cambia la direzione
            }
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            if (centroX < pnlPanificio.getWidth() - width) { // Limite a destra
                centroX += VELOCITA_PANETTIERE; // Muove a destra
                movimentoSinistra = false; // Cambia la direzione
            }
        }
    }

    // Metodi vuoti di KeyListener per gestire altri eventi del mouse.
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Restituisce la posizione X del centro del panettiere.
     * 
     * @return La coordinata X del centro del panettiere.
     */
    public int getCentroX() {
        return centroX;
    }

    /**
     * Restituisce la posizione Y del centro del panettiere.
     * 
     * @return La coordinata Y del centro del panettiere.
     */
    public int getCentroY() {
        return centroY;
    }

    /**
     * Restituisce la larghezza dell'immagine del panettiere.
     * 
     * @return La larghezza dell'immagine del panettiere.
     */
    public int getWidth() {
        return width;
    }

     /**
     * Restituisce l'altezza dell'immagine del panettiere.
     * 
     * @return L'altezza dell'immagine del panettiere.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Restituisce i confini del panettiere, utilizzati per il controllo delle collisioni.
     * 
     * @return Un Rectangle che rappresenta i confini del panettiere.
     */
    public static Rectangle getPanettiereBounds() {
        return panettiereBounds;
    }

}
