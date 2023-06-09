package tests;

import components.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class LiveDisplayTest extends JPanel {

    private ThreadedCameraRunner tcr;
    private JFrame frame;
    private JLabel picLabel;

    public LiveDisplayTest() {
        frame = new JFrame();
        frame.setSize(1000, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        picLabel = new JLabel();
    }

    public void print(String s) {
        System.out.println(s);
    }

    public void setUpTCR() {
        tcr = new ThreadedCameraRunner(20);
    }

    public void runLive() throws IOException {

        tcr.execute();
        while (true) {
            Mat compositeMat = tcr.getCompositeFrame().getMat();
            if (compositeMat == null) continue;
            print(compositeMat.size().toString());
            compositeMat =
                compositeMat.submat(new Rect(0, 0, Math.min(1000, compositeMat.cols()), compositeMat.rows()));
            BufferedImage liveImage = Mat2BufferedImage(compositeMat);
            if (liveImage == null) continue;

            picLabel.setIcon(new ImageIcon(liveImage));
            picLabel.setLocation(0, 0);
            add(picLabel);
            frame.add(this);
            frame.pack();
            frame.setVisible(true);
        }
    }

    public BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage bufImage = ImageIO.read(in);
            return bufImage;
        } catch (CvException cvex) {
            print(cvex.getStackTrace().toString());
            return null;
        }
    }
}

class Runner {
    public static void main(String[] args) {
        LiveDisplayTest ldt = new LiveDisplayTest();
        ldt.setUpTCR();
        try {
            ldt.runLive();
        } catch (IOException ioex) {
            System.out.println(ioex.getStackTrace());
        }
    }
}
