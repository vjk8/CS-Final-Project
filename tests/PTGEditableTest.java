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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import components.*;

public class PTGEditableTest extends JPanel {

    private static ArrayList<DraggableLine> finishes;
    private int                             check = 0;
    private JFrame                          frame;
    private CompositeFrame                  finishImage;
    private BufferedImage                   displayImage;

    public PTGEditableTest(CompositeFrame compframe) {
        super();
        this.finishes = new ArrayList<DraggableLine>();
        // this.finishes.add(new DraggableLine(new TimeFormat(), 5, 25));
        this.frame = new JFrame("Window title");
        // frame.setLayout(null);
        frame.add(this);
        repaint();
        this.finishImage = compframe;
        addAlphaStrip();

    }


    public void addAlphaStrip()
    {
        Mat m1 = finishImage.getMat();
        Mat m = new Mat(m1.size(), CvType.CV_8UC4);

        System.out.println(m1.type());
        
        Imgproc.cvtColor(m1, m, Imgproc.COLOR_BGR2BGRA);
        Mat alphastrip = new Mat(50, m.cols(), CvType.CV_8UC4, new Scalar(0, 0, 0, 0));
        List<Mat> toBeCombined = Arrays.asList(alphastrip, m);
        System.out.println(CvType.CV_8UC4);
        System.out.println(alphastrip.type());
        System.out.println(CvType.CV_8UC3);
        System.out.println(m.type());
        Core.vconcat(toBeCombined, m);
        try
        {
            displayImage = Mat2BufferedImage(m);
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getStackTrace());
        }
    }


    public void addLine(int eloc)
    {
        finishes.add(new DraggableLine(new TimeFormat(), -1, eloc, finishImage));
        repaint();
    }

    public void removeLine(int eloc) {
        for (int i = 0; i < finishes.size(); i++) {
            if (finishes.get(i).getXPos() == eloc) {
                PTGEditableTest.this.remove(finishes.get(i).editableHipNumber);
                finishes.remove(finishes.get(i));

                repaint();
            }
        }
    }

    public void moveLine(int eloc) {
        for (int i = 0; i < finishes.size(); i++) {
            if (Math.abs(finishes.get(i).getXPos() - check) <= 5 /* Threshold for click error */) {
                finishes.get(i).changeXPos(eloc);
                repaint();
            }
        }
        repaint();
    }


    public void addnremove()
    {
        addLine(-10);
        removeLine(-10);
        System.out.println("addnremove()");
    }

    public void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getY() >= 50) {
                    addLine(e.getX());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    removeLine(e.getX());
                }
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                check = e.getX();
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                moveLine(e.getX());
                addnremove();
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

        g.setColor(Color.RED);
        for (int i = 0; i < finishes.size(); i++) {
            g.drawLine(finishes.get(i).getXPos(), 0, finishes.get(i).getXPos(), this.getHeight());
            // g.drawString("" + finishes.get(i).getHipNumber(),
            // finishes.get(i).getXPos() + 6, 30);

            DraggableLine d = finishes.get(i);

            JTextField textField = d.editableHipNumber;

            textField.setBounds(d.getXPos() + 6, 20, 20, 20);
            textField.setVisible(true);

            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event)
                {
                    textField.setText(textField.getText());
                    d.setHipNumber(textField.getText());
                    textField.setText(((Integer)d.getHipNumber()).toString());
                }
            });

            add(textField);

            g.drawString("" + finishes.get(i).getTimestamp(), finishes.get(i).getXPos() + 6,
                         (int)(Math.random() * 380) + 50);

            // frame.setSize(frame.getPreferredSize());
        }
        System.out.println("repainted");
    }

    public void run() {
        addListener();
        frame = new JFrame();
        frame.setSize(1000, 500);
        add(new JLabel(new ImageIcon(displayImage)));
        repaint();

        frame.add(this);
        frame.setVisible(true);
        repaint();
    }


    /**
     * A cleaner version of Mat to BufferedImage for displaying the finish
     * images live.
     * 
     * @param mat
     *            the Mat to be displayed from the CompositeImage
     * @return a BufferedImage version of the Mat suitable for display in the
     * @throws IOException
     *             in the case that something goes wrong with the conversion
     */
    private BufferedImage Mat2BufferedImage(Mat mat)
        throws IOException
    {
        try
        {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".png", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage bufImage = ImageIO.read(in);
            return bufImage;
        }
        catch (CvException cvex)
        {
            System.out.println(cvex.getStackTrace().toString());
            return null;
        }
    }

}

class Tester {
    public static void main(String[] args) {
        // PTGEditableTest ptg = new PTGEditableTest();
        // ptg.run();
        System.out.println("deprecated main method of PTGEditable");
    }
}
