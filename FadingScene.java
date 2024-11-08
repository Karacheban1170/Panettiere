import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class FadingScene {
    private static float blackOpacity = 1f;
    private static Timer fadeInTimer;

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

    public static void disegnaFadingRect(Graphics2D g2d) {
        // Disegna un rettangolo nero semi-trasparente per il fading
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FadingScene.getBlackOpacity()));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, PanificioFrame.getWidthFrame(), PanificioFrame.getHeightFrame());

        // Ripristina la trasparenza completa per il disegno normale
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    /**
     * @return the blackOpacity
     */
    public static float getBlackOpacity() {
        return blackOpacity;
    }

    /**
     * @param blackOpacity the blackOpacity to set
     */
    public static void setBlackOpacity(float blackOpacity) {
        FadingScene.blackOpacity = blackOpacity;
    }

    /**
     * @return the fadeInTimer
     */
    public static Timer getFadeInTimer() {
        return fadeInTimer;
    }

    /**
     * @param fadeInTimer the fadeInTimer to set
     */
    public static void setFadeInTimer(Timer fadeInTimer) {
        FadingScene.fadeInTimer = fadeInTimer;
    }

}
