import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Prodotto extends JComponent{

    private Image prodottoImage;

    public Prodotto(String imgPath) {
        prodottoImage = new ImageIcon(imgPath).getImage();
        setPreferredSize(new Dimension(50, 50));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Chiama il metodo di superclasse
        g.drawImage(prodottoImage, 0, 0, getWidth(), getHeight(), this); // Ridimensiona l'immagine
    }
}
