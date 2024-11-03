import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Bancone extends JPanel implements Runnable, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final PanificioMonitor panificioMonitor = new PanificioMonitor();

    private final int width, height;

    private final BufferedImage sfondo;
    private final BufferedImage bancone;
    private final BufferedImage indietro;

    private final Rectangle indietroBounds;

    private ArrayList<Cliente> clienti;

    private final BufferedImage[] immaginiClienti = {
            loadImage("img/cliente1.png"),
            loadImage("img/cliente2.png")
    };

    private final ActionListener toPnlPanificioAction;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    public Bancone(int width, int height, ActionListener toPnlPanificioAction) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/bancone_sfondo.jpg");
        this.bancone = loadImage("img/bancone.png");
        this.indietro = loadImage("img/indietro.png");

        indietroBounds = new Rectangle(width - 215, 22, 180, 60);

        this.toPnlPanificioAction = toPnlPanificioAction;
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
        if (updateThread == null || !updateThread.isAlive()) {
            running = true;
            updateThread = new Thread(this);
            updateThread.start();
        }
    }

    public synchronized void stop() {
        running = false;
        if (updateThread != null) {
            updateThread.interrupt();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        disegnaSfondo(g2d);

        for (Cliente cliente : clienti) {
            cliente.disegnaCliente(g2d);
        }

        disegnaBancone(g2d);
        disegnaIndietro(g2d);
    }

    private void disegnaSfondo(Graphics2D g2d) {
        if (sfondo != null) {
            g2d.drawImage(sfondo, 0, 0, width, height, this);
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
        }
    }

    private void disegnaBancone(Graphics2D g2d) {
        if (bancone != null) {
            g2d.drawImage(bancone, 0, 355, bancone.getWidth(), bancone.getHeight(), this);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(0, 355, width, height);
        }
    }

    private void disegnaIndietro(Graphics2D g2d) {
        if (indietro != null) {
            g2d.drawImage(indietro, width - indietro.getWidth() - 40, 20, indietro.getWidth(), indietro.getHeight(),
                    this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 40, 20, width, height);
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore caricamento immagine: " + path);
            return null;
        }
    }

    public void initClienti() {
        clienti = new ArrayList<>();
        Random rand = new Random();
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Cliente cliente = new Cliente(immaginiClienti[rand.nextInt(immaginiClienti.length)], this,
                    panificioMonitor, "Cliente" + (i + 1));
            clienti.add(cliente);

            Thread th = new Thread(cliente);
            threads.add(th);
        }

        for (Thread th : threads) {
            th.start();
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
            if (isMouseOverIndietro(mousePosition)) {
                if (panificioMonitor.isClientiEntrano() == false) {
                    setCursor(selectCursor);
                } else {
                    setCursor(transparentSelectCursor);
                }
            } else {
                setCursor(defaultCursor);
            }
        }
    }

    private boolean isMouseOverIndietro(Point mousePoint) {
        return indietroBounds.contains(mousePoint);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (isMouseOverIndietro(mousePosition)) {
                if (toPnlPanificioAction != null && panificioMonitor.isClientiEntrano() == false) {
                    toPnlPanificioAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
