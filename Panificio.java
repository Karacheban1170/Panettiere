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

    private Cliente cliente = new Cliente(this);;
    private BufferedImage panettiereImage;
    private BufferedImage sfondoImage;
    private BufferedImage banconeImage;
    private boolean isSecondaScena = false;

    private int panettiereX = 500; // Posizione iniziale del panettiere
    private int panettiereY = 200; // Altezza fissa del panettiere
    private int panettiereWidth = 250;
    private int panettiereHeight = 410;

    private final int PANETTIERE_VELOCITA = 20;

    private boolean movimentoSinistra = false;

    // Definire il rettangolo-collider del bancone (posizione e dimensioni)
    private Rectangle colliderBancone = new Rectangle(400, 435, 520, 130);

    private ArrayList<Rectangle> colliderProdotti = new ArrayList<>();

    public Panificio() {

        
        Thread clienteThread = new Thread(cliente);
        clienteThread.start();
        
        try {
            sfondoImage = ImageIO.read(new File("img/panificio_sfondo.jpg"));
            banconeImage = ImageIO.read(new File("img/panificio_bancone.png"));
            panettiereImage = ImageIO.read(new File("img/panettiere.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true); // Imposta il pannello come focus per la tastiera
        requestFocusInWindow();

        // Inizializza MouseAdapter e KeyListener
        initMouseAndKeyListener();
    }

    private void initMouseAndKeyListener() {
        // MouseAdapter per entrambe le scene
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isSecondaScena) {
                    // Verifica se il clic è su uno dei prodotti
                    for (Rectangle colliderProdotto : colliderProdotti) {
                        if (colliderProdotto.contains(e.getPoint())) {
                            // Esegui la logica dell'acquisto
                            System.out.println("Prodotto selezionato! Cliente può tornare indietro.");
                            if (cliente != null && cliente.isFermo()) {
                                cliente.acquistaProdotto(); // Il cliente torna indietro
                            }
                            break;
                        }
                    }
                } else {
                    // Verifica se il panettiere è in collisione con il bancone
                    Rectangle colliderPanettiere = new Rectangle(panettiereX, panettiereY, panettiereWidth - 150,
                            panettiereHeight);

                    if (colliderBancone.intersects(colliderPanettiere)) {
                        if (SwingUtilities.isLeftMouseButton(e) && colliderBancone.contains(e.getPoint())) {
                            isSecondaScena = true;
                        }
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
                    movimentoSinistra = true;
                    panettiereX = Math.max(panettiereX - PANETTIERE_VELOCITA, 0); // Limiti a sinistra
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    movimentoSinistra = false;
                    panettiereX = Math.min(panettiereX + PANETTIERE_VELOCITA, getWidth() - panettiereWidth); // Limiti a
                                                                                                             // destra
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
            g2d.translate(panettiereX + panettiereWidth, panettiereY); // Posizionare il panettiere
            g2d.scale(-1, 1); // Riflettere orizzontalmente
            g2d.drawImage(panettiereImage, 0, 0, panettiereWidth, panettiereHeight, null);
        } else {
            g2d.drawImage(panettiereImage, panettiereX, panettiereY, panettiereWidth, panettiereHeight, null);
        }

        // Ripristina la trasformazione originale
        g2d.setTransform(trasformazioneOriginale);

        g2d.drawImage(banconeImage, 0, 0, getWidth(), getHeight(), null);

        // Gestione bordi (fermare il panettiere completamente ai limiti)
        if (panettiereX <= 0) {
            panettiereX = 0;
        } else if (panettiereX + panettiereWidth >= getWidth()) {
            panettiereX = getWidth() - panettiereWidth;
        }
    }

    private void banconePanello(Graphics2D g2d) {


        
        

        // Dimensioni e spaziatura dei prodotti
        int prodottoWidth = 80;
        int prodottoHeight = 80;
        int spacing = 50;
        int numeroProdotti = 4;
        int totalWidth = numeroProdotti * prodottoWidth + (numeroProdotti - 1) * spacing;

        int startX = (getWidth() - totalWidth) / 2;

        ArrayList<BufferedImage> prodottiImages = new ArrayList<>();
        colliderProdotti.clear();

        try {
            prodottiImages.add(ImageIO.read(new File("img/ciambella1.png")));
            prodottiImages.add(ImageIO.read(new File("img/muffin1.png")));
            prodottiImages.add(ImageIO.read(new File("img/pane1.png")));
            prodottiImages.add(ImageIO.read(new File("img/croissant1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creazione del bancone
        g2d.setColor(Color.DARK_GRAY);
        int banconeAltezza = 200;
        g2d.fillRect(0, getHeight() - banconeAltezza, getWidth(), banconeAltezza);

        for (int i = 0; i < prodottiImages.size(); i++) {
            int x = startX + i * (prodottoWidth + spacing);
            int y = getHeight() - banconeAltezza + 50;

            if (prodottiImages.get(i) != null) {
                g2d.drawImage(prodottiImages.get(i), x, y, prodottoWidth, prodottoHeight, null);
            }

            // Aggiungi i rettangoli collider per i prodotti
            colliderProdotti.add(new Rectangle(x, y, prodottoWidth, prodottoHeight));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (isSecondaScena) {
            // Seconda scena (bancone)
            cliente.disegnaCliente(g2d);
            banconePanello(g2d);
            
        } else {
            // Prima scena (panificio)
            panificioPanello(g2d);
        }
    }
}
