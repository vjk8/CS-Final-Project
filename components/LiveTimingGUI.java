package components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * LiveTimingGUI is the Main component of FinishCam. It handles the starting,
 * pausing, resuming, and stopping of the capture and displays the finish image
 * live. After timing is stopped, it invokes PostTimingGUI
 */
public class LiveTimingGUI extends JPanel {

    private volatile JButton startB;
    private volatile JButton stop;
    private volatile JButton pause;
    private volatile JButton resume;
    private volatile JFrame frame;
    private volatile JLabel label;
    private ThreadedCameraRunner camera;
    private volatile boolean terminated;
    private volatile JLabel timeLabel;

    /**
     * Constructs a new LiveTimingGUI
     */
    public LiveTimingGUI() {
        startB = new JButton("Start");
        stop = new JButton("Exit");
        pause = new JButton("Camera Off");
        resume = new JButton("Camera On");
        frame = new JFrame("Live Timing");
        label = new JLabel();
        frame.setSize(500, 200);
        camera = new ThreadedCameraRunner(20);
        terminated = false;
        timeLabel = new JLabel((new TimeFormat()).toString());
    }

    /**
     * called once to execute, this method handles the starting, running, and button listening functions of the
     * LiveTimingGUI
     */
    public void runTiming() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.execute();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("STOP");
                terminated = true;
                startPTG();
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("PAUSE");
            }
        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("RESUME");
            }
        });

        Thread refreshThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    Mat compositeMat = camera.getCompositeFrame().getMat();
                    if (compositeMat == null) continue;
                    compositeMat =
                        compositeMat.submat(new Rect(0, 0, Math.min(1000, compositeMat.cols()), compositeMat.rows()));
                    BufferedImage liveImage = null;
                    try {
                        liveImage = Mat2BufferedImage(compositeMat);
                    } catch (IOException ioex) {
                        System.out.println(ioex.getStackTrace());
                        continue;
                    }

                    if (liveImage == null) continue;

                    label.setIcon(new ImageIcon(liveImage));
                    label.setLocation(0, 0);
                    add(label);
                    frame.add(LiveTimingGUI.this);
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        };

        Thread timerThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    if (camera.getSystemStartTime() == 0) {
                        timeLabel.setText((new TimeFormat()).toString());
                        continue;
                    }
                    timeLabel.setText(
                        (new TimeFormat((int)(System.currentTimeMillis() - camera.getSystemStartTime()))).toString());
                    // add(timeLabel);
                }
            }
        };

        add(startB);
        add(resume);
        add(pause);
        add(stop);
        
        add(timeLabel);
        frame.add(this);
        frame.setVisible(true);
        timerThread.start();
        refreshThread.start();
    }

    private void startPTG() {
        PostTimingGUI PTG = new PostTimingGUI(camera.getCompositeFrame(), camera.getOCRStream());
        PTG.run();
    }

    /**
     * A cleaner version of Mat to BufferedImage for displaying the finish images live.
     * @param mat the Mat to be displayed from the CompositeImage
     * @return a BufferedImage version of the Mat suitable for display in the
     * @throws IOException in the case that something goes wrong with the conversion
     */
    private BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage bufImage = ImageIO.read(in);
            return bufImage;
        } catch (CvException cvex) {
            System.out.println(cvex.getStackTrace().toString());
            return null;
        }
    }

    /**
     * Main method for the LiveTimingGUI which runs the entirety of FinishCam.
     * @param args default arguments
     */
    public static void main(String[] args) {
        LiveTimingGUI LTG = new LiveTimingGUI();
        LTG.runTiming();
    }
}
