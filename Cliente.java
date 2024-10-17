import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Cliente extends Thread {

    // Caratteristiche del cliente
    private static final int CLIENTE_VELOCITA = 20;
    private int clienteX = -300; // Inizia fuori dalla schermata
    private int clienteY = 200; // Altezza del cliente (a livello della banchina)
    private int clienteWidth = 300;
    private int clienteHeight = 600;
    private boolean movimentoSinistra = false;
    private boolean fermo = false;

    private String nome;

    private PanificioGUI panificio;
    private PanificioMonitor panificioMonitor;
    private BufferedImage clienteImage;

    public Cliente(PanificioGUI panificio, PanificioMonitor panificioMonitor, String nome) {
        this.panificioMonitor = panificioMonitor;
        this.panificio = panificio;
        this.nome = nome;
        try {
            clienteImage = ImageIO.read(new File("img/cliente1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acquistaProdotto() {
        fermo = false; // Inizia il movimento verso sinistra
        movimentoSinistra = true; // Cambia direzione dopo l'acquisto
    }

    @Override
    public void run() {
        panificioMonitor.enterPanificio(nome); // Cliente entra nel panificio
        System.out.println("Cliente " + nome + " si trova nel panificio");
        while (true) {

            int centroDelBancone = (panificio.getWidth() / 2) - (clienteWidth / 2);
            if (!fermo) {
                if (!movimentoSinistra) {
                    // Muovi il cliente verso il centro della banchina
                    clienteX = Math.min(clienteX + CLIENTE_VELOCITA, centroDelBancone);
                    if (clienteX >= centroDelBancone) {
                        fermo = true; // Ferma il cliente al centro
                    }
                } else {
                    // Muovi il cliente verso l'uscita (lato sinistro)
                    clienteX = Math.max(clienteX - CLIENTE_VELOCITA, -clienteWidth);
                    if (clienteX == -300) {
                        panificioMonitor.exitPanificio(nome);
                        break;
                    }
                }
            }

            panificio.repaint(); // Ridisegna il panificio

            try {
                Thread.sleep(100); // Rallenta il movimento per renderlo più fluido
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Cliente esce dal panificio
    public void disegnaCliente(Graphics2D g2d) {
        if (movimentoSinistra) {
            // Riflettere l'immagine del cliente quando si muove a sinistra
            AffineTransform trasformazioneOriginale = g2d.getTransform();
            g2d.translate(clienteX + clienteWidth, clienteY);
            g2d.scale(-1, 1); // Riflettere orizzontalmente
            g2d.drawImage(clienteImage, 0, 0, clienteWidth, clienteHeight, null);
            g2d.setTransform(trasformazioneOriginale); // Ripristina la trasformazione originale
        } else {
            // Disegna il cliente normalmente
            g2d.drawImage(clienteImage, clienteX, clienteY, clienteWidth, clienteHeight, null);
        }
    }

    public boolean isFermo() {
        return fermo;
    }

}
