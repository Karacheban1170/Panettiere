import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panificio extends JPanel implements FocusListener, MouseListener {

    private final int width, height;
    private boolean focussed = false;
    private final Image sfondo;
    private final Panettiere panettiere;
    private final Image bancone;

    
    public Panificio(int width, int height) {
        this.width = width;
        this.height = height;
        this.sfondo = Toolkit.getDefaultToolkit().getImage("img/panificio_sfondo.jpg");
        this.bancone = Toolkit.getDefaultToolkit().getImage("img/panificio_bancone.png");
        panettiere = new Panettiere(this);

        addKeyListener(panettiere);
        addFocusListener(this);
        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        disegnaSfondo(g);
        panettiere.disegnaPanettiere(g);
        disegnaBancone(g);

        if (!focussed) {
            paintIstruzioni(g);
        }
    }

    private void disegnaSfondo(Graphics g) {
        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, width, height, this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, width, height);
        }
    }

    private void disegnaBancone(Graphics g) {
        if (bancone != null) {
            g.drawImage(bancone, 0, 0, width, height, this);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, width, height);
        }
    }

    private void paintIstruzioni(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Cliccare per iniziare!", width / 2 - 100, height / 2 - 100);
    }

    @Override
    public void focusGained(FocusEvent e) {
        focussed = true;
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        focussed = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}