import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cliente implements Runnable {
    private int x;
    private final int y;
    private final int width;
    private final int height;
    private String nome;
    private static final int VELOCITA_CLIENTE = 10;
    private final Bancone pnlBancone;
    private final BufferedImage imgCliente;

    private final PanificioMonitor panificioMonitor;

    private Thread clienteThread;
    private boolean running = true;

    private boolean movimentoSinistra = false;

    public Cliente(BufferedImage imgCliente, Bancone pnlBancone, PanificioMonitor panificioMonitor, String nome) {
        this.panificioMonitor = panificioMonitor;
        this.imgCliente = imgCliente;
        this.pnlBancone = pnlBancone;
        this.width = 200;
        this.height = width + 150;
        this.x = -width - 50;
        this.y = 120;
        this.nome = nome;
    }

    @Override
    public void run() {
        while (running) {
            entraCliente();

            // Una volta entrato, il cliente si ferma e aspetta un momento
            try {
                Thread.sleep(3000); // Simula l'azione del cliente all'interno del panificio
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
                g2d.translate(x + width, y);
                g2d.scale(-1, 1);
                g2d.drawImage(imgCliente, 0, 0, width, height, null);
            } else {
                g2d.drawImage(imgCliente, x, y, width, height, null);
            }
            g2d.setTransform(originalTransform);
        }
    }

    public void entraCliente() {
        movimentoSinistra = false;
        int centroBancone = pnlBancone.getWidth() / 2 - width / 2;
        panificioMonitor.enterPanificio(nome);
        while (x < centroBancone) {
            x += VELOCITA_CLIENTE; // Muovi il cliente a destra
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
        while (x > -width) {
            x -= VELOCITA_CLIENTE; // Muovi il cliente a sinistra
            try {
                Thread.sleep(50); // Controllo del movimento
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
        panificioMonitor.exitPanificio(nome);
        stop();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
