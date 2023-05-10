
package components;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JPanel;
import org.opencv.core.*;

public class PostTimingGUI extends JPanel {
    private ArrayList<DraggableLine> finishes;
    private Mat finishImage;
    private OutputProcessor processor;

    public PostTimingGUI(Mat image) {
        // TODO complete constructor
        finishImage = image;
    }

    private ArrayList<DraggableLine> getOCR() {
        // TODO don't worry about this one right now
        return null;
    }

    public void run() {
        // TODO GUI code, treat like a main method
        JPanel panel = new JPanel();
        panel.setSize(200, 200);
        for (DraggableLine d : finishes) {
            panel.addMouseListener(d);
        }
        panel.setVisible(true);
    }
}
