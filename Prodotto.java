import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * La classe Prodotto rappresenta un prodotto o ingrediente nel panificio,
 * gestendo il movimento
 * tra il bancone e il forno, l'interazione tramite il mouse, e il monitoraggio
 * della cottura attraverso una barra di progresso.
 * La classe implementa Runnable, MouseListener, e MouseMotionListener per
 * gestire il comportamento del prodotto,
 * come il suo movimento, il monitoraggio della cottura e la gestione del clic e
 * del trascinamento del mouse.
 * 
 * Ogni prodotto ha una quantità e una posizione iniziale, e può essere spostato
 * tra il bancone e i forni tramite il mouse.
 * La cottura del prodotto è monitorata da una barra di progresso che si
 * decrementa nel tempo.
 *
 * @author Gruppo7
 */
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

    /**
     * Costruttore per un prodotto nel pannello "Bancone" con una quantità.
     *
     * @param immagine l'immagine del prodotto
     * @param nome     il nome del prodotto
     * @param quantita la quantità del prodotto
     */
    public Prodotto(BufferedImage immagine, String nome, int quantita) {
        this.immagine = immagine;
        this.nome = nome;
        this.quantita = quantita;

        // Inizializza la barra di progresso e aggiungila al pannello
        progressBar = new ProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(PanificioFrame.getWidthFrame() / 2, 20));
        progressBar.setProgressColor(GREEN_COLOR);

    }

    /**
     * Costruttore per un ingrediente nel pannello "Forno".
     *
     * @param immagine l'immagine dell'ingrediente
     * @param nome     il nome dell'ingrediente
     */
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

    /**
     * Avvia il thread del prodotto, che inizia a monitorare la cottura e il
     * movimento.
     */
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

    /**
     * Avvia il thread del prodotto se non è già in esecuzione.
     */
    public synchronized void start() {
        if (prodottoThread == null || !prodottoThread.isAlive()) {
            prodottoThread = new Thread(this);
            prodottoThread.start();
        }
    }

    /**
     * Ferma il thread del prodotto.
     */
    public synchronized void stop() {
        running = false;
        if (prodottoThread != null) {
            prodottoThread.interrupt();
        }
    }

    /**
     * Metodo che viene invocato quando si preme il tasto del mouse.
     * Salva la posizione iniziale del clic per poter tracciare il movimento del mouse durante il trascinamento.
     * 
     * @param e L'evento del mouse che contiene informazioni sulla posizione del clic.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getPoint();
    }

    /**
     * Metodo che viene invocato quando si rilascia il tasto del mouse.
     * Calcola la distanza tra la posizione corrente dell'oggetto e tre posizioni fisse (bancone, forno 1 e forno 2).
     * L'oggetto si sposterà verso la posizione fissa più vicina tra bancone, forno 1 e forno 2.
     * 
     * @param e L'evento del mouse che contiene informazioni sulla posizione del rilascio del clic.
     */
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

    /**
     * Metodo che viene invocato quando il mouse viene trascinato.
     * Calcola il movimento del mouse e aggiorna la posizione dell'oggetto in base alla differenza tra la posizione iniziale
     * del clic e la posizione attuale del mouse.
     * L'oggetto si sposta solo se non c'è un prodotto in cottura.
     * 
     * @param e L'evento del mouse che contiene informazioni sulla posizione del mouse durante il trascinamento.
     */
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

    /**
     * Calcola la distanza euclidea tra due punti.
     * 
     * @param p1 Il primo punto, rappresentato come un oggetto Point.
     * @param p2 Il secondo punto, rappresentato come un oggetto Point.
     * @return La distanza euclidea tra i due punti.
     */
    private double calculateDistance(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Imposta la posizione fissa del bancone per il prodotto.
     *
     * @param fixedPositionBancone la nuova posizione fissa del bancone
     */
    public void setFixedPositionBancone(Point fixedPositionBancone) {
        this.fixedPositionBancone = fixedPositionBancone;
    }

    /**
     * Imposta la posizione iniziale del prodotto sul bancone.
     */
    public void setPosizioneIniziale() {
        setLocation(fixedPositionBancone);
    }

    /**
     * Restituisce la posizione fissa del forno 1.
     *
     * @return la posizione fissa del forno 1
     */
    public static Point getFixedPositionForno1() {
        return fixedPositionForno1;
    }

    /**
     * Restituisce la posizione fissa del forno 2.
     *
     * @return la posizione fissa del forno 2
     */
    public static Point getFixedPositionForno2() {
        return fixedPositionForno2;
    }

    /**
     * Restituisce la posizione centrale del nuovo prodotto.
     *
     * @return la posizione centrale del nuovo prodotto
     */
    public static Point getPositionCentraleNuovoProdotto() {
        return positionCentraleNuovoProdotto;
    }

    /**
     * Restituisce l'immagine del prodotto.
     *
     * @return l'immagine del prodotto
     */
    public BufferedImage getImage() {
        return immagine;
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return il nome del prodotto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce la quantità del prodotto.
     *
     * @return la quantità del prodotto
     */
    public int getQuantita() {
        return quantita;
    }

     /**
     * Incrementa la quantità del prodotto di 1.
     */
    public void incrementaQuantita() {
        quantita++;
    }

    /**
     * Decrementa la quantità del prodotto di 1, ma solo se la quantità è maggiore di 0.
     */
    public void decrementaQuantita() {
        if (quantita > 0) {
            quantita--;
        }
    }

    /**
     * Disegna la barra di progresso per il prodotto.
     *
     * @param g2d il contesto grafico su cui disegnare la barra
     */
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

    /**
     * Crea e avvia un thread per gestire la barra di progresso, che decrementa il valore da 100 a 0 in base al tempo dato in secondi.
     * La barra di progresso rappresenta un conto alla rovescia che si completa entro il numero di secondi specificato.
     * 
     * @param secondi Il numero di secondi per cui la progress bar deve eseguire il conto alla rovescia.
     *                La durata totale della progress bar verrà suddivisa in 100 passaggi, ognuno corrispondente a un valore 
     *                della barra di progresso.
     * 
     * @return Il thread che esegue il decremento della barra di progresso. Questo thread può essere utilizzato per effettuare
     *         un join() nel metodo che lo chiama, per sincronizzare l'esecuzione.
     */
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