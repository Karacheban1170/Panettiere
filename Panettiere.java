import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Panettiere implements KeyListener, Runnable {
    private int centroX;
    private final int centroY;
    private final int width;
    private final int height;
    private static final int VELOCITA_PANETTIERE = 10;
    private final Panificio pnlPanificio;
    private final BufferedImage imgPanettiere;

    private Thread newThread;
    private boolean running = true;

    private boolean movimentoSinistra = false;

    private Rectangle panettiereBounds;

    public Panettiere(Panificio pnlPanificio) {
        this.imgPanettiere = loadImage("img/panettiere.png");
        this.pnlPanificio = pnlPanificio;

        this.width = 200;
        this.height = 300;

        this.centroX = 370;
        this.centroY = 140;

        this.panettiereBounds = new Rectangle(centroX + 100, centroY, width - 199, height);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                pnlPanificio.repaint();
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto: " + e.getMessage());
            }
        }
    }

    public synchronized void start() {
        running = true;

        newThread = new Thread(this);
        newThread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void disegnaPanettiere(Graphics2D g2d) {
        if (imgPanettiere != null) {
            this.panettiereBounds = new Rectangle(centroX + 100, centroY, 1, height);
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
            this.panettiereBounds = new Rectangle(centroX + 100, centroY, 1, height);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(centroX, centroY, width, height);
        }
    }

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
        pnlPanificio.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @return the centroX
     */
    public int getCentroX() {
        return centroX;
    }

    /**
     * @return the centroY
     */
    public int getCentroY() {
        return centroY;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the panettiereBounds
     */
    public Rectangle getPanettiereBounds() {
        return panettiereBounds;
    }

}
