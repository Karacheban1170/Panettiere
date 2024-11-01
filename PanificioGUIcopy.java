// import java.awt.*;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;
// import javax.swing.*;

// public class PanificioGUI extends JFrame {

    
//     private JPanel pnlMain;
//     private JPanel pnlBancone;

//     private JButton toPnlMain, toPnlBancone;
//     private JLabel mainBackground;
//     private JLabel pnlBanconeBackground;
//     private Prodotto[] elencoProdotti;

//     private Panettiere panettiere;

//     public PanificioGUI() {
//         super("Panificio");

//         setSize(800, 600);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         // Impostare la finestra a schermo intero
//         setExtendedState(JFrame.MAXIMIZED_BOTH);

//         setFocusable(true);
//         requestFocusInWindow();

//         initComponenti();
//         initPannelli();
//         initAscoltatori();
//     }

//     private void initComponenti() {

//         mainBackground = new JLabel(new ImageIcon("img/panificio_sfondo"));

//         // toPnlBancone = new JButton("Vai al secondo pannello");
//         // toPnlBancone.addActionListener(e -> showPanel(pnlBanconeBackground));

//         // toPnlMain = new JButton("Torna al pannello principale");
//         // toPnlMain.addActionListener(e -> showPanel(pnlMain));

//         // panettiere = new Panettiere("img/panettiere.png");
//         // elencoProdotti = new Prodotto[5];

//         // for (int i = 0; i <= 4; i++) {
//         //     elencoProdotti[i] = new Prodotto("img/prodotto" + i + ".png");
//         // }
//     }

//     private void initPannelli() {
//         pnlMain = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         // pnlBancone = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         // pnlBanconeBackground = new PanificioPanel("img/bancone_sfondo.jpg");
        

//         pnlMain.add(mainBackground);
//         // pnlMain.add(panettiere);
//         // pnlMain.add(toPnlBancone);


//         // for (int i = 0; i <= 4; i++) {
//         //     pnlBancone.add(elencoProdotti[i]);
//         // }

//         // pnlBanconeBackground.add(pnlBancone);

//         // Aggiungi i pannelli con BorderLayout
//         // getContentPane().add(pnlBanconeBackground, BorderLayout.CENTER); // pannello bancone al centro
//         getContentPane().add(pnlMain); // pannello principale al centro
//         // getContentPane().add(pnlBancone, BorderLayout.SOUTH); // pannello bancone in basso

//         // Visualizza il pnlMain all'avvio
//         // showPanel(pnlMain);
//     }

//     // private void showPanel(JPanel panelToShow) {
//     //     pnlMain.setVisible(false);
//     //     pnlBanconeBackground.setVisible(false);
//     //     pnlBancone.setVisible(false);
//     //     panelToShow.setVisible(true);
//     // }

//     private void initAscoltatori() {
//         addKeyListener(new KeyAdapter() {
//             @Override
//             public void keyPressed(KeyEvent e) {
//                 if (e.getKeyCode() == KeyEvent.VK_A) {
//                     panettiere.muoviSinistra();
//                     System.out.println("A");
//                     System.out.println(panettiere.getLocation());

//                 } else if (e.getKeyCode() == KeyEvent.VK_D) {
//                     panettiere.muoviDestra();
//                     System.out.println(panettiere.getLocation());
//                     System.out.println("D");
//                 }
//                 panettiere.repaint();
//             }
//         });
//     }

// }

// // class BanconePanel extends JPanel {

// //     private Image banconeImg;

// //     public BanconePanel(String banconeImgPath) {
// //         banconeImg = new ImageIcon(banconeImgPath).getImage();
// //     }

// //     @Override
// //     public void paintComponent(Graphics g) {
// //         super.paintComponent(g);

// //         // Disegna l'immagine del bancone
// //         g.drawImage(banconeImg, 0, getHeight() - banconeImg.getHeight(this), getWidth(), banconeImg.getHeight(this), this);
// //     }
// // }

// // class PanificioPanel extends JPanel {

// //     private Image img;

// //     public PanificioPanel(String imgPath) {
// //         img = new ImageIcon(imgPath).getImage();
// //     }

// //     @Override
// //     public void paintComponent(Graphics g) {
// //         super.paintComponent(g); // Chiama il metodo di superclasse
// //         g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Ridimensiona l'immagine
// //     }
// // }
