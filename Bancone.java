import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

/**
 * La classe Bancone rappresenta il pannello del bancone nel gioco
 * "Panificio".
 * Questo pannello consente l'interazione con i clienti e la gestione della
 * vendita dei prodotti.
 * Implementa Runnable per aggiornare lo stato e MouseListener
 * per gestire
 * le interazioni tramite clic del mouse.
 * 
 * 
 * Il pannello contiene elementi grafici come sfondo, immagini di prodotti,
 * clienti e bottoni.
 * Inoltre, gestisce la logica di vendita dei prodotti e il passaggio tra
 * diverse scene (pannelli)
 * tramite ActionListener
 * 
 * @author Gruppo7
 */

public class Bancone extends JPanel implements Runnable, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final PanificioMonitor panificioMonitor;

    private final int width, height;

    private final BufferedImage sfondo;
    private final BufferedImage bancone;
    private final BufferedImage btnIndietro;
    private final BufferedImage btnForno;
    private final Rectangle btnIndietroBounds;
    private final Rectangle btnFornoBounds;

    private static ArrayList<Cliente> clienti;
    private final int numClienti;

    private static ArrayList<Prodotto> prodotti;
    private static BufferedImage prodottoFrame;
    private final ArrayList<Rectangle> prodottiBounds;
    private final int numProdotti;
    private final int quantitaProdotti;

    private static int score;

    private static final Color WHITE_COLOR = new Color(232, 247, 238, 255);
    private static final Color BROWN_COLOR = new Color(46, 21, 0, 255);

    private final BufferedImage[] immaginiClienti = {
            ImageLoader.loadImage("img/cliente1.png"),
            ImageLoader.loadImage("img/cliente2.png"),
            ImageLoader.loadImage("img/cliente3.png"),
            ImageLoader.loadImage("img/cliente4.png"),
            ImageLoader.loadImage("img/cliente5.png"),
            ImageLoader.loadImage("img/cliente6.png")
    };

    private final ActionListener toPnlPanificioAction;
    private final ActionListener toPnlFornoAction;

    private final GestioneAudio clienteSoddisfatto;
    private final GestioneAudio clienteArrabbiato;

    /**
     * Costruttore della classe Bancone
     * Inizializza le immagini, il monitor, le variabili di stato, i clienti e i
     * prodotti.
     *
     * @param width                larghezza del pannello
     * @param height               altezza del pannello
     * @param toPnlPanificioAction ActionListener per il passaggio al pannello
     *                             Panificio
     * @param toPnlFornoAction     ActionListener per il passaggio al pannello Forno
     */

    public Bancone(int width, int height, ActionListener toPnlPanificioAction, ActionListener toPnlFornoAction) {
        this.width = width;
        this.height = height;
        this.sfondo = ImageLoader.loadImage("img/bancone_sfondo.jpg");
        this.bancone = ImageLoader.loadImage("img/bancone.png");
        this.btnIndietro = ImageLoader.loadImage("img/btn_indietro.png");
        this.btnForno = ImageLoader.loadImage("img/btn_forno.png");

        panificioMonitor = new PanificioMonitor();

        numClienti = 10;
        numProdotti = 4;
        quantitaProdotti = 2;

        score = 0;

        prodotti = new ArrayList<>(numProdotti);
        prodottiBounds = new ArrayList<>(prodotti.size());

        prodottoFrame = ImageLoader.loadImage("img/prodottoFrame.png");
        prodotti.add(new Prodotto(ImageLoader.loadImage("img/prodotto1.png"), "ciambella", quantitaProdotti));
        prodotti.add(new Prodotto(ImageLoader.loadImage("img/prodotto2.png"), "croissant", quantitaProdotti));
        prodotti.add(new Prodotto(ImageLoader.loadImage("img/prodotto3.png"), "muffin", quantitaProdotti));
        prodotti.add(new Prodotto(ImageLoader.loadImage("img/prodotto4.png"), "pane", quantitaProdotti));

        btnIndietroBounds = new Rectangle(width - 215, 22, 180, 60);
        btnFornoBounds = new Rectangle(width - 181, 392, 105, 105);

        this.toPnlPanificioAction = toPnlPanificioAction;
        this.toPnlFornoAction = toPnlFornoAction;

        clienteSoddisfatto = new GestioneAudio("audio/cliente_soddisfatto.wav");
        clienteSoddisfatto.setVolume(0.55f);

        clienteArrabbiato = new GestioneAudio("audio/cliente_arrabbiato.wav");
        clienteArrabbiato.setVolume(0.55f);

        addMouseListener(this);

        DynamicCursor.setCustomCursors(this);
    }

    /**
     * Esegue il ciclo di aggiornamento del pannello, aggiornando il cursore
     * e ridisegnando l'interfaccia.
     */
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                DynamicCursor.updateCursor(this, btnIndietroBounds, btnFornoBounds, prodottiBounds);
                repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Avvia il thread di aggiornamento per il pannello, se non è già attivo.
     */
    public synchronized void start() {
        if (updateThread == null || !updateThread.isAlive()) {
            running = true;
            updateThread = new Thread(this);
            updateThread.start();
        }
    }

    /**
     * Ferma il thread di aggiornamento per il pannello.
     */
    public synchronized void stop() {
        running = false;
        if (updateThread != null) {
            updateThread.interrupt();
        }
    }

    /**
     * Disegna i componenti grafici del pannello, inclusi sfondo, clienti, prodotti,
     * bottoni e altri elementi interattivi.
     *
     * @param g l'oggetto Graphics per il disegno
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        disegnaSfondo(g2d);

        for (Cliente cliente : clienti) {
            cliente.disegnaCliente(g2d);
            cliente.disegnaNuvola(g2d);
            cliente.disegnaProdottoDesiderato(g2d);
            cliente.disegnaProgressBar(g2d);
        }

        disegnaBancone(g2d);
        disegnaProdotti(g2d);
        disegnaBtnIndietro(g2d);
        disegnaBtnForno(g2d);

        FadingScene.disegnaFadingRect(g2d);
    }

    /**
     * Disegna lo sfondo del pannello.
     *
     * @param g2d l'oggetto Graphics2D per il disegno
     */
    private void disegnaSfondo(Graphics2D g2d) {
        if (sfondo != null) {
            g2d.drawImage(sfondo, 0, 0, width, height, this);
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
        }
    }

    /**
     * Disegna il bancone sul pannello.
     *
     * @param g2d l'oggetto Graphics2D per il disegno
     */
    private void disegnaBancone(Graphics2D g2d) {
        if (bancone != null) {
            g2d.drawImage(bancone, 0, 355, bancone.getWidth(), bancone.getHeight(), this);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(0, 355, width, height);
        }
    }

    /**
     * Disegna i prodotti sul bancone, compresi la cornice e la quantità
     * disponibile.
     *
     * @param g2d l'oggetto Graphics2D per il disegno
     */
    private void disegnaProdotti(Graphics2D g2d) {
        int xPos = width / 2 - 195;
        int yPos = 405;
        int sizeProdotto = 80;

        for (int i = 0; i < prodotti.size(); i++) {
            if (prodotti.get(i) != null) {
                // Disegna la cornice del prodotto
                g2d.drawImage(prodottoFrame, xPos, yPos, sizeProdotto, sizeProdotto, this);

                // Disegna il prodotto
                g2d.drawImage(prodotti.get(i).getImage(), xPos, yPos, sizeProdotto, sizeProdotto, this);

                // Aggiungi il Rectangle per il prodotto
                prodottiBounds.add(new Rectangle(xPos, yPos, sizeProdotto, sizeProdotto));

                // Disegna la quantità accanto al prodotto
                g2d.setColor(BROWN_COLOR);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));

                String quantitaStr = String.valueOf(prodotti.get(i).getQuantita());
                g2d.fillOval(xPos + 58, yPos + 49, 31, 31);

                g2d.setColor(WHITE_COLOR);

                if (prodotti.get(i).getQuantita() > 9) {
                    g2d.drawString(quantitaStr, xPos + 65, yPos + 70);
                } else {
                    g2d.drawString(quantitaStr, xPos + 70, yPos + 70);
                }

                xPos += 100;
            }
        }
    }

    /**
     * Disegna il bottone per tornare al pannello Panificio.
     *
     * @param g2d l'oggetto Graphics2D per il disegno
     */
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

    /**
     * Disegna il bottone per passare al pannello Forno.
     *
     * @param g2d l'oggetto Graphics2D per il disegno
     */
    private void disegnaBtnForno(Graphics2D g2d) {
        if (btnForno != null) {
            g2d.drawImage(btnForno, btnForno.getWidth() + 270, btnForno.getHeight() - 118, 125, 125,
                    this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 40, 100, width, height);
        }
    }

    /**
     * Inizializza i clienti e avvia i thread associati per il comportamento di ogni
     * cliente.
     */
    public void initClienti() {
        clienti = new ArrayList<>();
        Random rand = new Random();
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numClienti; i++) {

            Cliente cliente = new Cliente(immaginiClienti[rand.nextInt(immaginiClienti.length)],
                    panificioMonitor, "Cliente " + (i + 1), prodotti);
            clienti.add(cliente);

            Thread th = new Thread(cliente);
            threads.add(th);
        }

        for (Thread th : threads) {
            th.start();
        }
    }

    /**
     * Gestisce la logica di vendita del prodotto selezionato in base alla posizione
     * del mouse.
     *
     * @param mousePosition la posizione del clic del mouse
     */
    private void vendiProdotto(Point mousePosition) {
        for (int i = 0; i < prodottiBounds.size(); i++) {
            if (prodottiBounds.get(i).contains(mousePosition)) {
                Prodotto prodottoSelezionato = prodotti.get(i);
                if (prodottoSelezionato.getQuantita() == 0) {
                    break;
                }

                // Verifica se il cliente desidera questo prodotto
                for (Cliente cliente : clienti) {
                    if (cliente.isClienteAspetta()) {
                        cliente.setProdottoComprato(true);
                        if (cliente.getProdottoDesiderato().equals(prodottoSelezionato)) {
                            // Decrementa la quantità del prodotto
                            prodottoSelezionato.decrementaQuantita();
                            cliente.setSoddisfatto(true);
                            clienteSoddisfatto.playSound();
                            score += 20;
                            break;
                        } else {
                            prodottoSelezionato.decrementaQuantita();
                            cliente.setSoddisfatto(false);
                            clienteArrabbiato.playSound();
                            score += 5;
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    // Metodi vuoti di MouseListener per gestire altri eventi del mouse.
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Evento del mouse che rileva il rilascio del pulsante e gestisce le azioni di
     * clic.
     *
     * @param e l'evento del mouse
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (DynamicCursor.isMouseOverBounds(mousePosition, btnIndietroBounds)) {
                if (toPnlPanificioAction != null && PanificioMonitor.isClientiEntrano() == false) {
                    toPnlPanificioAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

                    try {
                        File file = new File("risultato_migliore.txt");
                        int migliorRisultato = 0;
                        try (Scanner sc = new Scanner(file)) {
                            if (sc.hasNextInt()) {
                                migliorRisultato = sc.nextInt();
                            }
                        }

                        if (getScore() > migliorRisultato) {
                            try (PrintWriter writer = new PrintWriter(file)) {
                                writer.println(getScore());
                            }
                        }

                    } catch (FileNotFoundException exc) {
                        exc.getMessage();
                    }
                }

            } else if (DynamicCursor.isMouseOverBounds(mousePosition, btnFornoBounds)) {
                if (toPnlFornoAction != null) {
                    toPnlFornoAction.actionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED, null));
                }
            }

            vendiProdotto(mousePosition);

        }
    }

    /**
     * Restituisce la lista dei prodotti.
     *
     * @return ArrayList di prodotti
     */
    public static ArrayList<Prodotto> getProdotti() {
        return prodotti;
    }

    /**
     * Restituisce la lista dei clienti.
     *
     * @return ArrayList di clienti
     */
    public static ArrayList<Cliente> getClienti() {
        return clienti;
    }

    /**
     * Restituisce il frame per visualizzare i prodotti.
     *
     * @return immagine del frame del prodotto
     */
    public static BufferedImage getProdottoFrame() {
        return prodottoFrame;
    }

    /**
     * Imposta il punteggio corrente.
     *
     * @param score il punteggio da impostare
     */
    public static void setScore(int score) {
        Bancone.score = score;
    }

    /**
     * Restituisce il punteggio corrente.
     *
     * @return il punteggio corrente
     */
    public static int getScore() {
        return score;
    }
}
