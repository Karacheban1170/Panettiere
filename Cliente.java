import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Cliente implements Runnable {
    private int clienteX;
    private final int clienteY;

    private final int centroBancone;

    private final int width;
    private final int height;
    private final String nome;

    private final int secondi;

    private static final int VELOCITA_CLIENTE = 10;
    private final BufferedImage imgCliente;
    private BufferedImage nuvola;

    private final Prodotto prodottoDesiderato;
    private final ArrayList<Prodotto> prodottiDisponibili;

    private final PanificioMonitor panificioMonitor;

    private Thread clienteThread;
    private boolean running = true;

    private boolean movimentoSinistra = false;
    private static boolean clienteAspetta = false;

    private final MyProgressBar myProgressBar;

    public Cliente(BufferedImage imgCliente, PanificioMonitor panificioMonitor, String nome,
            ArrayList<Prodotto> prodottiDisponibili) {
        this.panificioMonitor = panificioMonitor;
        this.imgCliente = imgCliente;
        this.secondi = 10;
        this.width = 200;
        this.height = width + 150;
        this.clienteX = -width - 50;
        this.clienteY = 120;
        this.nome = nome;

        this.centroBancone = PanificioFrame.getWidthFrame() / 2 - width / 2;

        try {
            this.nuvola = ImageIO.read(new File("img/nuvola.png"));
        } catch (IOException e) {
            e.getMessage();
        }

        this.prodottiDisponibili = prodottiDisponibili;
        this.prodottoDesiderato = scegliProdottoDesiderato();

        // Inizializza la barra di progresso e aggiungila al pannello
        myProgressBar = new MyProgressBar(0, 100);
        myProgressBar.setPreferredSize(new Dimension(width / 2, 20));
        myProgressBar.setProgressColor(new Color(0, 255, 0, 127));
    }

    @Override
    public void run() {
        while (running) {
            entraCliente();
            Thread progressBarThread = createAndStartDecrementThread(secondi);

            // Una volta entrato, il cliente si ferma e aspetta un momento
            try {
                progressBarThread.join();
                clienteAspetta = false;
            } catch (InterruptedException e) {
                e.getMessage();
            }

            esceCliente();

        }
    }

    public synchronized void start() {
        if (clienteThread == null || !clienteThread.isAlive()) {
            clienteThread = new Thread(this);
            clienteThread.start();
        }
    }

    public synchronized void stop() {
        running = false;
        if (clienteThread != null) {
            clienteThread.interrupt();
        }
    }

    public void disegnaCliente(Graphics2D g2d) {
        if (imgCliente != null) {
            AffineTransform originalTransform = g2d.getTransform();
            if (movimentoSinistra) {
                g2d.translate(clienteX + width, clienteY);
                g2d.scale(-1, 1);
                g2d.drawImage(imgCliente, 0, 0, width, height, null);
            } else {
                g2d.drawImage(imgCliente, clienteX, clienteY, width, height, null);
            }
            g2d.setTransform(originalTransform);
        }
    }

    private Prodotto scegliProdottoDesiderato() {
        Random rand = new Random();
        return prodottiDisponibili.get(rand.nextInt(prodottiDisponibili.size())); // Scegli un prodotto casuale
    }

    public void disegnaNuvola(Graphics2D g2d) {
        if (nuvola != null) {
            if (clienteX >= centroBancone && clienteX <= centroBancone + width) {
                g2d.drawImage(nuvola, clienteX + width - 17, clienteY - 5, nuvola.getWidth(), nuvola.getHeight(), null);
            }
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public void disegnaProdottoDesiderato(Graphics2D g2d) {
        // Disegna il prodotto desiderato solo se il cliente è al centro del bancone
        if (prodottoDesiderato != null && clienteX >= centroBancone && clienteX <= centroBancone + width) {
            // Disegna l'immagine del prodotto desiderato accanto al cliente
            g2d.drawImage(prodottoDesiderato.getImage(), clienteX + width + 22, clienteY + 18, 50, 50, null);
            g2d.setColor(Color.BLACK);
        }
    }

    public void disegnaProgressBar(Graphics2D g2d) {
        int progressBarWidth = width;
        int progressBarHeight = 20;
        int x = clienteX;
        int y = 80;

        if (myProgressBar.getValue() != 0) {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, progressBarWidth, progressBarHeight);
        }

        int filledWidth = (int) ((myProgressBar.getValue() / 100.0) * progressBarWidth);

        g2d.setColor(myProgressBar.getProgressColor());
        g2d.fillRect(x, y, filledWidth, progressBarHeight);
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
                        myProgressBar.setValue(count);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
        });

        clienteAspetta = true;
        progressBarThread.start();

        // Restituiamo il thread per poter fare join() nel metodo run()
        return progressBarThread;
    }

    public void entraCliente() {
        movimentoSinistra = false;

        panificioMonitor.enterPanificio(nome);
        while (clienteX < centroBancone) {
            clienteX += VELOCITA_CLIENTE; // Muovi il cliente a destra
            try {
                Thread.sleep(50); // Controllo del movimento
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }

    }

    public void esceCliente() {
        movimentoSinistra = true;
        // Il cliente si muove verso l'esterno del panificio
        while (clienteX > -width) {
            clienteX -= VELOCITA_CLIENTE; // Muovi il cliente a sinistra
            try {
                Thread.sleep(50); // Controllo del movimento
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
        panificioMonitor.exitPanificio(nome);
        stop();
    }

    public static boolean isClienteAspetta() {
        return clienteAspetta;
    }

    public int getClienteX() {
        return clienteX;
    }

    public int getClienteY() {
        return clienteY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
