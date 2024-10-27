import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class ChangeContentPaneListener implements ActionListener {
    final String panelToShow;

    public ChangeContentPaneListener(String panelToShow) {
        this.panelToShow = panelToShow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PanificioGUI frame = (PanificioGUI) SwingUtilities.getWindowAncestor((JButton) e.getSource());
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), panelToShow);
    }
}