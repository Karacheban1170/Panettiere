import java.awt.image.BufferedImage;

public class Prodotto {
    private final BufferedImage immagine;
    private int quantita;

    public Prodotto(BufferedImage immagine, int quantita) {
        this.immagine = immagine;
        this.quantita = quantita;
    }

    public BufferedImage getImage() {
        return immagine;
    }

    public int getQuantita() {
        return quantita;
    }

    public void incrementaQuantita() {
        quantita++;
    }

    public void decrementaQuantita() {
        if (quantita > 0) {
            quantita--;
        }
    }

    
}
