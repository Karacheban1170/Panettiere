import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * La classe FadingScene gestisce l'effetto di fade-in su una scena disegnando
 * un rettangolo nero semi-trasparente che diminuisce progressivamente in
 * opacità.
 * Questo effetto è utile per transizioni di scena fluide.
 * Utilizza un Timer per ridurre l'opacità del rettangolo ad ogni tick
 * fino a renderlo completamente trasparente.
 * 
 * @author Gruppo7
 */
public class FadingScene {
    private static float blackOpacity = 1f;
    private static Timer fadeInTimer;

    /**
     * Avvia l'effetto di fade-in riducendo progressivamente l'opacità del
     * rettangolo nero.
     * Il valore di blackOpacity diminuisce ad ogni tick del timer fino a
     * raggiungere 0.
     */
    public static void fadingIn() {
        fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackOpacity -= 0.05f; // Riduce l'opacità del nero
                if (blackOpacity <= 0f) {
                    blackOpacity = 0f; // Limita l'opacità al minimo
                    fadeInTimer.stop();
                }
            }

        });
        blackOpacity = 1f;
        fadeInTimer.start();
    }

    /**
     * Disegna un rettangolo nero semi-trasparente per l'effetto di fading.
     * L'opacità del rettangolo è definita dalla variabile blackOpacity.
     *
     * @param g2d Il contesto grafico su cui disegnare il rettangolo di fading.
     */
    public static void disegnaFadingRect(Graphics2D g2d) {
        // Disegna un rettangolo nero semi-trasparente per il fading
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FadingScene.getBlackOpacity()));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, PanificioFrame.getWidthFrame(), PanificioFrame.getHeightFrame());

        // Ripristina la trasparenza completa per il disegno normale
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    /**
     * Ottiene l'opacità corrente del rettangolo nero.
     *
     * @return L'opacità attuale del rettangolo nero.
     */
    public static float getBlackOpacity() {
        return blackOpacity;
    }

    /**
     * Imposta l'opacità corrente del rettangolo nero.
     *
     * @param blackOpacity L'opacità del rettangolo nero da impostare.
     */
    public static void setBlackOpacity(float blackOpacity) {
        FadingScene.blackOpacity = blackOpacity;
    }

    /**
     * Ottiene il timer utilizzato per il fade-in.
     *
     * @return Il Timer che gestisce il fade-in.
     */
    public static Timer getFadeInTimer() {
        return fadeInTimer;
    }

    /**
     * Imposta un nuovo timer per il fade-in.
     *
     * @param fadeInTimer Il Timer da impostare per il fade-in.
     */
    public static void setFadeInTimer(Timer fadeInTimer) {
        FadingScene.fadeInTimer = fadeInTimer;
    }

}
