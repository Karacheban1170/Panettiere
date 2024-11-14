import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * La classe ImageLoader si occupa del caricamento delle immagini da file. 
 * Utilizza la classe ImageIO per leggere e restituire un'istanza di BufferedImage.
 * 
 * Questa classe è utile per caricare immagini da file nel formato supportato da ImageIO, come PNG, JPEG, GIF, ecc.
 * 
 * @author Gruppo7
 */
public class ImageLoader {
    /**
     * Carica un'immagine da un file specificato dal percorso.
     * 
     * @param path Il percorso del file dell'immagine da caricare.
     * @return L'immagine caricata come BufferedImage, o null se si è verificato un errore durante il caricamento.
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
