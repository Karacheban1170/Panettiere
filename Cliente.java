public class Cliente implements Runnable {
    private Panificio panificio;

    public Cliente(Panificio panificio) {
        this.panificio = panificio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Il cliente acquista pane
                panificio.acquistarePane();
                // Attende un po' prima di ritornare
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}