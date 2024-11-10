import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ProgressBar extends JPanel {

    private final int minValue, maxValue;
    private int currentValue;
    private ArrayList<Rectangle> rects = new ArrayList<>();
    private Color PROGRESS_COLOR = Color.blue;
    private int removeValue = 0;

    public ProgressBar(int min, int max) {
        maxValue = max;
        minValue = min;
        currentValue = maxValue;
        setBorder(new LineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;

        rects.clear();
        int rectWidths = getWidth() / maxValue;
        int startingX = 0;

        if (currentValue < maxValue) {
            for (int i = minValue; i < currentValue; i++) {
                rects.add(new Rectangle(startingX, 0, rectWidths, getHeight()));
                startingX += rectWidths;
            }
        } else {
            for (int i = minValue; i < (maxValue - removeValue); i++) {
                rects.add(new Rectangle(startingX, 0, rectWidths, getHeight()));
                startingX += rectWidths;
            }
        }

        for (Rectangle r : rects) {
            g2d.setColor(PROGRESS_COLOR);
            g2d.fillRect(r.x, r.y, r.width, r.height);
        }
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
        if (value < currentValue) {
            removeValue++;
        } else {
            int rem = value - currentValue;
            removeValue -= rem;
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