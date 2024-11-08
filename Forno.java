import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    private final ArrayList<Rectangle> ingredientiBounds;

    public Forno(int width, int height, ActionListener toPnlBanconeAction) {
        this.width = width;
        this.height = height;
        this.sfondo = ImageLoader.loadImage("img/forno_sfondo.jpg");
        this.btnBancone = ImageLoader.loadImage("img/btn_bancone.png");

        setLayout(null);

        ingredienti = new ArrayList<>(4);
        ingredientiBounds = new ArrayList<>(ingredienti.size());
        aggiungiIngredienti();

        btnBanconeBounds = new Rectangle(width - 181, 392, 105, 105);
        this.toPnlBanconeAction = toPnlBanconeAction;

        addMouseListener(this);
        DynamicCursor.setCustomCursors(this);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                DynamicCursor.updateCursor(this, btnBanconeBounds, ingredientiBounds);
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

    private void aggiungiIngredienti() {
        int startX = width / 2 - 180; // Coordinata iniziale x centrata dinamicamente
        int yPos = 415; // Posizione fissa verticale

        // Aggiungi ingredienti con posizioni calcolate dinamicamente
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente1.png"), "acqua"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente2.png"), "farina"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente3.png"), "latte"));
        ingredienti.add(new Prodotto(ImageLoader.loadImage("img/ingrediente4.png"), "uova"));

        int offsetX = 100; // Distanza orizzontale tra ingredienti
        int currentX = startX;

        for (Prodotto ingrediente : ingredienti) {
            ingrediente.setBounds(currentX, yPos, 60, 60);
            ingrediente.setFixedPositionBancone(new Point(currentX, yPos));

            ingredientiBounds.add(ingrediente.getBounds());
            add(ingrediente);
            currentX += offsetX;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        disegnaSfondo(g2d);

        disegnaCornici(g2d);
        disegnaIngredienti(g2d);

        disegnaBtnBancone(g2d);

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

    private void disegnaCornici(Graphics2D g2d) {

        Graphics2D g2dTrans = (Graphics2D) g2d.create();
        g2dTrans.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        // Assumiamo che ingredienti contenga gli oggetti Prodotto
        for (Prodotto ingrediente : ingredienti) {
            // Cornice 1 - Forno 1
            Rectangle forno1Frame = new Rectangle(ingrediente.getFixedPositionForno1().x,
                    ingrediente.getFixedPositionForno1().y, 60, 60);
            g2dTrans.setColor(Color.BLACK); // Colore nero per la prima cornice
            g2dTrans.fill(forno1Frame);

            // Cornice 2 - Forno 2
            Rectangle forno2Frame = new Rectangle(ingrediente.getFixedPositionForno2().x,
                    ingrediente.getFixedPositionForno2().y, 60, 60);
            g2dTrans.setColor(Color.BLACK); // Colore nero per la seconda cornice
            g2dTrans.fill(forno2Frame);
        }

        // Cornice Centrale (Rosso)
        Rectangle centraleFrame = new Rectangle(width / 2 - 30, height / 2 + 30, 60, 60); // Posizione centrale
        g2dTrans.setColor(Color.RED); // Colore rosso per la cornice centrale
        g2dTrans.fill(centraleFrame);
    }

    private void disegnaIngredienti(Graphics2D g2d) {
        for (int i = 0; i < ingredienti.size(); i++) {
            Prodotto ingrediente = ingredienti.get(i);
            if (ingrediente.getImage() != null) {
                Point location = ingrediente.getLocation();

                g2d.drawImage(ingrediente.getImage(), location.x, location.y, ingrediente.getWidth(),
                        ingrediente.getHeight(), this);

                if (i < ingredientiBounds.size()) {
                    ingredientiBounds.get(i).setBounds(location.x, location.y, ingrediente.getWidth(),
                            ingrediente.getHeight());
                } else {
                    ingredientiBounds.add(
                            new Rectangle(location.x, location.y, ingrediente.getWidth(), ingrediente.getHeight()));
                }
            }
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

        }
    }

}
