
package components;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JFrame;
import java.awt.event.MouseListener;
import org.opencv.core.*;

public class PostTimingGUI extends JFrame {
    private ArrayList<DraggableLine> finishes;
    private static CompositeFrame finishImage;
    private OutputProcessor processor;
    private int xPos = 10;

    public PostTimingGUI(CompositeFrame image) {
        // TODO complete constructor
        finishes = new ArrayList<DraggableLine>();
        finishes.add(new DraggableLine(new TimeFormat(), "a", 100));
        addMouseListener (new MouseListener() {


            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                xPos = e.getX();
            }
 
 
            @Override
            public void mousePressed(java.awt.event.MouseEvent e)
            {
            }
 
 
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e)
            {
                
                for (DraggableLine d: finishes)
                {
                    if (d.getXPos() == xPos)
                    {
                        finishes.get(finishes.indexOf(d)).changeXPos(e.getX());
                    }
                }
                xPos = e.getX();
                e.translatePoint(xPos, 0);
                repaint();
                
            }
 
 
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e)
            {
              
            }
 
 
            @Override
            public void mouseExited(java.awt.event.MouseEvent e)
            {
               
            }
        });
        setTitle("hi");
        setSize(200, 200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        finishImage = image;
        finishes = new ArrayList<DraggableLine>();
        processor = new OutputProcessor(finishes);
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.RED);
        for (DraggableLine d: finishes)
        {
            g.drawLine(d.getXPos(), 0, d.getXPos(), this.getHeight());
        }

    }

    public void removeLine(int x, int y)
    {

    }



    private ArrayList<DraggableLine> getOCR() {
        // TODO don't worry about this one right now
        // each draggable line gets the ocr, every time the draggable line is moved
        return null;
    }

    public static void run() {
        // TODO GUI code, treat like a main method
        PostTimingGUI run = new PostTimingGUI(finishImage);
    }

    public static void main(String[] args)
    {
        run();
    }
    
}
