import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Panificio extends JPanel implements Runnable, FocusListener, MouseListener {

    private final Thread updateThread;
    private boolean running = true;

    private final int width, height;
    private boolean focussed = false;
    private final BufferedImage sfondo;
    private final Panettiere panettiere;
    private final BufferedImage bancone;

    private final Rectangle banconeBounds;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    public Panificio(int width, int height) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/panificio_sfondo.jpg");
        this.bancone = loadImage("img/panificio_bancone.png");
        panettiere = new Panettiere(this);

        banconeBounds = new Rectangle((width / 2) - 230, (height / 2) + 45, 400, 100);

        addKeyListener(panettiere);
        addFocusListener(this);
        addMouseListener(this);

        setCustomCursors();

        updateThread = new Thread(this);
        updateThread.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(16);
                updateCursor();
                repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
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
            g.fillRect(banconeBounds.x, banconeBounds.y, banconeBounds.width, banconeBounds.height);
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void paintIstruzioni(Graphics g) {
        g.setColor(Color.WHITE);
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

    private void setCustomCursors() {
        BufferedImage defaultCursorImage = loadImage("img/default_cursor.png");
        BufferedImage selectCursorImage = loadImage("img/select_cursor.png");
        BufferedImage transparentSelectCursorImage = loadImage("img/trasparent_select_cursor.png");

        if (defaultCursorImage != null) {
            defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(defaultCursorImage, new Point(0, 0),
                    "Default Cursor");
        }
        if (selectCursorImage != null) {
            selectCursor = Toolkit.getDefaultToolkit().createCustomCursor(selectCursorImage, new Point(0, 0),
                    "Select Cursor");
        }

        // Crea il cursore trasparente
        if (transparentSelectCursorImage != null) {
            transparentSelectCursor = Toolkit.getDefaultToolkit().createCustomCursor(transparentSelectCursorImage,
                    new Point(0, 0),
                    "Transparent Select Cursor");
        }
        setCursor(defaultCursor);
    }

    private void updateCursor() {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            if (isMouseOverBancone(mousePosition)) {
                if (isPanettiereIntersectsBancone(banconeBounds)) {
                    setCursor(selectCursor);
                } else {
                    setCursor(transparentSelectCursor);
                }
            } else {
                setCursor(defaultCursor);
            }
        }
    }

    private boolean isMouseOverBancone(Point mousePoint) {
        return banconeBounds.contains(mousePoint);
    }

    private boolean isPanettiereIntersectsBancone(Rectangle banconeBounds) {
        return panettiere.getPanettiereBounds().intersects(banconeBounds);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Point mousePosition = e.getPoint();
        if (isMouseOverBancone(mousePosition)) {
            // Simula il clic sul pulsante toPnlBancone
            toPnlBancone.doClick(); // Questo attiverà l'azione del pulsante
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
