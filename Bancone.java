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
    private final BufferedImage btnIndietro;
    private final BufferedImage btnForno;

    private final Rectangle btnIndietroBounds;
    private final Rectangle btnFornoBounds;

    private ArrayList<Cliente> clienti;

    private final BufferedImage[] immaginiClienti = {
            loadImage("img/cliente1.png"),
            loadImage("img/cliente2.png")
    };

    private final ActionListener toPnlPanificioAction;
    private final ActionListener toPnlFornoAction;

    private Cursor defaultCursor, selectCursor, transparentSelectCursor;

    private final MyProgressBar myProgressBar;

    public Bancone(int width, int height, ActionListener toPnlPanificioAction) {
        this.width = width;
        this.height = height;
        this.sfondo = loadImage("img/bancone_sfondo.jpg");
        this.bancone = loadImage("img/bancone.png");
        this.btnIndietro = loadImage("img/btn_indietro.png");
        this.btnForno = loadImage("img/btn_forno.png");

        btnIndietroBounds = new Rectangle(width - 215, 22, 180, 60);
        btnFornoBounds = new Rectangle(width - 190, 385, 105, 105);

        this.toPnlPanificioAction = toPnlPanificioAction;
        addMouseListener(this);
        setCustomCursors();

        // Inizializza la barra di progresso e aggiungila al pannello
        myProgressBar = new MyProgressBar(0, 100);
        myProgressBar.setPreferredSize(new Dimension(width / 2, 20));
        myProgressBar.setProgressColor(new Color(0, 255, 0, 127));

        // Avvia il timer per decrementare la barra di progresso
        // createAndStartDecrementTimer();
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
        disegnaBtnIndietro(g2d);
        disegnaBtnForno(g2d);
        disegnaProgressBar(g2d);
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

    private void disegnaProgressBar(Graphics2D g2d) {
        int progressBarWidth = width / 2;
        int progressBarHeight = 20;
        int x = (width - progressBarWidth) / 2;
        int y = 50;

        // Disegna il contorno della barra di progresso
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, progressBarWidth, progressBarHeight);

        // Calcola la larghezza riempita in base al valore corrente della barra di
        // progresso
        int filledWidth = (int) ((myProgressBar.getValue() / 100.0) * progressBarWidth);

        // Disegna la parte riempita
        g2d.setColor(myProgressBar.getProgressColor());
        g2d.fillRect(x, y, filledWidth, progressBarHeight);
    }

    public void createAndStartDecrementTimer(int millisecondi) {
        int delay = millisecondi / 100;

        Timer progressBarCountDownTimer = new Timer(delay, new AbstractAction() {
            int count = 100;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (count == 0) {
                    ((Timer) ae.getSource()).stop();
                    System.out.println("Progress bar completata");
                } else {
                    count--;
                    myProgressBar.setValue(count);
                }
            }
        });
        progressBarCountDownTimer.start();
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
            if (isMouseOverBtnIndietro(mousePosition)) {
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

    private boolean isMouseOverBtn(Point mousePoint, Rectangle btnBounds) {
        switch (btnbounds) {
            case btnbounds == btnIndietroBounds:
                return btnIndietroBounds.contains(mousePoint);
                break;

        }

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
            if (isMouseOverBtn(mousePosition, btnIndietroBounds)) {
                if (toPnlPanificioAction != null && panificioMonitor.isClientiEntrano() == false) {
                    toPnlPanificioAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }else if(isMouseOverBtn(mousePosition, btnFornoBounds)){
                if (toPnlFornoAction != null) {
                    toPnlFornoAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
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
