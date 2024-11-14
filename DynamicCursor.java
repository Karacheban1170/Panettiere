
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * La classe DynamicCursor gestisce la personalizzazione e il comportamento
 * dinamico
 * del cursore del mouse in base all'interazione con vari componenti del gioco
 * "Panificio".
 * 
 * @author Gruppo7
 */
public class DynamicCursor {

    private static Cursor defaultCursor, selectCursor, transparentSelectCursor;

    /**
     * Imposta i cursori personalizzati per il pannello specificato.
     * 
     * @param panello il JPanel su cui impostare i cursori personalizzati.
     */
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

    /**
     * Aggiorna il cursore in base alla posizione del mouse e dell'intersezione del
     * panettiere
     * con l'area del bancone o della porta nel pannello "Panificio".
     * 
     * @param panello          il JPanel su cui aggiornare il cursore.
     * @param panettiereBounds i confini del panettiere.
     * @param banconeBounds    i confini del bancone.
     * @param portaBounds      i confini della porta.
     */
    public static void updateCursor(JPanel panello, Rectangle panettiereBounds, Rectangle banconeBounds,
            Rectangle portaBounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            boolean isOverBancone = isMouseOverBounds(mousePosition, banconeBounds);
            boolean isOverPorta = isMouseOverBounds(mousePosition, portaBounds);

            boolean isIntersectsBancone = isPanettiereIntersectsBounds(panettiereBounds, banconeBounds);
            boolean isIntersectsPorta = isPanettiereIntersectsBounds(panettiereBounds, portaBounds);

            if ((isOverBancone && isIntersectsBancone) ||
                    (isOverPorta && isIntersectsPorta)) {
                panello.setCursor(selectCursor);
            } else if (isOverBancone || isOverPorta) {
                panello.setCursor(transparentSelectCursor);
            } else {
                panello.setCursor(defaultCursor);
            }
        }
    }

    /**
     * Aggiorna il cursore in base alla posizione del mouse nel pannello "Bancone",
     * considerando le aree del pulsante Indietro, dei prodotti e del pulsante
     * Forno.
     * 
     * @param panello           il JPanel su cui aggiornare il cursore.
     * @param btnIndietroBounds i confini del pulsante Indietro.
     * @param btnFornoBounds    i confini del pulsante Forno.
     * @param prodottiBounds    lista dei confini dei prodotti.
     */
    public static void updateCursor(JPanel panello, Rectangle btnIndietroBounds, Rectangle btnFornoBounds,
            ArrayList<Rectangle> prodottiBounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            String area = "";

            // Determina in quale area si trova il mouse
            if (isMouseOverBounds(mousePosition, btnIndietroBounds)) {
                area = "btnIndietro";
            } else if (isMouseOverArrayBounds(mousePosition, prodottiBounds)) {
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

    /**
     * Aggiorna il cursore in base alla posizione del mouse nel pannello "Forno",
     * considerando le aree del pulsante Bancone, dei nuovi prodotti, degli
     * ingredienti
     * e del libro delle ricette.
     * 
     * @param panello             il JPanel su cui aggiornare il cursore.
     * @param btnBanconeBounds    i confini del pulsante Bancone.
     * @param ingredientiBounds   lista dei confini degli ingredienti.
     * @param nuovoProdottoBounds i confini del nuovo prodotto.
     * @param libroRicetteBounds  i confini del libro delle ricette.
     */
    public static void updateCursor(JPanel panello, Rectangle btnBanconeBounds,
            ArrayList<Rectangle> ingredientiBounds, Rectangle nuovoProdottoBounds, Rectangle libroRicetteBounds) {
        Point mousePosition = panello.getMousePosition();
        if (mousePosition != null) {
            String area = "";

            // Determina in quale area si trova il mouse
            if (isMouseOverBounds(mousePosition, btnBanconeBounds)) {
                area = "btnBancone";
            } else if (isMouseOverBounds(mousePosition, nuovoProdottoBounds)) {
                area = "nuovoProdotto";
            } else if (isMouseOverArrayBounds(mousePosition, ingredientiBounds)) {
                area = "ingredienti";
            } else if (isMouseOverBounds(mousePosition, libroRicetteBounds)) {
                area = "libroRicette";
            }

            // Imposta il cursore in base all'area determinata
            switch (area) {
                case "btnBancone":
                    panello.setCursor(selectCursor);
                    break;
                case "nuovoProdotto":
                    panello.setCursor(selectCursor);
                    break;
                case "libroRicette":
                    panello.setCursor(selectCursor);
                    break;
                case "ingredienti":
                    if (!Forno.isProdottoStaCuocendo()) {
                        panello.setCursor(selectCursor);
                    } else {
                        panello.setCursor(transparentSelectCursor);
                    }
                    break;

                default:
                    panello.setCursor(defaultCursor);
                    break;
            }
        }
    }

    /**
     * Verifica se il panettiere si trova all'interno dei confini specificati.
     * 
     * @param panettiereBounds i confini del panettiere.
     * @param bounds           i confini da controllare.
     * @return true se il panettiere interseca i confini specificati, false
     *         altrimenti.
     */
    public static boolean isPanettiereIntersectsBounds(Rectangle panettiereBounds, Rectangle bounds) {
        panettiereBounds = Panettiere.getPanettiereBounds();
        return panettiereBounds != null && panettiereBounds.intersects(bounds);
    }

    /**
     * Verifica se il mouse si trova all'interno dei confini specificati.
     * 
     * @param mousePoint la posizione del mouse.
     * @param bounds     i confini da controllare.
     * @return true se il mouse si trova all'interno dei confini, false altrimenti.
     */
    public static boolean isMouseOverBounds(Point mousePoint, Rectangle bounds) {
        if (bounds != null) {
            return bounds.contains(mousePoint);
        }
        return false;
    }

    /**
     * Verifica se il mouse si trova all'interno di uno qualsiasi dei confini
     * specificati
     * in una lista di prodotti.
     * 
     * @param mousePoint     la posizione del mouse.
     * @param prodottiBounds lista dei confini dei prodotti.
     * @return true se il mouse si trova all'interno di uno dei confini, false
     *         altrimenti.
     */
    private static boolean isMouseOverArrayBounds(Point mousePoint, ArrayList<Rectangle> prodottiBounds) {
        for (int i = 0; i < prodottiBounds.size(); i++) {
            if (prodottiBounds.get(i).contains(mousePoint)) {
                return true;
            }
        }
        return false;
    }

}