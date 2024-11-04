import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class CustomProgressBarTest {

    public CustomProgressBarTest() {
        createAndShowGui();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomProgressBarTest();
            }
        });
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final MyProgressBar myProgressBar = new MyProgressBar(0, 100);
        myProgressBar.setProgressColor(new Color(0, 255, 0, 127));

        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 300);
            }
        };

        panel.add(new JLabel());

        frame.add(panel);

        frame.add(myProgressBar, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);

        createAndStartDecrementTimer(myProgressBar);
    }

    private void createAndStartDecrementTimer(final MyProgressBar myProgressBar) {
        Timer progressBArCountDownTimer = new Timer(50, new AbstractAction() {
            int count = 100;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (count == 0) {
                    System.out.println("Done");
                    ((Timer) ae.getSource()).stop();
                } else if (count > 0) {
                    count--;
                    myProgressBar.setValue(count);
                    System.out.println(myProgressBar.getValue());
                }
            }
        });
        progressBArCountDownTimer.start();
    }
}

class MyProgressBar extends JPanel {

    private final int minValue, maxValue;
    private int currentValue;
    private ArrayList<Rectangle> rects = new ArrayList<>();
    private Color PROGRESS_COLOR = Color.blue;
    private int removeValue = 0;

    public MyProgressBar(int min, int max) {
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