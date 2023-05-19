
package components;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.JFrame;
import org.opencv.core.*;
import javax.swing.JPanel;

public class PostTimingGUI extends JPanel {
    private ArrayList<DraggableLine> finishes;
    private static CompositeFrame finishImage;
    private static ArrayList<SingleFrame> OCRstream;
    private OutputProcessor processor;
    private int check = 0;
    public PostTimingGUI(CompositeFrame image, ArrayList<SingleFrame> ocr) {
        // TODO complete constructor

        OCRstream = ocr;
        finishes = new ArrayList<DraggableLine>(4);
        finishes.add(new DraggableLine(new TimeFormat(), "a", 75));
        finishes.add(new DraggableLine(new TimeFormat(), "a", 50));
        finishes.add(new DraggableLine(new TimeFormat(), "a", 25));

        finishImage = image;
        processor = new OutputProcessor(finishes);
    }

    public void addListener()
    {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                finishes.add(new DraggableLine(new TimeFormat(), "a", e.getX()));
                repaint();
                
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                check = e.getX();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                for (int i = 0; i < finishes.size(); i++)
                {
                    if (finishes.get(i).getXPos() == check 
                    || finishes.get(i).getXPos() == check - 1
                    || finishes.get(i).getXPos() == check + 1)
                    {
                        finishes.get(i).changeXPos(e.getX());
                        e.translatePoint(e.getX(), 0);
                        repaint();
                    }
                }
                
                
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        });
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        for (int i = 0; i < finishes.size(); i++)
        {
            g.drawLine(finishes.get(i).getXPos(), 0, 
            finishes.get(i).getXPos(), this.getHeight());
        }
    }


     private int getOCR(int xPos) {
        // TODO don't worry about this one right now
        // each draggable line gets the ocr, every time the draggable line is moved
         return null;
     }

    public static void run() {
        // TODO GUI code, treat like a main method
        PostTimingGUI run = new PostTimingGUI(finishImage, OCRstream);
        run.addListener();
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        frame.add(run);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        run();
    }
}
