import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Forno extends JPanel implements Runnable, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final int width, height;

    private final BufferedImage sfondo;

    private final Rectangle btnBanconeBounds;

    private ActionListener toPnlBanconeAction;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    private float blackOpacity = 1f; 
    private Timer fadeInTimer;

    public Forno(int width, int height, ActionListener toPnlBanconeAction) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/panificio_sfondo.jpg");

        btnBanconeBounds = new Rectangle((width / 2) - 230, (height / 2) + 45, 400, 100);

        this.toPnlBanconeAction = toPnlBanconeAction;
        addMouseListener(this);
        setCustomCursors();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                updateCursor();
                repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public synchronized void start() {
        running = true;

        updateThread = new Thread(this);
        updateThread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        disegnaSfondo(g2d);

        // Disegna un rettangolo nero semi-trasparente per il fading
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blackOpacity));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        // Ripristina la trasparenza completa per il disegno normale
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2d.setColor(Color.BLACK);
        g2d.fillRect(btnBanconeBounds.x, btnBanconeBounds.y, btnBanconeBounds.width, btnBanconeBounds.height);
    }

    private void disegnaSfondo(Graphics2D g2d) {
        if (sfondo != null) {
            g2d.drawImage(sfondo, 0, 0, width, height, this);
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
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
            if (isMouseOverBtn(mousePosition, btnBanconeBounds)) {
                setCursor(selectCursor);
            } else {
                setCursor(defaultCursor);
            }
        }
    }

    private boolean isMouseOverBtn(Point mousePoint, Rectangle btnBounds) {
        return btnBounds.contains(mousePoint);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (isMouseOverBtn(mousePosition, btnBanconeBounds)) {
                if (toPnlBanconeAction != null) {
                    toPnlBanconeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        }
    }

    public void fadingIn() {
        fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackOpacity -= 0.05f; // Riduce l'opacità del nero
                if (blackOpacity <= 0f) {
                    blackOpacity = 0f; // Limita l'opacità al minimo
                    fadeInTimer.stop();
                }
                repaint();
            }
        });
        blackOpacity = 1f;
        fadeInTimer.start();
    }

}
