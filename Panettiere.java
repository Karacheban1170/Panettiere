import java.awt.*;
import java.awt.event.*;

public class Panettiere implements KeyListener, Runnable {
    private int centroX;
    private final int centroY;
    private final int width;
    private final int height;
    private static final int VELOCITA_PANETTIERE = 10;
    private final Panificio pnlPanificio;
    private boolean running = true;
    private final Image imgPanettiere;

    private Rectangle panettiereBounds;

    public Panettiere(Panificio pnlPanificio) {
        this.imgPanettiere = Toolkit.getDefaultToolkit().getImage("img/panettiere.png");
        this.pnlPanificio = pnlPanificio;

        this.width = 200;
        this.height = 300;

        this.centroX = 355;
        this.centroY = 140;
    }

    public void disegnaPanettiere(Graphics g) {
        if (imgPanettiere != null) {
            this.panettiereBounds = new Rectangle(centroX + 100, centroY, width - 199, height);
            g.drawImage(imgPanettiere, centroX, centroY, width, height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(centroX, centroY, width, height);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            if (centroX > 50) { // Limite a sinistra
                centroX -= VELOCITA_PANETTIERE; // Muove a sinistra
            }
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            if (centroX < pnlPanificio.getWidth() - width) { // Limite a destra
                centroX += VELOCITA_PANETTIERE; // Muove a destra
            }
        }
        pnlPanificio.repaint(); // Richiama il repaint del pannello per aggiornare la posizione
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(25);
                pnlPanificio.repaint(); // Rinfresca il pannello del gioco
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
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
