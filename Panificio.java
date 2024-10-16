import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class Panificio extends JPanel {

    private BufferedImage panettiereImage;
    private BufferedImage sfondoImage;
    private BufferedImage banconeImage;
    private boolean inSecondaScena = false;

    private int panettiereX = 500; // Posizione iniziale del panettiere
    private int panettiereY = 200; // Altezza fissa del panettiere
    private int panettiereWidth = 250;
    private int panettiereHeight = 410;

    private final int PANETTIERE_VELOCITA = 20;

    private boolean movimentoSinistra = false;

    // Definire il rettangolo-collider del bancone (posizione e dimensioni)
    private Rectangle banconeRettangolo = new Rectangle(400, 435, 520, 130);

    public Panificio() {

        try {
            sfondoImage = ImageIO.read(new File("img/panificio_sfondo.jpg"));
            banconeImage = ImageIO.read(new File("img/panificio_bancone.png"));
            panettiereImage = ImageIO.read(new File("img/panettiere.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true); // Imposta il pannello come focus per la tastiera
        requestFocusInWindow();

        // MouseListener per cambiare scena quando il panettiere è sulla bancone
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Verifica se il panettiere è in collisione con il rettangolo verde (bancone)
                Rectangle panettiereRettangolo = new Rectangle(panettiereX, panettiereY, panettiereWidth - 150, panettiereHeight);

                if (banconeRettangolo.intersects(panettiereRettangolo)) {
                    // Verifica se il click è avvenuto all'interno del rettangolo verde
                    if (SwingUtilities.isLeftMouseButton(e) && banconeRettangolo.contains(e.getPoint())) {
                        inSecondaScena = true;
                    }
                }
                repaint(); // Ridisegna il pannello per mostrare la seconda scena
            }
        });

        // KeyListener per rilevare i movimenti
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    // Muovi il panettiere a sinistra
                    movimentoSinistra = true;
                    panettiereX = Math.max(panettiereX - PANETTIERE_VELOCITA, 0); // Limiti a sinistra
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    // Muovi il panettiere a destra
                    movimentoSinistra = false;
                    panettiereX = Math.min(panettiereX + PANETTIERE_VELOCITA, getWidth() - 100); // Limiti a destra
                }
                repaint(); // Ridisegna il panettiere nella nuova posizione
            }
        });
    }

    private void panificioPanello(Graphics2D g2d) {

        g2d.drawImage(sfondoImage, 0, 0, getWidth(), getHeight(), null);

        // Salva la trasformazione originale
        AffineTransform trasformazioneOriginale = g2d.getTransform();

        // Verifica se il panettiere si sta muovendo a sinistra
        if (movimentoSinistra) {
            // Applicare una riflessione orizzontale per il movimento a sinistra
            g2d.translate(panettiereX + panettiereWidth, panettiereY); // Posizionare il panettiere
            g2d.scale(-1, 1); // Riflettere orizzontalmente
            g2d.drawImage(panettiereImage, 0, 0, panettiereWidth, panettiereHeight, null);
        } else {
            // Disegnare il panettiere normalmente
            g2d.drawImage(panettiereImage, panettiereX, panettiereY, panettiereWidth, panettiereHeight, null);
        }

        // Ripristina la trasformazione originale
        g2d.setTransform(trasformazioneOriginale);

        g2d.drawImage(banconeImage, 0, 0, getWidth(), getHeight(), null);

        /// Disegna il rettangolo del bancone

        // g2d.drawRect(banconeRettangolo.x, banconeRettangolo.y,
        // banconeRettangolo.width,
        // banconeRettangolo.height);

        /// Cambia il colore del bancone se il panettiere collide con essa

        // Rectangle panettiereRettangolo = new Rectangle(panettiereX, panettiereY,
        // panettiereWidth - 100,
        // panettiereHeight);
        // if (banconeRettangolo.intersects(panettiereRettangolo)) {
        // g2d.setColor(Color.GREEN); // Cambia colore se c'è una collisione
        // g2d.fillRect(banconeRettangolo.x, banconeRettangolo.y,
        // banconeRettangolo.width,
        // banconeRettangolo.height);
        // }

        // Gestione bordi trasparenti (limita il panettiere all'interno della scena)
        if (panettiereX < 150) {
            panettiereX = 150; // Limite sinistro
        } else if (panettiereX + 330 > getWidth()) {
            panettiereX = getWidth() - 330; // Limite destro
        }
    }

    private void banconePanello(Graphics g2d) {
        g2d.setColor(Color.DARK_GRAY);
        int banconeAltezza = 200;
        g2d.fillRect(0, getHeight() - banconeAltezza, getWidth(), banconeAltezza);

        // Dimensioni e spaziatura dei prodotti
        int prodottoWidth = 80;
        int prodottoHeight = 80;
        int spacing = 50;
        int numeroProdotti = 4;
        int totalWidth = numeroProdotti * prodottoWidth + (numeroProdotti - 1) * spacing;

        int startX = (getWidth() - totalWidth) / 2;

        ArrayList<BufferedImage> prodottiImages = new ArrayList<>();
        try {
            prodottiImages.add(ImageIO.read(new File("img/ciambella1.png")));
            prodottiImages.add(ImageIO.read(new File("img/muffin1.png")));
            prodottiImages.add(ImageIO.read(new File("img/pane1.png")));
            prodottiImages.add(ImageIO.read(new File("img/croissant1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < prodottiImages.size(); i++) {
            int x = startX + i * (prodottoWidth + spacing);

            if (prodottiImages.get(i) != null) {
                g2d.drawImage(prodottiImages.get(i), x, getHeight() - banconeAltezza + 50, prodottoWidth,
                        prodottoHeight,
                        null);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (inSecondaScena) {
            // Seconda scena (bancone)
            banconePanello(g2d);

        } else {
            // Prima scena (panificio)
            panificioPanello(g2d);
        }
    }

}