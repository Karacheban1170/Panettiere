import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PanificioGUI extends JPanel {

    private PanificioMonitor panificioMonitor = new PanificioMonitor();

    private Panettiere panettiere = new Panettiere(this);
    private ArrayList<Cliente> clienti = new ArrayList<>(2);
    // private Cliente cliente = new Cliente(this, new PanificioMonitor(), "Marco");

    private BufferedImage sfondoImage;
    private BufferedImage banconeImage;
    private boolean isSecondaScena = false;

    // Definire il rettangolo-collider del bancone (posizione e dimensioni)
    private Rectangle colliderBancone = new Rectangle(400, 435, 520, 130);

    private ArrayList<Rectangle> colliderProdotti = new ArrayList<>();

    public PanificioGUI() {

        System.out.println("Panificio apre");
        // Crea 5 clienti
        Cliente cliente1 = new Cliente(this, new File("img/cliente1.png"), panificioMonitor, "Cliente 1");
        // Cliente cliente2 = new Cliente(this, new File("img/cliente2.png"), panificioMonitor, "Cliente 2");
        // Cliente cliente3 = new Cliente(this, new File("img/cliente3.png"), panificioMonitor, "Cliente 3");

        clienti.add(cliente1);
        // clienti.add(cliente2);
        // clienti.add(cliente3);

        try {
            sfondoImage = ImageIO.read(new File("img/panificio_sfondo.jpg"));
            banconeImage = ImageIO.read(new File("img/panificio_bancone.png"));
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
                    // Itera sull'array di clienti per trovare chi può acquistare
                    for (Cliente cliente : clienti) {
                        if (cliente != null && cliente.isFermo()) {
                            // Verifica se il clic del mouse è su uno dei prodotti
                            for (Rectangle colliderProdotto : colliderProdotti) {
                                if (colliderProdotto.contains(e.getPoint())) {
                                    // Logica dell'acquisto
                                    System.out.println("Prodotto selezionato! Cliente può tornare indietro.");
                                    cliente.acquistaProdotto();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    // Verifica se il panettiere è in collisione con il bancone
                    Rectangle colliderPanettiere = new Rectangle(panettiere.getPanettiereX(),
                            panettiere.getPanettiereY(), panettiere.getPanettiereWidth() - 150,
                            panettiere.getPanettiereHeight());

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
                    panettiere.muoviSinistra();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    panettiere.muoviDestra();
                }
                repaint(); // Ridisegna il panettiere nella nuova posizione
            }
        });
    }

    private void panificioPanello(Graphics2D g2d) {
        g2d.drawImage(sfondoImage, 0, 0, getWidth(), getHeight(), null);

        panettiere.disegnaPanettiere(g2d);

        g2d.drawImage(banconeImage, 0, 0, getWidth(), getHeight(), null);
    }

    private void banconePanello(Graphics2D g2d) {
        clienti.get(1).disegnaCliente(g2d);

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
            banconePanello(g2d);

        } else {
            // Prima scena (panificio)
            panificioPanello(g2d);
        }
    }

    public void avviaClienti() {

        for (int i = 0; i < clienti.size(); i++) {
            if (clienti.get(i) != null) {
                clienti.get(i).start();
            }
        }

        for (int i = 0; i < clienti.size(); i++) {
            if (clienti.get(i) != null) {
                try {
                    clienti.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
