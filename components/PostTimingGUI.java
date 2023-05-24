
package components;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.opencv.core.*;

public class PostTimingGUI extends JPanel {
    private ArrayList<DraggableLine> finishes;
    private static CompositeFrame finishImage;
    private static ArrayList<SingleFrame> OCRstream;
    private OutputProcessor processor;
    private int check = 0;

    public PostTimingGUI(CompositeFrame image, ArrayList<SingleFrame> ocr) {
        // TODO complete constructor

        OCRstream = ocr;
        finishes = new ArrayList<DraggableLine>();
        finishes.add(new DraggableLine(new TimeFormat(), 5, 25));
        finishImage = image;
        processor = new OutputProcessor(finishes);
    }

    public void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    finishes.add(new DraggableLine(new TimeFormat(), 5, e.getX()));
                    System.out.println("Clicked at " + e.getX());
                    repaint();
                }
                else if (SwingUtilities.isRightMouseButton(e))
                {
                    for (int i = 0; i < finishes.size(); i++) {
                        if (finishes.get(i).getXPos() == e.getX()) {
                            finishes.remove(finishes.get(i));
                            repaint();
                        }
                }
            }
                

            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                check = e.getX();
                System.out.println("Pressed at " + check);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                for (int i = 0; i < finishes.size(); i++) {
                    if (Math.abs(finishes.get(i).getXPos() - check) <= 5 /* Threshold for click error */) {
                        System.out.println("drag release detected");
                        finishes.get(i).changeXPos(e.getX());
                        // getOCR(finishes.get(i).getXPos()); This line is OK, just need to disable while testing
                        e.translatePoint(e.getX(), 0);
                        repaint();
                        add(new JButton("" + finishes.get(i).getHipNumber() + " " ));
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
        for (int i = 0; i < finishes.size(); i++) {
            g.drawLine(finishes.get(i).getXPos(), 0, finishes.get(i).getXPos(), this.getHeight());
        }
    }

    private int getOCR(int xPos) {
        // TODO don't worry about this one right now
        // each draggable line gets the ocr, every time the draggable line is moved
        // find which frame has the x position, find where it is in the finished image
        // call the athlete ocr on it
        int i = 0;
        for (DraggableLine d : finishes) {
            if (d.getXPos() == xPos) {
                d.updateTimestamp();
                break;
            }
            i++;
        }
        Mat ret = null;
        for (SingleFrame f : OCRstream) {
            if (f.getTime() == finishes.get(i).getTimestamp()) {
                ret = f.getMat();
            }
        }

        return getAthleteNumber(ret);
    }

    public static void run() {
        // TODO GUI code, treat like a main method
        PostTimingGUI run = new PostTimingGUI(finishImage, OCRstream);
        run.addListener();
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        if (finishImage != null) {
            frame.setIconImage(finishImage.getImage());
        }
        frame.add(run);
        frame.setVisible(true);
    }
}

class PTGTester {
    public static void main(String[] args) {
        PostTimingGUI PTG = new PostTimingGUI(null, null);
        PTG.run();
    }
}
