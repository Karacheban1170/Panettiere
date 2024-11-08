import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class Prodotto extends JComponent implements MouseListener, MouseMotionListener {
    private final BufferedImage immagine;
    private int quantita;
    private final String nome;

    private Point initialClick;
    private int xMoved;
    private int yMoved;

    // Punti fissi per il posizionamento magnetico
    private Point fixedPositionBancone;
    private Point fixedPositionForno1;
    private Point fixedPositionForno2;

    public Prodotto(BufferedImage immagine, String nome, int quantita) {
        this.immagine = immagine;
        this.nome = nome;
        this.quantita = quantita;
    }

    public Prodotto(BufferedImage immagine, String nome) {
        this.nome = nome;
        this.immagine = immagine;
        fixedPositionBancone = new Point(0, 0);
        fixedPositionForno1 = new Point(PanificioFrame.getWidthFrame() / 2 - 100,
                PanificioFrame.getHeightFrame() / 2 + 30);
        fixedPositionForno2 = new Point(PanificioFrame.getWidthFrame() / 2 + 40,
                PanificioFrame.getHeightFrame() / 2 + 30);

        addMouseListener(this);
        addMouseMotionListener(this);
        setLocation(fixedPositionBancone); // Posizione iniziale
    }

    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double distanceToBancone = calculateDistance(getLocation(), fixedPositionBancone);
        double distanceToForno1 = calculateDistance(getLocation(), fixedPositionForno1);
        double distanceToForno2 = calculateDistance(getLocation(), fixedPositionForno2);

        if (distanceToBancone <= distanceToForno1 && distanceToBancone <= distanceToForno2) {
            setLocation(fixedPositionBancone); // Se Bancone è il più vicino
        } else if (distanceToForno1 < distanceToForno2) {
            setLocation(fixedPositionForno1); // Se Forno 1 è il più vicino
        } else {
            setLocation(fixedPositionForno2); // Se Forno 2 è il più vicino
        }

        xMoved = 0;
        yMoved = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int thisX = getLocation().x;
        int thisY = getLocation().y;

        xMoved = e.getX() - initialClick.x;
        yMoved = e.getY() - initialClick.y;

        int X = thisX + xMoved;
        int Y = thisY + yMoved;
        setLocation(X, Y);
    }

    private double calculateDistance(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void setFixedPositionBancone(Point fixedPositionBancone) {
        this.fixedPositionBancone = fixedPositionBancone;
    }

    public Point getFixedPositionForno1() {
        return fixedPositionForno1;
    }

    public Point getFixedPositionForno2() {
        return fixedPositionForno2;
    }

    public BufferedImage getImage() {
        return immagine;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void incrementaQuantita() {
        quantita++;
    }

    public void decrementaQuantita() {
        if (quantita > 0) {
            quantita--;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}