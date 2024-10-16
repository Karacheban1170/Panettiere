import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class PanificioPanello extends JPanel {

    private BufferedImage panettiereImage;
    private BufferedImage sfondoImage;
    private boolean inSecondaScena = false;

    private int panettiereX = 600; // Posizione iniziale del panettiere
    private int panettiereY = 250; // Altezza fissa del panettiere

    private final int PANETTIERE_VELOCITA = 20;

    private boolean movimentoSinistra = false;

    // Definire il rettangolo della banchina (posizione e dimensioni)
    private Rectangle banchinaRettangolo = new Rectangle(600, 490, 100, 50);

    public PanificioPanello() {
        try {
            // Carichiamo l'immagine di sfondo e del panettiere
            sfondoImage = ImageIO.read(new File("img/panificio_sfondo.jpg"));
            panettiereImage = ImageIO.read(new File("img/panettiere.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true); // Imposta il pannello come focus per la tastiera
        requestFocusInWindow(); // Richiedi il focus in finestra

        // MouseListener per cambiare scena quando il panettiere è sulla banchina
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Verifica se il panettiere è in collisione con il rettangolo verde (banchina)
                Rectangle panettiereRettangolo = new Rectangle(panettiereX, panettiereY, 300, 450);

                if (banchinaRettangolo.intersects(panettiereRettangolo)) {
                    // Verifica se il click è avvenuto all'interno del rettangolo verde
                    if (SwingUtilities.isLeftMouseButton(e) && banchinaRettangolo.contains(e.getPoint())) {
                        inSecondaScena = true; // Passa alla seconda scena
                    }
                }
                repaint(); // Ridisegna il pannello per mostrare la seconda scena
            }
        });

        // Aggiungiamo un KeyListener per rilevare i movimenti
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (inSecondaScena) {
            // Seconda scena (banchina)
            g2d.setColor(Color.GREEN);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Prima scena (panificio)
            g2d.drawImage(sfondoImage, 0, 0, getWidth(), getHeight(), null);

            // Salva la trasformazione originale
            AffineTransform trasformazioneOriginale = g2d.getTransform();

            // Verifica se il panettiere si sta muovendo a sinistra
            if (movimentoSinistra) {
                // Applicare una riflessione orizzontale per il movimento a sinistra
                g2d.translate(panettiereX + 400, panettiereY); // Posizionare il panettiere
                g2d.scale(-1, 1); // Riflettere orizzontalmente
                g2d.drawImage(panettiereImage, 0, 0, 400, 450, null);
            } else {
                // Disegnare il panettiere normalmente
                g2d.drawImage(panettiereImage, panettiereX, panettiereY, 400, 450, null);
            }

            // Ripristina la trasformazione originale
            g2d.setTransform(trasformazioneOriginale);

            // Disegna il rettangolo della banchina
            g2d.setColor(Color.RED); // Rettangolo visibile
            g2d.drawRect(banchinaRettangolo.x, banchinaRettangolo.y, banchinaRettangolo.width,
                    banchinaRettangolo.height);

            // Cambia il colore della banchina se il panettiere collide con essa
            Rectangle panettiereRettangolo = new Rectangle(panettiereX, panettiereY, 300, 450);
            if (banchinaRettangolo.intersects(panettiereRettangolo)) {
                g2d.setColor(Color.GREEN); // Cambia colore se c'è una collisione
                g2d.fillRect(banchinaRettangolo.x, banchinaRettangolo.y, banchinaRettangolo.width,
                        banchinaRettangolo.height);
            }
        }
    }

}
