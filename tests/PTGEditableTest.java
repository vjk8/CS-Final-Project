package tests;

import OCR_server.AthleteOCR;
import components.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import components.*;

public class PTGEditableTest
    extends JPanel
{

    private static ArrayList<DraggableLine> finishes;
    private int                             check = 0;
    private JFrame                          frame;
    private CompositeFrame                  finishImage;

    public PTGEditableTest(CompositeFrame compframe)
    {
        super();
        this.finishes = new ArrayList<DraggableLine>();
        // this.finishes.add(new DraggableLine(new TimeFormat(), 5, 25));
        this.frame = new JFrame("Window title");
        // frame.setLayout(null);
        frame.add(this);
        repaint();
        this.finishImage = compframe;

    }


    public void addLine(int eloc)
    {
        finishes.add(new DraggableLine(new TimeFormat(), -1, eloc, finishImage));
        repaint();
    }


    public void removeLine(int eloc)
    {
        for (int i = 0; i < finishes.size(); i++)
        {
            if (finishes.get(i).getXPos() == eloc)
            {
                PTGEditableTest.this.remove(finishes.get(i).editableHipNumber);
                finishes.remove(finishes.get(i));

                repaint();
            }
        }
    }


    public void moveLine(int eloc)
    {
        for (int i = 0; i < finishes.size(); i++)
        {
            if (Math.abs(
                finishes.get(i).getXPos()
                    - check) <= 5 /* Threshold for click error */)
            {
                finishes.get(i).changeXPos(eloc);
                repaint();
            }
        }
        repaint();
        addnremove();
    }

    public void addnremove() {
        DraggableLine d = new DraggableLine(new TimeFormat(), 5, -1);
        this.finishes.add(d);
        repaint();
        this.finishes.remove(d);
        repaint();
    }


    public void addListener()
    {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && e.getY() >= 50)
                {
                    addLine(e.getX());
                }
                else if (SwingUtilities.isRightMouseButton(e))
                {
                    removeLine(e.getX());
                }
                repaint();
            }


            @Override
            public void mousePressed(java.awt.event.MouseEvent e)
            {
                check = e.getX();
                repaint();
            }


            @Override
            public void mouseReleased(java.awt.event.MouseEvent e)
            {
                moveLine(e.getX());
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
    }


    /**
     * for each draggable line in the array finishes, draws a line as well as
     * the finish time and the hip number.
     *
     * @param g
     *            tool used to draw in GUI
     */
    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.RED);
        for (int i = 0; i < finishes.size(); i++)
        {
            g.drawLine(finishes.get(i).getXPos(), 0, finishes.get(i).getXPos(), this.getHeight());
            // g.drawString("" + finishes.get(i).getHipNumber(),
            // finishes.get(i).getXPos() + 6, 30);

            DraggableLine d = finishes.get(i);

            JTextField textField = d.editableHipNumber;

            textField.setBounds(d.getXPos() + 6, 20, 20, 20);
            textField.setVisible(true);

            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    textField.setText(textField.getText());
                    d.setHipNumber(textField.getText());
                    textField.setText(((Integer)d.getHipNumber()).toString());
                }
            });

            add(textField);

            g.drawString(
                "" + finishes.get(i).getTimestamp(),
                finishes.get(i).getXPos() + 6,
                (int)(Math.random() * 380) + 50);

            frame.setSize(frame.getPreferredSize());
        }
        System.out.println("repainted");
    }


    public void run()
    {
        addListener();
        frame = new JFrame();
        frame.setSize(1000, 500);
        add(new JLabel(new ImageIcon(finishImage.getImage())));
        repaint();

        frame.add(this);
        frame.setVisible(true);
        repaint();
    }
}




class Tester
{
    public static void main(String[] args)
    {
        // PTGEditableTest ptg = new PTGEditableTest();
        // ptg.run();
        System.out.println("deprecated main method of PTGEditable");
    }
}
