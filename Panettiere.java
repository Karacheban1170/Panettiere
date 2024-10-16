public class Panettiere implements Runnable {
    private final Panificio panificio;

    public Panettiere(Panificio panificio) {
        this.panificio = panificio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Il panettiere produce pane
                panificio.produrrePane();
                // Attende un po' prima di produrre altro pane
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


