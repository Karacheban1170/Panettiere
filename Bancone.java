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

    private PanificioMonitor panificioMonitor;

    private final int width, height;

    private final BufferedImage sfondo;
    private final BufferedImage bancone;
    private final BufferedImage btnIndietro;
    private final BufferedImage btnForno;
    private final Rectangle btnIndietroBounds;
    private final Rectangle btnFornoBounds;

    private ArrayList<Cliente> clienti;

    private ArrayList<Prodotto> prodotti;
    private ArrayList<Rectangle> prodottiBounds;
    private final int quantita = 5;

    private final BufferedImage[] immaginiClienti = {
            loadImage("img/cliente1.png"),
            loadImage("img/cliente2.png")
    };

    private final ActionListener toPnlPanificioAction;
    private final ActionListener toPnlFornoAction;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    private float blackOpacity = 1f;
    private Timer fadeInTimer;

    public Bancone(int width, int height, ActionListener toPnlPanificioAction, ActionListener toPnlFornoAction) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/bancone_sfondo.jpg");
        this.bancone = loadImage("img/bancone.png");
        this.btnIndietro = loadImage("img/btn_indietro.png");
        this.btnForno = loadImage("img/btn_forno.png");

        panificioMonitor = new PanificioMonitor();

        prodotti = new ArrayList<>(4);
        prodottiBounds = new ArrayList<>(prodotti.size());

        this.prodotti.add(new Prodotto(loadImage("img/prodotto1.png"), quantita));
        this.prodotti.add(new Prodotto(loadImage("img/prodotto2.png"), quantita));
        this.prodotti.add(new Prodotto(loadImage("img/prodotto3.png"), quantita));
        this.prodotti.add(new Prodotto(loadImage("img/prodotto4.png"), quantita));

        btnIndietroBounds = new Rectangle(width - 215, 22, 180, 60);
        btnFornoBounds = new Rectangle(width - 190, 385, 105, 105);

        this.toPnlPanificioAction = toPnlPanificioAction;
        this.toPnlFornoAction = toPnlFornoAction;
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
            cliente.disegnaProdottoDesiderato(g2d);
            cliente.disegnaProgressBar(g2d);
        }

        disegnaBancone(g2d);
        disegnaProdotti(g2d);
        disegnaBtnIndietro(g2d);
        disegnaBtnForno(g2d);

        // Disegna un rettangolo nero semi-trasparente per il fading
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blackOpacity));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        // Ripristina la trasparenza completa per il disegno normale
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
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

    private void disegnaProdotti(Graphics2D g2d) {
        int xPos = width / 2 - 180;
        int yPos = 415;

        for (int i = 0; i < prodotti.size(); i++) {
            if (prodotti.get(i) != null) {
                // Disegna il prodotto
                g2d.drawImage(prodotti.get(i).getImage(), xPos, yPos, 50, 50, this);

                // Aggiungi il Rectangle per il prodotto
                prodottiBounds.add(new Rectangle(xPos, yPos, 50, 50));

                // Disegna la quantità accanto al prodotto
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String quantitaStr = String.valueOf(prodotti.get(i).getQuantita());
                g2d.drawString(quantitaStr, xPos + 55, yPos + 50);

                xPos += 100;
            }
        }
    }

    private void disegnaBtnIndietro(Graphics2D g2d) {
        if (btnIndietro != null) {
            g2d.drawImage(btnIndietro, width - btnIndietro.getWidth() - 40, 20, btnIndietro.getWidth(),
                    btnIndietro.getHeight(),
                    this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 40, 20, width, height);
        }
    }

    private void disegnaBtnForno(Graphics2D g2d) {
        if (btnForno != null) {
            g2d.drawImage(btnForno, width - btnForno.getWidth(), height - btnForno.getHeight(), 100, 100,
                    this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 40, 100, width, height);
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
                    panificioMonitor, "Cliente" + (i + 1), prodotti);
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
            if (isMouseOverBtn(mousePosition, btnIndietroBounds)) {
                if (panificioMonitor.isClientiEntrano() == false) {
                    setCursor(selectCursor);
                } else {
                    setCursor(transparentSelectCursor);
                }
            } else if (isMouseOverBtn(mousePosition, btnFornoBounds)) {
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
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();

        Point mousePosition = e.getPoint();

        // Controlla se il clic è su uno dei prodotti
        for (int i = 0; i < prodottiBounds.size(); i++) {
            if (prodottiBounds.get(i).contains(mousePosition)) {
                prodotti.get(i).decrementaQuantita();
                break;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (isMouseOverBtn(mousePosition, btnIndietroBounds)) {
                if (toPnlPanificioAction != null && panificioMonitor.isClientiEntrano() == false) {
                    toPnlPanificioAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }

            } else if (isMouseOverBtn(mousePosition, btnFornoBounds)) {
                if (toPnlFornoAction != null) {
                    toPnlFornoAction.actionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED, null));
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
