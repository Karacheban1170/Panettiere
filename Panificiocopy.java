// import java.awt.*;
// import java.awt.event.*;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.util.Scanner;
// import javax.swing.*;

// public class Panificio extends JPanel implements FocusListener, MouseListener {

//     private Graphics g;

//     // protected Panettiere objPanettiere;

//     // private int width;
//     // private int height;

//     private boolean focussed = false;

//     public Panificio() {

//         // objPanettiere = new Panettiere(width, height, this);

//         addFocusListener(this);
//         // addKeyListener(objPanettiere);
//         // addMouseListener(this);
//     }

//     @Override
//     public void paint(Graphics g) {
//         if (width != getSize().width || height != getSize().height) {
//             setup();
//         }
//         drawFrame(g);
//         // paintStatistiche(g);
//         if (!focussed) {
//             // paintIstruzioni(g);
//         }
//     }

//     @Override
//     public void update(Graphics g) {
//         paint(g);
//     }

//     // private void paintStatistiche(Graphics g) {
//     // try {
//     // Scanner f = new Scanner(new File("bestScore.txt"));
//     // if (f.hasNextInt())
//     // bestScore = f.nextInt();
//     // f.close();
//     // g.setColor(Color.yellow);
//     // g.setFont(ftScore);
//     // g.drawString("Best Score: " + bestScore, width - 150, 30);
//     // } catch (FileNotFoundException e) {
//     // g.setColor(Color.yellow);
//     // g.setFont(ftScore);
//     // g.drawString("Best Score: --", width - 150, 30);
//     // }
//     // g.setColor(Color.yellow);
//     // g.setFont(ftScore);
//     // g.drawString("Score: " + score, 10, 30);
//     // }

//     // private void paintIstruzioni(Graphics g) {
//     // g.setColor(Color.black);
//     // g.setFont(ft);
//     // g.drawString("Cliccare per iniziare!", width / 2 - 100, height / 2 - 100);
//     // }

//     // public void setup() {
//     // width = getSize().width;
//     // height = getSize().height;
//     // miaIm = createImage(width, height);
//     // mg = miaIm.getGraphics();
//     // mg = miaIm.getGraphics();
//     // mg.setFont(ft);
//     // mg.setFont(ft);
//     // mg.setColor(Color.red);
//     // mg.setColor(Color.red);
//     // }

//     // public void focusGained(FocusEvent e) {
//     // focussed = true;
//     // repaint();
//     // }

//     // public void focusLost(FocusEvent e) {
//     // focussed = false;
//     // repaint();
//     // }

//     // public void mousePressed(MouseEvent e) {
//     // requestFocus();
//     // }

//     // public void mouseEntered(MouseEvent e) {}
//     // public void mouseExited(MouseEvent e) {}
//     // public void mouseReleased(MouseEvent e) {}
//     // public void mouseClicked(MouseEvent e) {}

//     private void drawFrame(Graphics g) {
//         // Imposta il colore di sfondo o disegna l'immagine di sfondo del panificio
//         Image sfondo = Toolkit.getDefaultToolkit().getImage("img/panificio_sfondo.jpg");
//         // Assicurati di avere un'immagine
//         g.drawImage(sfondo, 0, 0, this);
//     }

// }
