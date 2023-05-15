
package components;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.*;

public class PostTimingGUI extends JPanel {
    private ArrayList<DraggableLine> finishes;
    private CompositeFrame finishImage;
    private OutputProcessor processor;

    public PostTimingGUI(CompositeFrame image) {
        // TODO complete constructor
        finishImage = image;
        finishes = new ArrayList<DraggableLine>();
        processor = new OutputProcessor(finishes);
    }

    private ArrayList<DraggableLine> getOCR() {
        // TODO don't worry about this one right now
        // each draggable line gets the ocr, every time the draggable line is moved
        for (DraggableLine d : finishes) {
            if (d.isDragged())
                ;
        }
        return null;
    }

    public void run() {
        // TODO GUI code, treat like a main method
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        for (DraggableLine d : finishes) {
            frame.addMouseListener(d);
        }
        frame.setVisible(true);
    }
}
