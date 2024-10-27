import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Panettiere extends JComponent {

    private int panettiereWidth = 200;
    private int panettiereHeight = 300;

    private final int PANETTIERE_VELOCITA = 20;
    private boolean movimentoSinistra = false;

    private Image panettiereImage;

    public Panettiere(String imgPath) {
        setLocation(0, 500);
        panettiereImage = new ImageIcon(imgPath).getImage();
        setPreferredSize(new Dimension(panettiereWidth, panettiereHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform transform = g2d.getTransform();
        if (movimentoSinistra) {
            g2d.scale(-1, 1); // Inverte l'immagine orizzontalmente
            g2d.drawImage(panettiereImage, -panettiereWidth, 0, panettiereWidth, panettiereHeight, this);
        } else {
            g2d.drawImage(panettiereImage, 0, 0, panettiereWidth, panettiereHeight, this);
        }
        g2d.setTransform(transform); // Ripristina la trasformazione originale
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

    public void muoviSinistra() {
        movimentoSinistra = true;
        setLocation(Math.max(getX() - PANETTIERE_VELOCITA, 0), this.getY());
    }

    public void muoviDestra() {
        movimentoSinistra = false;
        setLocation(Math.min(getX() + PANETTIERE_VELOCITA, getParent().getWidth() - panettiereWidth), this.getY());
    }

}
