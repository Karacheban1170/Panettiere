import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Panettiere extends Thread {

    // Caratteristiche del panettiere
    private int panettiereX = 500; // Posizione iniziale del panettiere
    private int panettiereY = 200; // Altezza fissa del panettiere
    private int panettiereWidth = 250;
    private int panettiereHeight = 410;

    private final int PANETTIERE_VELOCITA = 20;

    private boolean movimentoSinistra = false;

    private PanificioGUI panificio; // Collegamento alla classe principale
    private BufferedImage panettiereImage;

    public Panettiere(PanificioGUI panificio) {
        this.panificio = panificio;
        try {
            panettiereImage = ImageIO.read(new File("img/panettiere.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    public synchronized void produrrePane() throws InterruptedException {
        // while (pani.size() == CAPACITA) {
        // System.out.println("Panificio pieno! Attesa...");
        // wait();
        // }

        // pani.add("Pane");
        // System.out.println("Panettiere: Pane prodotto!");
        // notifyAll();
    }

    /**
     * @return the panettiereX
     */
    public int getPanettiereX() {
        return panettiereX;
    }

    /**
     * @return the panettiereY
     */
    public int getPanettiereY() {
        return panettiereY;
    }

    /**
     * @return the panettiereWidth
     */
    public int getPanettiereWidth() {
        return panettiereWidth;
    }

    /**
     * @return the panettiereHeight
     */
    public int getPanettiereHeight() {
        return panettiereHeight;
    }

    /**
     * @return the movimentoSinistra
     */
    public boolean isMovimentoSinistra() {
        return movimentoSinistra;
    }

    public void muoviSinistra() {
        movimentoSinistra = true;
        panettiereX = Math.max(panettiereX - PANETTIERE_VELOCITA, 0);
    }

    public void muoviDestra() {
        movimentoSinistra = false;
        panettiereX = Math.min(panettiereX + PANETTIERE_VELOCITA, panificio.getWidth() - panettiereWidth);
    }

    public void disegnaPanettiere(Graphics2D g2d) {
        if (movimentoSinistra) {
            AffineTransform originalTransform = g2d.getTransform(); // Salva lo stato originale
            g2d.translate(panettiereX + panettiereWidth, panettiereY); // Posizionare il panettiere
            g2d.scale(-1, 1); // Riflettere orizzontalmente
            g2d.drawImage(panettiereImage, 0, 0, panettiereWidth, panettiereHeight, null);
            g2d.setTransform(originalTransform); // Ripristina lo stato originale
        } else {
            g2d.drawImage(panettiereImage, panettiereX, panettiereY, panettiereWidth, panettiereHeight, null);
        }
    }

}
