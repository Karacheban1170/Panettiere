import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class GestioneAudio {

    private Clip clip;
    private FloatControl controlloVolume;

    public GestioneAudio(String percorso) {
        try {
            File audioFile = new File(percorso);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                controlloVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.out.println("Il controllo del volume non è supportato su questo sistema.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.getMessage();
        }
    }

    public void playBackgroundMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void playSound() {
        if (clip != null) {
            clip.stop(); // Ferma qualsiasi suono attuale
            clip.setFramePosition(0); // Riporta il clip all'inizio
            clip.start(); // Avvia la riproduzione
        }
    }

    public void stopSound() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void setVolume(float volume) {
        if (controlloVolume != null) {
            float min = controlloVolume.getMinimum();
            float max = controlloVolume.getMaximum();
            float dB = min + (max - min) * volume;
            controlloVolume.setValue(dB);
        }
    }
}
