import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * La classe GestioneAudio si occupa della gestione della riproduzione di file
 * audio
 * (sia suoni che musica di sottofondo) all'interno dell'applicazione.
 * Permette di avviare, fermare e regolare il volume dell'audio.
 * 
 * Questa classe supporta la riproduzione ciclica di musica di sottofondo e la
 * riproduzione di suoni singoli.
 * È possibile anche controllare il volume tramite il tipo di controllo
 * "MASTER_GAIN".
 * 
 * @author Gruppo7
 */
public class GestioneAudio {

    private Clip clip;
    private FloatControl controlloVolume;

    /**
     * Costruttore che inizializza la gestione dell'audio con il percorso del file
     * audio.
     * Carica il file audio e, se supportato, permette la regolazione del volume.
     * 
     * @param percorso Il percorso del file audio da caricare.
     */
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

    /**
     * Avvia la riproduzione della musica di sottofondo in loop continuo.
     */
    public void playBackgroundMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    /**
     * Avvia la riproduzione di un suono (non in loop), fermando eventuali suoni
     * precedenti.
     */
    public void playSound() {
        if (clip != null) {
            clip.stop(); // Ferma qualsiasi suono attuale
            clip.setFramePosition(0); // Riporta il clip all'inizio
            clip.start(); // Avvia la riproduzione
        }
    }

    /**
     * Ferma la riproduzione dell'audio e chiude il clip.
     */
    public void stopSound() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Imposta il volume dell'audio. Il volume è un valore compreso tra 0.0 (minimo)
     * e 1.0 (massimo).
     * 
     * @param volume Il valore del volume da impostare, compreso tra 0.0 e 1.0.
     */
    public void setVolume(float volume) {
        if (controlloVolume != null) {
            float min = controlloVolume.getMinimum();
            float max = controlloVolume.getMaximum();
            float dB = min + (max - min) * volume;
            controlloVolume.setValue(dB);
        }
    }
}
