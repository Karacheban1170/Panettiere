import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;

public class Panificio extends JPanel implements Runnable, FocusListener, MouseListener {
    private Image miaIm;
    private Graphics mg;
    private Thread runner;

    protected int score;
    protected int bestScore;

    private Font ft = new Font("Dialog", Font.BOLD, 20);
    private Font ftScore = new Font("Dialog", Font.BOLD, 18);

    protected Panettiere objPanettiere;

    private JFrame finestra;

    private int width;
    private int height;

    private boolean focussed = false;

    public Panificio(int w, int h) {
        score = 0;

        this.width = w;
        this.height = h;
        this.miaIm = null;

        finestra = new JFrame("Gioco Panificio");
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setBounds(0, 0, width + 6, height + 100);

        Container iContainer = finestra.getContentPane();
        iContainer.add(this);

        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        finestra.setVisible(true);

        objPanettiere = new Panettiere(width, height, this);

        addFocusListener(this);
        addKeyListener(objPanettiere);
        addMouseListener(this);
    }

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void stop() {
        if (runner != null) {
            runner = null;
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (width != getSize().width || height != getSize().height) {
            setup();
        }
        if (miaIm != null) {
            drawFrame(mg, width, height);
            objPanettiere.panettiereFrame(mg, width, height);
            g.drawImage(miaIm, 0, 0, this);
            paintStatistiche(g);
            if (!focussed) {
                paintIstruzioni(g);
            }
        }
    }

    private void paintStatistiche(Graphics g) {
        try {
            Scanner f = new Scanner(new File("bestScore.txt"));
            if (f.hasNextInt())
                bestScore = f.nextInt();
            f.close();
            g.setColor(Color.yellow);
            g.setFont(ftScore);
            g.drawString("Best Score: " + bestScore, width - 150, 30);
        } catch (FileNotFoundException e) {
            g.setColor(Color.yellow);
            g.setFont(ftScore);
            g.drawString("Best Score: --", width - 150, 30);
        }
        g.setColor(Color.yellow);
        g.setFont(ftScore);
        g.drawString("Score: " + score, 10, 30);
    }

    private void paintIstruzioni(Graphics g) {
        g.setColor(Color.black);
        g.setFont(ft);
        g.drawString("Cliccare per iniziare!", width / 2 - 100, height / 2 - 100);
    }

    public void setup() {
        width = getSize().width;
        height = getSize().height;
        miaIm = createImage(width, height);
        mg = miaIm.getGraphics();
        mg.setFont(ft);
        mg.setColor(Color.red);
    }

    public void run() {
        Thread iThread = Thread.currentThread();
        iThread.setPriority(Thread.MIN_PRIORITY);

        while (runner == iThread) {
            try {
                runner.sleep(25);
            } catch (Exception e) {
                System.out.println(e);
            }
            repaint();
        }
    }

    public void focusGained(FocusEvent e) {
        focussed = true;
        repaint();
    }

    public void focusLost(FocusEvent e) {
        focussed = false;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        requestFocus();
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

    protected void drawFrame(Graphics g, int width, int height) {
        // Imposta il colore di sfondo o disegna l'immagine di sfondo del panificio
        Image sfondo = Toolkit.getDefaultToolkit().getImage("panificio_background.jpg"); // Assicurati di avere un'immagine
        g.drawImage(sfondo, 0, 0, this);
    }

}
