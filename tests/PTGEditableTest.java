package tests;

import OCR_server.AthleteOCR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

public class PTGEditableTest extends JPanel {

    private static ArrayList<DraggableLine> finishes;
    private int check = 0;
    private JFrame frame;

    public PTGEditableTest()
    {
        super();
        this.finishes = new ArrayList<DraggableLine>();
        this.finishes.add(new DraggableLine(new TimeFormat(), 5, 25));
        this.frame = new JFrame("Window title");
        frame.setLayout(null);
        frame.add(this);

    }

    public void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // OutputProcessor op = new OutputProcessor(finishes);
                    // for (DraggableLine d : finishes)
                    // {
                    // op.addAthlete(d.getHipNumber());
                    // }
                    finishes.add(new DraggableLine(new TimeFormat(), -1, e.getX()));
                    // PostTimingGUI.this.removeAll();
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    for (int i = 0; i < finishes.size(); i++) {
                        if (finishes.get(i).getXPos() == e.getX()) {
                            finishes.remove(finishes.get(i));
                            // PostTimingGUI.this.removeAll();
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                check = e.getX();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                for (int i = 0; i < finishes.size(); i++) {
                    if (Math.abs(finishes.get(i).getXPos() - check) <= 5 /* Threshold for click error */) {
                        finishes.get(i).changeXPos(e.getX());
                        // getOCR(finishes.get(i).getXPos()); This line is OK,
                        // just need to disable while testing
                        // e.translatePoint(e.getX(), 0);
                        // PostTimingGUI.this.removeAll();
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

    /**
     * for each draggable line in the array finishes, draws a line as well as
     * the finish time and the hip number.
     *
     * @param g
     *            tool used to draw in GUI
     */
    public void paint(Graphics g) {
        super.paint(g);
        /*
         * if (finishImage != null) { add(new JLabel(new
         * ImageIcon(finishImage.getImage()))); frame.pack(); }
         */

        g.setColor(Color.RED);
        for (int i = 0; i < finishes.size(); i++) {
            g.drawLine(finishes.get(i).getXPos(), 0, finishes.get(i).getXPos(), this.getHeight());
            //g.drawString("" + finishes.get(i).getHipNumber(), finishes.get(i).getXPos() + 6, 30);

            
            //JTextField textField = finishes.get(i).editableHipNumber;

            //System.out.println(textField);

            //this.add(textField);

            g.drawString(
                "" + finishes.get(i).getTimestamp(),
                finishes.get(i).getXPos() + 6,
                (int)(Math.random() * 400) + 40);
        }
    }


    public void run()
    {
        addListener();
        frame = new JFrame();
        frame.setSize(1000, 500);

        frame.add(this);
        frame.setVisible(true);
        repaint();
    }
}




class Tester
{
    public static void main(String[] args)
    {
        PTGEditableTest ptg = new PTGEditableTest();
        ptg.run();
    }
}
