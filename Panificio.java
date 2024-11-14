import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class Panificio extends JPanel implements Runnable, FocusListener, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final int width, height;

    private boolean focussed = false;
    private boolean clickAllowed = false;

    private final BufferedImage sfondo;
    private final Panettiere panettiere;
    private final BufferedImage bancone;

    private final BufferedImage spunta;
    private final BufferedImage croce;

    private final Rectangle banconeBounds;
    private final Rectangle portaBounds;

    private final ActionListener toPnlBanconeAction;
    private boolean scoreFinaleAperto;

    private final GestioneAudio apparizioneScore;

    public Panificio(int width, int height, ActionListener toPnlBanconeAction) {
        this.width = width;
        this.height = height;
        this.sfondo = ImageLoader.loadImage("img/panificio_sfondo.jpg");
        this.bancone = ImageLoader.loadImage("img/panificio_bancone.png");

        this.spunta = ImageLoader.loadImage("img/spunta.png");
        this.croce = ImageLoader.loadImage("img/croce.png");

        panettiere = new Panettiere(this);
        scoreFinaleAperto = false;

        portaBounds = new Rectangle(width - 155, 180, 75, 230);
        banconeBounds = new Rectangle((width / 2) - 230, (height / 2) + 45, 400, 100);

        apparizioneScore = new GestioneAudio("audio/apparizione_score.wav");
        apparizioneScore.setVolume(0.7f);

        this.toPnlBanconeAction = toPnlBanconeAction;
        addKeyListener(panettiere);
        addFocusListener(this);
        addMouseListener(this);

        DynamicCursor.setCustomCursors(this);

        FadingScene.fadingIn();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                DynamicCursor.updateCursor(this, Panettiere.getPanettiereBounds(), banconeBounds, portaBounds);
                repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public synchronized void start() {
        running = true;

        updateThread = new Thread(this);
        updateThread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        disegnaSfondo(g2d);
        panettiere.disegnaPanettiere(g2d);
        disegnaBancone(g2d);

        if (!focussed) {
            disegnaIstruzioni(g2d);
        }

        if (scoreFinaleAperto) {

            disegnaScoreFinale(g2d);
        }

        FadingScene.disegnaFadingRect(g2d);
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
            g2d.drawImage(bancone, 0, 0, width, height, this);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(banconeBounds.x, banconeBounds.y, banconeBounds.width, banconeBounds.height);
        }
    }

    private void disegnaIstruzioni(Graphics2D g2d) {
        // Imposta il colore e la trasparenza per lo sfondo delle istruzioni
        g2d.setColor(new Color(0, 0, 0, 150)); // Nero semi-trasparente
        g2d.fillRect(0, 0, width, height); // Rettangolo di sfondo

        // Testo di istruzioni
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Usa i tasti per muovere il panettiere", width / 2 - 150, height / 2 - 130);
        g2d.drawString("Clicca con il mouse per interagire!", width / 2 - 150, height / 2 + 80);

        // Carica e disegna le immagini dei tasti A, D e del mouse
        BufferedImage tastoA = ImageLoader.loadImage("img/tastoA.png");
        BufferedImage tastoD = ImageLoader.loadImage("img/tastoD.png");
        BufferedImage frecciaSinistra = ImageLoader.loadImage("img/freccia_sinistra.png");
        BufferedImage frecciaDestra = ImageLoader.loadImage("img/freccia_destra.png");
        BufferedImage mouse = ImageLoader.loadImage("img/mouse.png");

        int keySize = 60; // Dimensioni delle icone

        // Posizioni per le immagini
        int baseX = width / 2; // Coordinate X di base per il posizionamento
        int baseY = height / 2; // Coordinate Y di base per il posizionamento

        // Controlla e disegna le immagini delle frecce
        if (frecciaSinistra != null) {
            g2d.drawImage(frecciaSinistra, baseX - 130, baseY - 100, keySize, keySize, this); // Sinistra
        }
        if (frecciaDestra != null) {
            g2d.drawImage(frecciaDestra, baseX - 50, baseY - 100, keySize, keySize, this); // Destra
        }

        // Controlla e disegna le immagini dei tasti A e D
        if (tastoA != null) {
            g2d.drawImage(tastoA, baseX - 130, baseY - 20, keySize, keySize, this); // Tasto A
        }
        if (tastoD != null) {
            g2d.drawImage(tastoD, baseX - 50, baseY - 20, keySize, keySize, this); // Tasto D
        }

        int mouseWidth = 88;
        int mouseHeight = 140;

        // Controlla e disegna l'immagine del mouse
        if (mouse != null) {
            g2d.drawImage(mouse, baseX + 55, baseY - 100, mouseWidth, mouseHeight, this); // Mouse
        }
    }

    private void disegnaScoreFinale(Graphics2D g2d) {
        // Sfondo semitrasparente
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, width, height);

        // Impostazioni di posizione per i nomi dei clienti e le icone
        int xPosLeft = width / 3 - 35; // Colonna sinistra
        int xPosRight = xPosLeft + 250; // Colonna destra
        int yPos = 50;
        int sizeIcon = 50;
        int maxPerColonna = 5; // Numero massimo di clienti per colonna

        // Leggi il risultato migliore da un file esterno
        int migliorRisultato = 0;
        try (Scanner sc = new Scanner(new File("risultato_migliore.txt"))) {
            if (sc.hasNextInt()) {
                migliorRisultato = sc.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.getMessage();
            migliorRisultato = 0;
        }

        ArrayList<Cliente> clienti = Bancone.getClienti();
        BufferedImage imgSoddisfatto;

        if (clienti != null) {
            xPosLeft = width / 3 - 35; // Colonna sinistra
            xPosRight = xPosLeft + 250; // Colonna destra
            yPos = 50;
            for (int i = 0; i < clienti.size(); i++) {
                // Determina l'icona in base alla soddisfazione del cliente
                if (clienti.get(i).isSoddisfatto()) {
                    imgSoddisfatto = spunta;
                } else {
                    imgSoddisfatto = croce;
                }

                // Imposta la colonna e la posizione Y
                int xPos;
                if (i < maxPerColonna) {
                    xPos = xPosLeft;
                } else {
                    xPos = xPosRight;
                }
                int currentYPos = yPos + (i % maxPerColonna) * 70;

                // Disegna il nome del cliente
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 15));
                g2d.drawString(clienti.get(i).getNome(), xPos, currentYPos + 30);

                // Disegna l'icona
                g2d.drawImage(imgSoddisfatto, xPos + 100, currentYPos, sizeIcon, sizeIcon, this);
            }
        }

        // Mostra il punteggio finale a sinistra
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        g2d.drawString("Risultato Finale: ", xPosLeft, yPos + (maxPerColonna + 1) * 61);
        g2d.drawString(String.valueOf(Bancone.getScore()), xPosLeft + 70, yPos + (maxPerColonna + 1) * 66);

        // Mostra il miglior risultato a destra
        g2d.drawString("Risultato Migliore: ", xPosRight, yPos + (maxPerColonna + 1) * 61);
        g2d.drawString(String.valueOf(migliorRisultato), xPosRight + 70, yPos + (maxPerColonna + 1) * 66);

    }

    @Override
    public void focusGained(FocusEvent e) {
        focussed = true;
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        focussed = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickAllowed = true;
        requestFocusInWindow();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (clickAllowed && e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (DynamicCursor.isMouseOverBounds(mousePosition, banconeBounds)
                    && DynamicCursor.isPanettiereIntersectsBounds(Panettiere.getPanettiereBounds(), banconeBounds)) {
                Bancone.setScore(0);
                if (toPnlBanconeAction != null) {
                    toPnlBanconeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            } else if (DynamicCursor.isMouseOverBounds(mousePosition, portaBounds)
                    && DynamicCursor.isPanettiereIntersectsBounds(Panettiere.getPanettiereBounds(), portaBounds)
                    && scoreFinaleAperto == false) {
                scoreFinaleAperto = true;
                if(Bancone.getClienti() != null){
                    apparizioneScore.playSound();
                }
                
            } else {
                scoreFinaleAperto = false;
            }

        }
    }

}
