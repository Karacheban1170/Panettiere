import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DraggableComponent extends JComponent {
    private Point initialClick;
    private int xMoved;
    private int yMoved;

    // Due punti fissi per l'effetto "calamita"
    private final Point fixedPosition1 = new Point(50, 50);
    private final Point fixedPosition2 = new Point(200, 200);
    private final int snapDistance = 20; // Distanza per la "calamita"

    public DraggableComponent() {
        setOpaque(true);
        setBackground(Color.CYAN);

        // Imposta la posizione iniziale
        setLocation(fixedPosition1);

        // Listener per il clic iniziale del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Calcola la distanza tra la posizione corrente e le due posizioni fisse
                double distanceToFixedPosition1 = calculateDistance(getLocation(), fixedPosition1);
                double distanceToFixedPosition2 = calculateDistance(getLocation(), fixedPosition2);

                // Determina quale posizione fissa è più vicina
                if (distanceToFixedPosition1 <= distanceToFixedPosition2) {
                    setLocation(fixedPosition1);
                } else {
                    setLocation(fixedPosition2);
                }

                // Reset dei movimenti
                xMoved = 0;
                yMoved = 0;
            }
        });

        // Listener per il trascinamento del mouse
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Ottieni la posizione corrente del componente
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Calcola il movimento
                xMoved = e.getX() - initialClick.x;
                yMoved = e.getY() - initialClick.y;

                // Muovi il componente alla nuova posizione
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });
    }

    // Metodo per calcolare la distanza tra due punti
    private double calculateDistance(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    // Override per personalizzare l'aspetto
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

class DragExample extends JFrame {
    public DragExample() {
        // Crea un'istanza di DraggableComponent
        DraggableComponent draggableComponent = new DraggableComponent();
        draggableComponent.setBounds(50, 50, 100, 50);

        // Imposta layout e aggiungi il componente trascinabile
        setLayout(null);
        add(draggableComponent);

        // Impostazioni del frame
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DragExample::new);
    }
}
