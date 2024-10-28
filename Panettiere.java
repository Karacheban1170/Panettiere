import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Panettiere extends JLabel {

    private int panettiereWidth = 200; // Larghezza originale
    private int panettiereHeight = 300; // Altezza originale
    private final int PANETTIERE_VELOCITA = 20; // Velocità di movimento
    private boolean movimentoSinistra = false; // Direzione di movimento

    public int getPanettiereWidth() {
        return panettiereWidth;
    }

    public int getPanettiereHeight() {
        return panettiereHeight;
    }

    public void muoviSinistra() {
        movimentoSinistra = true;
        setLocation(Math.max(getX() - PANETTIERE_VELOCITA, 0), this.getY());
        repaint(); // Ridisegna il panettiere
    }

    public void muoviDestra() {
        movimentoSinistra = false;
        setLocation(Math.min(getX() + PANETTIERE_VELOCITA, getParent().getWidth() - (panettiereWidth / 4)), this.getY());
        repaint(); // Ridisegna il panettiere
    }
}
