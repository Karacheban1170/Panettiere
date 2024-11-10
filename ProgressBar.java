import java.awt.Color;
import javax.swing.JPanel;

public class ProgressBar extends JPanel {

    private final int minValue, maxValue;
    private int currentValue;
    private static Color PROGRESS_COLOR = Color.GREEN;

    public ProgressBar(int min, int max) {
        maxValue = max;
        minValue = min;
        currentValue = maxValue;
    }

    int getValue() {
        return currentValue;
    }

    void setValue(int value) {
        if (value > maxValue) {
            return;
        }
        if (value < minValue) {
            return;
        }
    
        currentValue = value;
        repaint();
    }

    void setProgressColor(Color c) {
        PROGRESS_COLOR = c;
    }

    Color getProgressColor() {
        return PROGRESS_COLOR;
    }
}