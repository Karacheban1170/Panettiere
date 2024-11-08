
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class DynamicCursor {

    private static Cursor defaultCursor, selectCursor, transparentSelectCursor;

    public static void setCustomCursors(JPanel panello) {
        BufferedImage defaultCursorImage = ImageLoader.loadImage("img/default_cursor.png");
        BufferedImage selectCursorImage = ImageLoader.loadImage("img/select_cursor.png");
        BufferedImage transparentSelectCursorImage = ImageLoader.loadImage("img/trasparent_select_cursor.png");

        if (defaultCursorImage != null) {
            defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(defaultCursorImage, new Point(0, 0),
                    "Default Cursor");
        }
        if (selectCursorImage != null) {
            selectCursor = Toolkit.getDefaultToolkit().createCustomCursor(selectCursorImage, new Point(0, 0),
                    "Select Cursor");
        }
        if (transparentSelectCursorImage != null) {
            transparentSelectCursor = Toolkit.getDefaultToolkit().createCustomCursor(transparentSelectCursorImage,
                    new Point(0, 0),
                    "Transparent Select Cursor");
        }

        panello.setCursor(defaultCursor);
    }

    // Metodo UpdateCursor per "Panificio"
    public static void updateCursor(JPanel panello, Rectangle panettiereBounds, Rectangle bounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            if (isMouseOverBounds(mousePosition, bounds)) {
                if (isPanettiereIntersectsBounds(panettiereBounds, bounds)) {
                    panello.setCursor(selectCursor);
                } else {
                    panello.setCursor(transparentSelectCursor);
                }
            } else {
                panello.setCursor(defaultCursor);
            }
        }
    }

    // Metodo UpdateCursor per "Bancone"
    public static void updateCursor(JPanel panello, Rectangle btnIndietroBounds, Rectangle btnFornoBounds,
            ArrayList<Rectangle> prodottiBounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            String area = "";

            // Determina in quale area si trova il mouse
            if (isMouseOverBounds(mousePosition, btnIndietroBounds)) {
                area = "btnIndietro";
            } else if (isMouseOverProdottiBounds(mousePosition, prodottiBounds)) {
                area = "prodotti";
            } else if (isMouseOverBounds(mousePosition, btnFornoBounds)) {
                area = "btnForno";
            }

            // Imposta il cursore in base all'area determinata
            switch (area) {
                case "btnIndietro":
                    if (PanificioMonitor.isClientiEntrano()) {
                        panello.setCursor(transparentSelectCursor);
                    } else {
                        panello.setCursor(selectCursor);
                    }
                    break;

                case "prodotti":
                    if (Cliente.isClienteEntrato()) {
                        panello.setCursor(selectCursor);
                    } else {
                        panello.setCursor(transparentSelectCursor);
                    }
                    break;

                case "btnForno":
                    panello.setCursor(selectCursor);
                    break;

                default:
                    panello.setCursor(defaultCursor);
                    break;
            }
        }
    }

    // Metodo UpdateCursor per "Forno"
    public static void updateCursor(JPanel panello, Rectangle btnBanconeBounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            if (isMouseOverBounds(mousePosition, btnBanconeBounds)) {
                panello.setCursor(selectCursor);
            } else {
                panello.setCursor(defaultCursor);
            }
        }
    }

    public static boolean isPanettiereIntersectsBounds(Rectangle panettiereBounds, Rectangle bounds) {
        panettiereBounds = Panettiere.getPanettiereBounds();
        return panettiereBounds != null && panettiereBounds.intersects(bounds);
    }

    public static boolean isMouseOverBounds(Point mousePoint, Rectangle bounds) {
        return bounds.contains(mousePoint);
    }

    private static boolean isMouseOverProdottiBounds(Point mousePoint, ArrayList<Rectangle> prodottiBounds) {
        for (int i = 0; i < prodottiBounds.size(); i++) {
            if (prodottiBounds.get(i).contains(mousePoint)) {
                return true;
            }
        }
        return false;
    }

}