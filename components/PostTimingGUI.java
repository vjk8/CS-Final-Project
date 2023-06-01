
package components;

import OCR_server.AthleteOCR;
import java.awt.Color;
import java.awt.FlowLayout;
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
import java.util.HashSet;
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
import org.opencv.imgproc.Imgproc;

/**
 * The PostTImingGUI class is called by LiveTiminGUI and adds draggable lines
 * corresponding to each athlete. Also creates buttons that call the ocr and
 * methods from the Outputprocessor.
 */
public class PostTimingGUI extends JPanel {
    private static ArrayList<DraggableLine> finishes;
    private static CompositeFrame finishImage;
    private static ArrayList<SingleFrame> OCRstream;
    private int check = 0;
    private JFrame frame;
    private static AthleteOCR aOcr;
    private JButton ocr;
    private JButton exportCSV;
    private JButton exportText;
    private JButton printResults;
    private BufferedImage displayImage;
    private HashMap<Integer, Athlete> outputProcessorHashMap;

    /**
     * sets the background image, as well as initializes the arrays
     *
     * @param image
     *            the background image of the finish
     * @param ocr
     *            an arraylist of frames
     */
    public PostTimingGUI(CompositeFrame image, ArrayList<SingleFrame> ocr) {
        super();
        this.outputProcessorHashMap = new HashMap<Integer, Athlete>();
        this.OCRstream = ocr;
        this.aOcr = new AthleteOCR();
        this.finishes = new ArrayList<DraggableLine>();
        this.finishImage = image;
        this.ocr = new JButton("Run OCR");
        this.exportCSV = new JButton("Export .CSV");
        this.exportText = new JButton("Export .TXT");
        this.printResults = new JButton("Print to Terminal");
        this.frame = new JFrame("Post Timing");
        // this.frame.setLayout(null);
        frame.add(this);
        addAlphaStrip();
        repaint();
    }

    private void addAlphaStrip() {
        Mat m1 = finishImage.getMat();
        Mat m = new Mat(m1.size(), CvType.CV_8UC4);
        Imgproc.cvtColor(m1, m, Imgproc.COLOR_BGR2BGRA);
        Mat alphastrip = new Mat(50, m.cols(), CvType.CV_8UC4, new Scalar(0, 0, 0, 0));

        List<Mat> toBeCombined = Arrays.asList(alphastrip, m, alphastrip);
        Core.vconcat(toBeCombined, m);
        try {
            displayImage = Mat2BufferedImage(m);
        } catch (IOException ioe) {
            System.out.println(ioe.getStackTrace());
        }
    }

    private int validPos(int observedPos) {
        while (finishImage.getTimeAtPixel(observedPos) == null) {
            observedPos--;
        }
        return observedPos;
    }

    private void addLine(MouseEvent e) {
        if (e.getX() == validPos(e.getX())) {
            finishes.add(new DraggableLine(new TimeFormat(), -1, e.getX(), finishImage));
        }
        repaint();
    }

    private void removeLine(MouseEvent e) {
        for (int i = 0; i < finishes.size(); i++) {
            if (finishes.get(i).getXPos() == e.getX()) {
                PostTimingGUI.this.remove(finishes.get(i).editableHipNumber);
                finishes.remove(finishes.get(i));
                repaint();
            }
        }
    }

    private void moveLine(MouseEvent e) {
        for (int i = 0; i < finishes.size(); i++) {
            if (Math.abs(finishes.get(i).getXPos() - check) <= 5 /* Threshold for click error */) {
                finishes.get(i).changeXPos(validPos(e.getX()));
            }
        }
        repaint();
    }

    private void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    addLine(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    removeLine(e);
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
                moveLine(e);
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

            g.drawString("" + finishes.get(i).getTimestamp(), finishes.get(i).getXPos() + 6, getHeight() - 40);
        }
        frame.setSize(frame.getPreferredSize());
    }

    /**
     * For each draggable line, every time it is moved, it gets the ocr. Then it
     * finds which frame has the x position in the finished image. Finally calls
     * the athlete ocr on it and returns the number.
     *
     * @param xPos
     *            the x position of the frame
     * @return the hip number of the athlete
     */
    private static int getOCR(int xPos) {
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
            if (Math.abs(f.getTime().intValue() - finishes.get(i).getTimestamp().intValue()) <= 100) {
                ret = f.getMat();
                if (ret != null) {
                    testimshow(ret);
                    break;
                }
            }
        }

        try {
            if (ret != null) {
                int OCR_ret = aOcr.getAthleteNumber(ret);
                System.out.println("OCR found hip number of " + OCR_ret);
                return OCR_ret;
            }

            else
                System.out.println("Could not find frame to use for OCR");
        } catch (IOException ioex) {
            System.out.println(ioex.getStackTrace());
        }

        return -1;
    }

    private boolean promptInput(int hipNumber, OutputProcessor o) {
        Scanner scan = new Scanner(System.in);
        System.out.print(
            "Enter space-separated FirstName, LastName, Team, Grade, Seed Time, and PR (in that order) for athlete with hip number " +
            hipNumber + ": ");
        try {
            String nm = scan.next();
            nm = nm + " " + scan.next();
            String tm = scan.next();
            int gr = scan.nextInt();
            String sd = scan.next();
            String pr = scan.next();
            o.addAthlete(hipNumber, new Athlete(nm, tm, gr, new TimeFormat(sd), new TimeFormat(pr)));
            outputProcessorHashMap.put(hipNumber, new Athlete(nm, tm, gr, new TimeFormat(sd), new TimeFormat(pr)));
        } catch (InputMismatchException e) {
            return false;
        }

        return true;
    }

    // to prevent code duplication
    private OutputProcessor preppedProcessor() {
        return preppedProcessor(outputProcessorHashMap);
    }

    // to prevent code duplication
    private OutputProcessor preppedProcessor(HashMap<Integer, Athlete> initHashMap) {

        OutputProcessor op;
        Collections.sort(finishes);
        op = new OutputProcessor(finishes, initHashMap);

        ArrayList<Integer> hns = new ArrayList<Integer>();
        for (int i = 0; i < finishes.size(); i++) hns.add(i, finishes.get(i).getHipNumber());

        if ((new HashSet<Integer>(hns)).size() != hns.size()) {
            System.out.println("Duplicate hip numbers found, fix this.");
            return null;
        }

        for (DraggableLine d : finishes) {
            if (!op.getHashMap().containsKey(d.getHipNumber())) {
                boolean isValid = promptInput(d.getHipNumber(), op);
                while (!isValid) {
                    System.out.println("Invalid Input, try again.");
                    isValid = promptInput(d.getHipNumber(), op);
                }
            }
        }

        return op;
    }

    /**
     * Creates 4 buttons, one that calls the ocr, the others call methods from
     * the outputprocessor class. Then creates a JFrame and adds all of the
     * buttons as well as a mouseListener to it. Also adds the finish image and
     * calls the paint method to draw the draggable lines and editable hip
     * number fields.
     */
    public void run() {

        ocr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (OCRstream != null) {
                    for (DraggableLine d : finishes) {
                        PostTimingGUI.this.getOCR(d.getXPos());
                    }
                }
            }
        });

        exportCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = preppedProcessor();
                if (op != null) {
                    try {
                        op.exportCSV(".\\finishes.csv");
                    } catch (IOException a) {
                        System.out.println(a.getStackTrace());
                    }
                }
            }
        });

        exportText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = preppedProcessor();
                if (op != null) {
                    try {
                        op.exportText(".\\finishes.txt");
                    } catch (IOException a) {
                        System.out.println(a.getStackTrace());
                    }
                }
            }
        });

        printResults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutputProcessor op = preppedProcessor();
                if (op != null) op.printResults();
            }
        });

        addListener();
        frame = new JFrame("Post Timing");
        frame.setSize(1000, 500);

        if (finishImage != null) {
            add(new JLabel(new ImageIcon(displayImage)));
            add(ocr);
            add(exportCSV);
            add(exportText);
            add(printResults);
        }
        frame.add(this);
        frame.setVisible(true);
        repaint();
    }

    private static void testimshow(Mat m) {
        JFrame f2 = new JFrame();
        f2.getContentPane().setLayout(new FlowLayout());
        try {
            f2.getContentPane().add(new JLabel(new ImageIcon(Mat2BufferedImage(m))));
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        f2.pack();
        f2.setVisible(true);
    }

    private static BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".png", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage bufImage = ImageIO.read(in);
            return bufImage;
        } catch (CvException cvex) {
            System.out.println(cvex.getStackTrace().toString());
            return null;
        }
    }
}
