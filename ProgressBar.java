import java.awt.Color;
import javax.swing.JPanel;

/**
 * Rappresenta una barra di progresso personalizzata che può essere utilizzata per visualizzare il progresso di un'operazione.
 * La barra può essere aggiornata con valori compresi tra un minimo e un massimo, e il colore della barra può essere modificato.
 * 
 * @author Gruppo7
 */
public class ProgressBar extends JPanel {

    private final int minValue, maxValue;
    private int currentValue;
    private static Color PROGRESS_COLOR = Color.GREEN;

    /**
     * Costruttore che inizializza la barra di progresso con i valori minimo e massimo.
     * La barra parte con il valore massimo.
     * 
     * @param min Il valore minimo della barra di progresso.
     * @param max Il valore massimo della barra di progresso.
     */
    public ProgressBar(int min, int max) {
        maxValue = max;
        minValue = min;
        currentValue = maxValue;
    }

    /**
     * Restituisce il valore corrente della barra di progresso.
     * 
     * @return Il valore corrente della barra di progresso.
     */
    public int getValue() {
        return currentValue;
    }

     /**
     * Imposta il valore corrente della barra di progresso.
     * Se il valore fornito è fuori dal range definito da minValue e maxValue, non viene modificato.
     * 
     * @param value Il nuovo valore da impostare.
     */
    public void setValue(int value) {
        if (value > maxValue) {
            return;
        }
        if (value < minValue) {
            return;
        }

        currentValue = value;
        repaint();
    }

    /**
     * Imposta il colore della barra di progresso.
     * 
     * @param c Il colore da impostare per la barra di progresso.
     */
    public void setProgressColor(Color c) {
        PROGRESS_COLOR = c;
    }

    /**
     * Restituisce il colore corrente della barra di progresso.
     * 
     * @return Il colore corrente della barra di progresso.
     */
    public Color getProgressColor() {
        return PROGRESS_COLOR;
    }
}