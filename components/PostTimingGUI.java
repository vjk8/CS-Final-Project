
package components;

import OCR_server.AthleteOCR;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.opencv.core.*;

public class PostTimingGUI extends JPanel {
    private ArrayList<DraggableLine> finishes;
    private static CompositeFrame finishImage;
    private static ArrayList<SingleFrame> OCRstream;
    private int check = 0;
    private JFrame frame;
    private AthleteOCR aOcr;
    private JButton ocr;
    private JButton exportCSV;
    private JButton exportHtml;
    private JButton exportText;
    private JButton printResults;
    // ocr button, export csv, export html, export text, print results

    public PostTimingGUI(CompositeFrame image, ArrayList<SingleFrame> ocr) {
        // TODO complete constructor

        OCRstream = ocr;
        aOcr = new AthleteOCR();
        finishes = new ArrayList<DraggableLine>();
        finishImage = image;
        finishes.add(new DraggableLine(new TimeFormat(), 5, 25, finishImage));
    }

    public void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    OutputProcessor op = new OutputProcessor(finishes);
                    for (DraggableLine d: finishes)
                    {
                        op.addAthlete(d.getHipNumber());
                    }
                    finishes.add(new DraggableLine(new TimeFormat(), -1, e.getX(), finishImage));
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

    public void paint(Graphics g) {
        super.paint(g);
        /*if (finishImage != null)
        {
            add(new JLabel(new ImageIcon(finishImage.getImage())));
            frame.pack();
        }*/
        frame.pack();

        g.setColor(Color.RED);
        for (int i = 0; i < finishes.size(); i++) {
            g.drawLine(finishes.get(i).getXPos(), 0, finishes.get(i).getXPos(), this.getHeight());
            g.drawString("" + finishes.get(i).getHipNumber(), finishes.get(i).getXPos() + 6, 30);
            g.drawString("" + finishes.get(i).getTimestamp(), finishes.get(i).getXPos() + 6,
                         (int)(Math.random() * 400) + 40);
        }
    }

    private int getOCR(int xPos) {
        // each draggable line gets the ocr, every time the draggable line is
        // moved
        // find which frame has the x position, find where it is in the finished
        // image
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

        return aOcr.getAthleteNumber(ret);
    }

    public void run() {
        // TODO GUI code, treat like a main method

        // ocr button, export csv, export html, export text, print results

        ocr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (DraggableLine d: finishes)
                {
                    PostTimingGUI.this.getOCR(d.getXPos());
                }
                
            }
        });

        exportCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = new OutputProcessor(finishes);
                for (DraggableLine d: finishes)
                    {
                        op.addAthlete(d.getHipNumber());
                    }
                try {
                    op.exportCSV(".\\finishes.csv");
                }
                catch(IOException a){
                    System.out.println(a.getStackTrace());
                }
                
            }
        });

        exportHtml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = new OutputProcessor(finishes);
                for (DraggableLine d: finishes)
                    {
                        op.addAthlete(d.getHipNumber());
                    }
                try {
                    op.exportHTML(".\\finishes.csv");
                }
                catch(IOException a){
                    System.out.println(a.getStackTrace());
                }
            }
        });

        exportText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = new OutputProcessor(finishes);
                for (DraggableLine d: finishes)
                    {
                        op.addAthlete(d.getHipNumber());
                    }
                try {
                    op.exportText(".\\finishes.csv");
                }
                catch(IOException a){
                    System.out.println(a.getStackTrace());
                }
            }
        });

        printResults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = new OutputProcessor(finishes);
                for (DraggableLine d: finishes)
                    {
                        op.addAthlete(d.getHipNumber());
                    }
                op.printResults();
            }
        });
        addListener();
        frame = new JFrame();
        frame.setSize(1000, 500);
        if (finishImage != null) {
            add(new JLabel(new ImageIcon(finishImage.getImage())));
        }
        frame.add(this);
        frame.setVisible(true);
        repaint();
    }
}

class PTGTester {
    public static void main(String[] args) {
        PostTimingGUI PTG = new PostTimingGUI(null, null);
        PTG.run();
    }
}
