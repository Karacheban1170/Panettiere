import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

public class Forno extends JPanel implements Runnable, MouseListener {

    private Thread updateThread;
    private boolean running = true;

    private final int width, height;

    private final BufferedImage sfondo;

    private final BufferedImage btnBancone;
    private final Rectangle btnBanconeBounds;

    private final ActionListener toPnlBanconeAction;

    private final ArrayList<Prodotto> ingredienti;
    private final ArrayList<BufferedImage> ingredientiFrame;
    private final ArrayList<Rectangle> ingredientiBounds;

    private Prodotto nuovoProdotto;
    private Rectangle nuovoProdottoBounds;

    private final ProgressBar progressBar;
    private final Color GREEN_COLOR;
    private final int secondi;

    private final Map<Set<String>, Prodotto> ricette;

    public Forno(int width, int height, ActionListener toPnlBanconeAction) {
        this.width = width;
        this.height = height;
        this.sfondo = ImageLoader.loadImage("img/forno_sfondo.jpg");
        this.btnBancone = ImageLoader.loadImage("img/btn_bancone.png");

        setLayout(null);

        ingredienti = new ArrayList<>(4);
        ingredientiBounds = new ArrayList<>(ingredienti.size());
        ingredientiFrame = new ArrayList<>(ingredienti.size());
        nuovoProdotto = null;
        ricette = new HashMap<>();
        aggiungiIngredienti();
        caricaRicette();

        nuovoProdottoBounds = new Rectangle();
        btnBanconeBounds = new Rectangle(width - 181, 392, 105, 105);
        this.toPnlBanconeAction = toPnlBanconeAction;

        // Inizializza la barra di progresso e aggiungila al pannello
        GREEN_COLOR = new Color(0, 255, 0, 200);
        secondi = 10;
        progressBar = new ProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(width / 2, 20));
        progressBar.setProgressColor(GREEN_COLOR);

        addMouseListener(this);
        DynamicCursor.setCustomCursors(this);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                DynamicCursor.updateCursor(this, btnBanconeBounds, ingredientiBounds, nuovoProdottoBounds);
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

        disegnaFornoFrame(g2d);
        cuociProdotto();
        disegnaProgressBar(g2d);

        disegnaBtnBancone(g2d);

        disegnaIngredientiFrame(g2d);
        disegnaIngredienti(g2d);

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

    private void disegnaFornoFrame(Graphics2D g2d) {

        Graphics2D g2dTrans = (Graphics2D) g2d.create();
        g2dTrans.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        // Assumiamo che ingredienti contenga gli oggetti Prodotto
        // Cornice 1 - Forno 1
        Rectangle forno1Frame = new Rectangle(Prodotto.getFixedPositionForno1().x,
                Prodotto.getFixedPositionForno1().y, 80, 80);
        g2dTrans.setColor(Color.BLACK); // Colore nero per la prima cornice
        g2dTrans.fill(forno1Frame);

        // Cornice 2 - Forno 2
        Rectangle forno2Frame = new Rectangle(Prodotto.getFixedPositionForno2().x,
                Prodotto.getFixedPositionForno2().y, 80, 80);
        g2dTrans.setColor(Color.BLACK); // Colore nero per la seconda cornice
        g2dTrans.fill(forno2Frame);

        // Cornice Centrale (Rosso)
        Rectangle centraleFrame = new Rectangle(Prodotto.getPositionCentraleNuovoProdotto().x,
                Prodotto.getPositionCentraleNuovoProdotto().y, 80, 80); // Posizione centrale
        g2dTrans.setColor(Color.RED); // Colore rosso per la cornice centrale
        g2dTrans.fill(centraleFrame);
    }

    private void aggiungiIngredienti() {
        int startX = width / 4; // Coordinata iniziale x centrata dinamicamente
        int yPos = 400; // Posizione fissa verticale

        ingredientiFrame.add(ImageLoader.loadImage("img/ingredienteFrame1.png"));
        ingredientiFrame.add(ImageLoader.loadImage("img/ingredienteFrame2.png"));
        ingredientiFrame.add(ImageLoader.loadImage("img/ingredienteFrame3.png"));
        ingredientiFrame.add(ImageLoader.loadImage("img/ingredienteFrame4.png"));
        ingredientiFrame.add(ImageLoader.loadImage("img/ingredienteFrame5.png"));

        // Aggiungi ingredienti con posizioni calcolate dinamicamente
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente1.png"), "acqua"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente2.png"), "farina"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente3.png"), "latte"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente4.png"), "uova"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente5.png"), "cioccolato"));

        int offsetX = 100; // Distanza orizzontale tra ingredienti
        int currentX = startX;

        for (int i = 0; i < ingredienti.size(); i++) {
            Prodotto ingrediente = ingredienti.get(i);

            ingrediente.setBounds(currentX, yPos, 80, 80);
            ingrediente.setFixedPositionBancone(new Point(currentX, yPos));

            ingredientiBounds.add(ingrediente.getBounds());
            add(ingrediente);

            currentX += offsetX;
        }
    }

    private void disegnaIngredientiFrame(Graphics2D g2d) {
        int xPos = width / 4;
        int yPos = 400;

        for (int i = 0; i < ingredientiFrame.size(); i++) {
            if (ingredientiFrame.get(i) != null) {
                // Disegna il prodotto
                g2d.drawImage(ingredientiFrame.get(i), xPos, yPos, 80, 80, this);

                xPos += 100;
            }
        }
    }

    private void disegnaIngredienti(Graphics2D g2d) {
        for (int i = 0; i < ingredienti.size(); i++) {
            Prodotto ingrediente = ingredienti.get(i);
            if (ingrediente.getImage() != null) {
                Point location = ingrediente.getLocation();

                g2d.drawImage(ingrediente.getImage(), location.x, location.y,
                        ingrediente.getWidth(),
                        ingrediente.getHeight(), this);

                if (i < ingredientiBounds.size()) {
                    ingredientiBounds.get(i).setBounds(location.x, location.y,
                            ingrediente.getWidth(),
                            ingrediente.getHeight());
                } else {
                    ingredientiBounds.add(
                            new Rectangle(location.x, location.y, ingrediente.getWidth(),
                                    ingrediente.getHeight()));
                }
            }
        }

        // Disegna il prodotto visualizzato, se esiste
        if (nuovoProdotto != null && nuovoProdotto.getImage() != null) {
            Point location = nuovoProdotto.getLocation();

            g2d.drawImage(nuovoProdotto.getImage(), location.x, location.y, nuovoProdotto.getWidth(),
                    nuovoProdotto.getHeight(), this);
        }

    }

    private void caricaRicette() {
        // Crea le ricette usando coppie di ingredienti
        ricette.put(Set.of("farina", "latte"), new Prodotto(ImageLoader.loadImage("img/prodotto1.png"), "ciambella"));
        ricette.put(Set.of("farina", "uova"), new Prodotto(ImageLoader.loadImage("img/prodotto2.png"), "croissant"));
        ricette.put(Set.of("farina", "cioccolato"), new Prodotto(ImageLoader.loadImage("img/prodotto3.png"), "muffin"));
        ricette.put(Set.of("farina", "acqua"), new Prodotto(ImageLoader.loadImage("img/prodotto4.png"), "pane"));
    }

    private String getProdottoInForno(Point position) {
        for (Prodotto ingrediente : ingredienti) {
            if (ingrediente.getLocation().equals(position)) {
                return ingrediente.getNome();
            }
        }
        return null;
    }

    private void cuociProdotto() {
        // Recupera ingredienti nelle posizioni del forno
        String ingr1 = getProdottoInForno(Prodotto.getFixedPositionForno1());
        String ingr2 = getProdottoInForno(Prodotto.getFixedPositionForno2());

        if (ingr1 != null && ingr2 != null) {
            Prodotto prodotto = ricette.get(Set.of(ingr1, ingr2));
            if (prodotto != null) {
                creaNuovoProdotto(ingr1, ingr2, prodotto.getImage(), prodotto.getNome());
            }
        }
    }

    private void creaNuovoProdotto(String ingr1, String ingr2, BufferedImage immagine, String nome) {
        for (Prodotto ingrediente : ingredienti) {
            if (ingrediente.getNome().equals(ingr1) || ingrediente.getNome().equals(ingr2)) {
                // Riposiziona l'ingrediente alla sua posizione originale
                ingrediente.setPosizioneIniziale();
            }
        }

        // Crea il nuovo prodotto ma non lo aggiunge alla lista
        nuovoProdotto = new Prodotto(immagine, nome);

        // Avvia il thread della progress bar senza bloccare il thread principale
        Thread progressBarThread = createAndStartDecrementThread(secondi);

        // Una volta entrato, il cliente si ferma e aspetta un momento
        try {
            progressBarThread.join();
        } catch (InterruptedException e) {
            e.getMessage();
        }

        // Non usare join qui. Non bloccare il thread principale
        // Aggiungi direttamente la logica di posizionamento del nuovo prodotto
        nuovoProdotto.setBounds(Prodotto.getPositionCentraleNuovoProdotto().x,
                Prodotto.getPositionCentraleNuovoProdotto().y, 80,
                80);

        nuovoProdottoBounds = new Rectangle(nuovoProdotto.getX(), nuovoProdotto.getY(), nuovoProdotto.getWidth(),
                nuovoProdotto.getHeight());
    }

    private void aggiungiNuovoProdotto(Point mousePosition) {
        // Verifica se il clic è all'interno dell'area del nuovo prodotto
        if (nuovoProdotto != null && DynamicCursor.isMouseOverBounds(mousePosition, nuovoProdottoBounds)) {
            // Recupera il nome del nuovo prodotto da incrementare
            String nomeNuovoProdotto = nuovoProdotto.getNome();

            // Cerca il prodotto corrispondente nel bancone e incrementa la quantità solo di
            // quel prodotto
            for (Prodotto prodotto : Bancone.getProdotti()) {
                if (prodotto.getNome().equals(nomeNuovoProdotto)) {
                    prodotto.incrementaQuantita();
                    break; // Interrompe il ciclo dopo aver trovato e incrementato il prodotto corretto
                }
            }

            nuovoProdotto = null;
            nuovoProdottoBounds = null;
        }
    }

    private void disegnaBtnBancone(Graphics2D g2d) {
        if (btnBancone != null) {
            g2d.drawImage(btnBancone, btnBancone.getWidth() + 270, btnBancone.getHeight() - 118, 125, 125,
                    this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 40, 100, width, height);
        }
    }

    public void disegnaProgressBar(Graphics2D g2d) {
        int progressBarWidth = width / 4;
        int progressBarHeight = 20;
        int x = width / 3 + 40;
        int y = height / 3;

        if (progressBar.getValue() > 0) {
            int filledWidth = (int) ((progressBar.getValue() / 100.0) * progressBarWidth);

            g2d.setColor(progressBar.getProgressColor());
            g2d.fillRect(x, y, filledWidth, progressBarHeight);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, progressBarWidth, progressBarHeight);
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
                    try {
                        Thread.sleep(delay);
                        count--;
                        progressBar.setValue(count);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
        });
        progressBarThread.start();

        // Restituiamo il thread per poter fare join() nel metodo run()
        return progressBarThread;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mousePosition = e.getPoint();
            if (DynamicCursor.isMouseOverBounds(mousePosition, btnBanconeBounds)) {
                if (toPnlBanconeAction != null) {
                    toPnlBanconeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }

            aggiungiNuovoProdotto(mousePosition);
        }
    }

}
