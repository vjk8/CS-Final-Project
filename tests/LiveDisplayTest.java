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
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class LiveDisplayTest extends JPanel {

    private ThreadedCameraRunner tcr;
    private JFrame frame;
    private JLabel picLabel;

    private BufferedImage b;

    public LiveDisplayTest() {
        frame = new JFrame();
        frame.setSize(500, 500);
    }

    public void print(String s) {
        System.out.println(s);
    }

    public void singleImageTest() {
        try {
            b = ImageIO.read(new File("C:\\Users\\adars\\Downloads\\download.png"));
        } catch (IOException ioex) {
            System.out.println("IO Exception occurred");
            return;
        }

        picLabel = new JLabel(new ImageIcon(b));
        picLabel.setIcon(new ImageIcon(b));
        add(picLabel);
        frame.add(this);
        frame.setVisible(true);
        System.out.println("complete");
    }

    public void setUpTCR() {
        tcr = new ThreadedCameraRunner();
        print("tcr set up");
    }

    public void runLive() throws IOException {
        tcr.execute();
        print("executing");
        while (true) {
            Mat compositeMat = tcr.getCompositeFrame().getMat();
            BufferedImage liveImage = Mat2BufferedImage(compositeMat);
            picLabel.setIcon(new ImageIcon(b));
        }
    }

    public BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
        // Encoding the image
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        // Storing the encoded Mat in a byte array
        byte[] byteArray = matOfByte.toArray();
        // Preparing the Buffered Image
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);
        return bufImage;
    }
}

class Runner {
    public static void main(String[] args) {
        LiveDisplayTest ldt = new LiveDisplayTest();
        ldt.setUpTCR();
        ldt.runLive();
    }
}
