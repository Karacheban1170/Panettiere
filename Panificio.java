import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Panificio extends JPanel implements Runnable, FocusListener, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final int width, height;

    private boolean focussed = false;
    private boolean clickAllowed = false;

    private final BufferedImage sfondo;
    private final Panettiere panettiere;
    private final BufferedImage bancone;

    private final Rectangle banconeBounds;

    private ActionListener toPnlBanconeAction;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    public Panificio(int width, int height, ActionListener toPnlBanconeAction) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/panificio_sfondo.jpg");
        this.bancone = loadImage("img/panificio_bancone.png");

        panettiere = new Panettiere(this);

        banconeBounds = new Rectangle((width / 2) - 230, (height / 2) + 45, 400, 100);

        this.toPnlBanconeAction = toPnlBanconeAction;
        addKeyListener(panettiere);
        addFocusListener(this);
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
        panettiere.disegnaPanettiere(g2d);
        disegnaBancone(g2d);

        if (!focussed) {
            paintIstruzioni(g2d);
        }
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
            g2d.drawImage(bancone, 0, 0, width, height, this);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(banconeBounds.x, banconeBounds.y, banconeBounds.width, banconeBounds.height);
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

    private void paintIstruzioni(Graphics2D g2d) {
        // Imposta il colore e la trasparenza per lo sfondo delle istruzioni
        g2d.setColor(new Color(0, 0, 0, 150)); // Nero semi-trasparente
        g2d.fillRect(0, 0, width, height); // Rettangolo di sfondo

        // Testo di istruzioni
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Usa i tasti per muovere il panettiere", width / 2 - 150, height / 2 - 130);
        g2d.drawString("Clicca con il mouse per interagire!", width / 2 - 150, height / 2 + 80);

        // Carica e disegna le immagini dei tasti A, D e del mouse
        BufferedImage tastoA = loadImage("img/tastoA.png");
        BufferedImage tastoD = loadImage("img/tastoD.png");
        BufferedImage frecciaSinistra = loadImage("img/freccia_sinistra.png");
        BufferedImage frecciaDestra = loadImage("img/freccia_destra.png");
        BufferedImage mouse = loadImage("img/mouse.png");

        int keySize = 60; // Dimensioni delle icone

        // Posizioni per le immagini
        int baseX = width / 2; // Coordinate X di base per il posizionamento
        int baseY = height / 2; // Coordinate Y di base per il posizionamento

        // Controlla e disegna le immagini delle frecce
        if (frecciaSinistra != null) {
            g2d.drawImage(frecciaSinistra, baseX - 130, baseY - 100, keySize, keySize, this); // Sinistra
        }
        if (frecciaDestra != null) {
            g2d.drawImage(frecciaDestra, baseX - 50, baseY - 100, keySize, keySize, this); // Destra
        }

        // Controlla e disegna le immagini dei tasti A e D
        if (tastoA != null) {
            g2d.drawImage(tastoA, baseX - 130, baseY - 20, keySize, keySize, this); // Tasto A
        }
        if (tastoD != null) {
            g2d.drawImage(tastoD, baseX - 50, baseY - 20, keySize, keySize, this); // Tasto D
        }

        int mouseWidth = 88;
        int mouseHeight = 140;

        // Controlla e disegna l'immagine del mouse
        if (mouse != null) {
            g2d.drawImage(mouse, baseX + 55, baseY - 100, mouseWidth, mouseHeight, this); // Mouse
        }
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

        // BufferedImage cursorImage = ImageIO.read(new File(percorsoImmagine)); 
        // Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0),
        //             "Cursor Martello");
        // setCursor(cursor)
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
        Rectangle panettiereBounds = panettiere.getPanettiereBounds();
        return panettiereBounds != null && panettiereBounds.intersects(banconeBounds);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickAllowed = true;
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
        if (clickAllowed && e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (isMouseOverBancone(mousePosition) && isPanettiereIntersectsBancone(banconeBounds)) {
                if (toPnlBanconeAction != null) {
                    toPnlBanconeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        }
    }

}
