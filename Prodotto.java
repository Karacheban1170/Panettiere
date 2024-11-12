import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class Prodotto extends JComponent implements Runnable, MouseListener, MouseMotionListener {
    private final BufferedImage immagine;
    private int quantita;
    private final String nome;

    private Point initialClick;
    private int xMoved;
    private int yMoved;

    // Punti fissi per il posizionamento magnetico
    private Point fixedPositionBancone;
    private static Point fixedPositionForno1;
    private static Point fixedPositionForno2;
    private static Point positionCentraleNuovoProdotto;

    private Thread prodottoThread;
    private boolean running = true;

    private ProgressBar progressBar;

    private static final Color GREEN_COLOR = new Color(0, 255, 0, 200);
    private static final int SECONDI_COTTURA = 10;
    private int secondiRimasti;

    // Prodotto nel panello "Bancone"
    public Prodotto(BufferedImage immagine, String nome, int quantita) {
        this.immagine = immagine;
        this.nome = nome;
        this.quantita = quantita;

        // Inizializza la barra di progresso e aggiungila al pannello
        progressBar = new ProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(PanificioFrame.getWidthFrame() / 2, 20));
        progressBar.setProgressColor(GREEN_COLOR);

    }

    // Ingrediente nel panello "Forno"
    public Prodotto(BufferedImage immagine, String nome) {
        this.nome = nome;
        this.immagine = immagine;
        secondiRimasti = SECONDI_COTTURA;
        fixedPositionBancone = new Point(0, 0);
        fixedPositionForno1 = new Point(PanificioFrame.getWidthFrame() / 2 - 120,
                PanificioFrame.getHeightFrame() / 2 + 10);
        fixedPositionForno2 = new Point(PanificioFrame.getWidthFrame() / 2 + 40,
                PanificioFrame.getHeightFrame() / 2 + 10);

        positionCentraleNuovoProdotto = new Point(PanificioFrame.getWidthFrame() / 2 - 40,
                PanificioFrame.getHeightFrame() / 2 + 10);

        addMouseListener(this);
        addMouseMotionListener(this);
        setLocation(fixedPositionBancone); // Posizione iniziale
    }

    @Override
    public void run() {
        while (running) {
            Thread progressBarThread = createAndStartDecrementThread(SECONDI_COTTURA);

            // Una volta entrato, il cliente si ferma e aspetta un momento
            try {
                progressBarThread.join();
                setLocation(new Point(positionCentraleNuovoProdotto));
                Forno.setProdottoStaCuocendo(false);
                Forno.getNuovoProdottoBounds().setLocation(new Point(positionCentraleNuovoProdotto));
                stop();
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    public synchronized void start() {
        if (prodottoThread == null || !prodottoThread.isAlive()) {
            prodottoThread = new Thread(this);
            prodottoThread.start();
        }
    }

    public synchronized void stop() {
        running = false;
        if (prodottoThread != null) {
            prodottoThread.interrupt();
        }
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
        if (!Forno.isProdottoStaCuocendo()) {
            int thisX = getLocation().x;
            int thisY = getLocation().y;

            xMoved = e.getX() - initialClick.x;
            yMoved = e.getY() - initialClick.y;

            int X = thisX + xMoved;
            int Y = thisY + yMoved;
            setLocation(X, Y);
        }
    }

    private double calculateDistance(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void setFixedPositionBancone(Point fixedPositionBancone) {
        this.fixedPositionBancone = fixedPositionBancone;
    }

    public void setPosizioneIniziale() {
        setLocation(fixedPositionBancone);
    }

    public static Point getFixedPositionForno1() {
        return fixedPositionForno1;
    }

    public static Point getFixedPositionForno2() {
        return fixedPositionForno2;
    }

    public static Point getPositionCentraleNuovoProdotto() {
        return positionCentraleNuovoProdotto;
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

    public void disegnaProgressBar(Graphics2D g2d) {
        int progressBarWidth = PanificioFrame.getWidthFrame() / 4;
        int progressBarHeight = 20;
        int x = PanificioFrame.getWidthFrame() / 3 + 40;
        int y = PanificioFrame.getHeightFrame() / 3;

        if (progressBar.getValue() > 0) {
            int filledWidth = (int) ((progressBar.getValue() / 100.0) * progressBarWidth);

            g2d.setColor(progressBar.getProgressColor());
            g2d.fillRect(x, y, filledWidth, progressBarHeight);

            g2d.setColor(Color.BLACK);

            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.drawString(String.valueOf(secondiRimasti) + " S", x + 110, y + 16);

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, progressBarWidth, progressBarHeight);
        }

    }

    private Thread createAndStartDecrementThread(int secondi) {

        // Crea un thread per la barra di progresso da 100 a 0.
        Thread progressBarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Calcola il ritardo tra ogni decremento della barra di progresso per
                // completare la progressione in secondi.
                // secondi * 1000 converte il tempo totale di attesa da secondi a
                // millisecondi (poiché 1 secondo = 1000 ms).
                // Dividendo per 100 otteniamo l'intervallo di tempo per ridurre di un'unità
                int count = 100;
                int delay = (secondi * 1000) / count;
                while (count > 0) {
                    try {
                        Thread.sleep(delay);
                        count--;
                        secondiRimasti = count * secondi / 100;
                        progressBar.setValue(count);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
        });
        progressBarThread.start();

        // Restituiamo il thread per poter fare join() nel metodo run()
        return progressBarThread;
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