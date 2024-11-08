import java.awt.image.BufferedImage;

public class Prodotto {
    private final BufferedImage immagine;
    private int quantita;
    private final String nome;

    public Prodotto(BufferedImage immagine, String nome, int quantita) {
        this.immagine = immagine;
        this.nome = nome;
        this.quantita = quantita;
    }

    public Prodotto(BufferedImage immagine, String nome) {
        this.nome = nome;
        this.immagine = immagine;
    }

    public BufferedImage getImage() {
        return immagine;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void incrementaQuantita() {
        quantita++;
    }

    public void decrementaQuantita() {
        if (quantita > 0 && Cliente.isClienteAspetta()) {
            quantita--;
        }
    }

    
}
