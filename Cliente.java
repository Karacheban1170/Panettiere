import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Cliente implements Runnable {
    private int clienteX;
    private final int clienteY;

    private final int centroBancone;

    private final int width;
    private final int height;
    private final String nome;

    private static final int VELOCITA_CLIENTE = 3;
    private final BufferedImage imgCliente;
    private final BufferedImage nuvola;

    private final ArrayList<Prodotto> prodotti;
    private final Prodotto prodottoDesiderato;

    private boolean soddisfatto;
    private boolean prodottoComprato;

    private final PanificioMonitor panificioMonitor;

    private Thread clienteThread;
    private boolean running = true;

    private boolean movimentoSinistra;
    private boolean clienteAspetta;
    private static boolean clienteEntrato;

    private final ProgressBar progressBar;
    private final Random rand = new Random();

    private static final Color GREEN_COLOR = new Color(0, 255, 0, 200);
    private final int secondiAttesa;
    private int secondiRimasti;

    public Cliente(BufferedImage imgCliente, PanificioMonitor panificioMonitor, String nome,
            ArrayList<Prodotto> prodotti) {
        this.panificioMonitor = panificioMonitor;
        this.imgCliente = imgCliente;
        this.nome = nome;
        this.prodotti = prodotti;
        prodottoDesiderato = scegliProdottoDesiderato();

        secondiAttesa = 10 + rand.nextInt(11); // range(10-20)

        // Variabili del Cliente
        width = 200;
        height = width + 150;
        clienteX = -width - 50;
        clienteY = 120;
        soddisfatto = false;
        prodottoComprato = false;

        movimentoSinistra = false;
        clienteAspetta = false;
        clienteEntrato = false;

        centroBancone = PanificioFrame.getWidthFrame() / 2 - width / 2;
        nuvola = ImageLoader.loadImage("img/nuvola.png");

        // Inizializza la barra di progresso e aggiungila al pannello

        progressBar = new ProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(width / 2, 20));
        progressBar.setProgressColor(GREEN_COLOR);
        secondiRimasti = secondiAttesa;
    }
    

    @Override
    public void run() {
        while (running) {
            entraCliente();
            Thread progressBarThread = createAndStartDecrementThread(secondiAttesa);

            // Una volta entrato, il cliente si ferma e aspetta un momento
            try {
                progressBarThread.join();
                clienteAspetta = false;
                clienteEntrato = false;
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
        return prodotti.get(rand.nextInt(prodotti.size())); // Scegli un prodotto casuale
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
            g2d.drawImage(prodottoDesiderato.getImage(), clienteX + width + 10, clienteY + 3, 80, 80, null);
            g2d.setColor(Color.BLACK);
        }
    }

    public void disegnaProgressBar(Graphics2D g2d) {
        int progressBarWidth = width;
        int progressBarHeight = 20;
        int x = clienteX;
        int y = 80;

        if (progressBar.getValue() > 0) {
            if (!prodottoComprato) {
                int filledWidth = (int) ((progressBar.getValue() / 100.0) * progressBarWidth);

                g2d.setColor(progressBar.getProgressColor());
                g2d.fillRect(x, y, filledWidth, progressBarHeight);

                g2d.setColor(Color.BLACK);

                g2d.setFont(new Font("Arial", Font.BOLD, 15));
                g2d.drawString(String.valueOf(secondiRimasti) + " S", x + 95, y + 16);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(x, y, progressBarWidth, progressBarHeight);
            }
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
                    if (prodottoComprato)
                        break;
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

        clienteAspetta = true;
        clienteEntrato = true;
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
                Thread.sleep(10); // Controllo del movimento
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
                Thread.sleep(10); // Controllo del movimento
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
        if (soddisfatto) {
            System.out.println(nome + " e' stato soddisfatto: ");
        } else {
            System.out.println(nome + " non e' stato soddisfatto: ");
        }
        panificioMonitor.exitPanificio(nome);
        stop();
    }

    public boolean isClienteAspetta() {
        return clienteAspetta;
    }

    public static boolean isClienteEntrato() {
        return clienteEntrato;
    }

    public boolean isSoddisfatto() {
        return soddisfatto;
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

    public String getNome() {
        return nome;
    }

    public Prodotto getProdottoDesiderato() {
        return prodottoDesiderato;
    }

    public void setSoddisfatto(boolean stato) {
        this.soddisfatto = stato;
    }

    public void setProdottoComprato(boolean stato) {
        this.prodottoComprato = stato;
    }

}
