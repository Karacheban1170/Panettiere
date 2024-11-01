import java.awt.*;
import java.awt.event.*;

public class Panettiere implements KeyListener {
    protected int centroX;
    protected int centroY;
    private int larghezza;
    private int altezza;
    private Panificio game;

    public Panettiere(int width, int height, Panificio game) {
        this.game = game;
        this.larghezza = width;
        this.altezza = height;
        centroX = width / 2;
        centroY = height - 50; // Posizione Y del panettiere
    }

    public void panettiereFrame(Graphics g, int width, int height) {
        g.setColor(Color.BLACK); // Colore del panettiere
        g.fillRect(centroX - 15, centroY - 30, 30, 60); // Disegna il panettiere come un rettangolo
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if (centroX > 0) {
                centroX -= 5; // Muove a sinistra
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (centroX < larghezza) {
                centroX += 5; // Muove a destra
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
