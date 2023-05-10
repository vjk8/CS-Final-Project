package components;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

public class CameraRunner {
    private static Queue<SingleFrame> toBeProcessed;
    private static CompositeFrame finishImage;

    public CameraRunner() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        toBeProcessed = new LinkedList<SingleFrame>();
        finishImage = new CompositeFrame();
    }

    public static void run() {
        VideoCapture cap = new VideoCapture();
        cap.open(0);
        Mat newFrame = new Mat();
        CameraStarter cs = new CameraStarter(128);
        long startTime = cs.getStartTime();
        while (System.currentTimeMillis() - startTime < 20000) {
            newFrame = new Mat();
            cap.read(newFrame);
            long capTime = System.currentTimeMillis();
            // toBeProcessed.add(new SingleFrame(newFrame, capTime, startTime));
            finishImage.processFrame(new SingleFrame(newFrame, capTime, startTime));
            imshow(finishImage.getMat());
        }
    }

    public void receiveMessage() {
        // TODO complete method
    }

    public CompositeFrame getCompositeFrame() {
        return finishImage;
    }

    public BufferedImage getBufferedImage() {
        return finishImage.getImage();
    }

    public Mat pause() {
        return null;
    }

    // for testing only

    private static void imshow(Mat m) {
        long startTime = System.currentTimeMillis();
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(matToBufferedImage(m))));
        frame.pack();
        frame.setVisible(true);
        if (System.currentTimeMillis() - startTime >= 100) {
            frame.dispose();
        }
    }

    private static BufferedImage matToBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    // for testing only
    /*public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.println("loaded library, fetching webcam");
        VideoCapture cap = new VideoCapture();
        cap.open(0);
        System.out.println("Webcam available");
        Mat newFrame = new Mat();

        if (cap.isOpened())
            System.out.println("cap is opened");
        else
            System.out.println("cap is not opened");

        while (true) {
            boolean isRead = cap.read(newFrame);
            System.out.println("isRead: " + isRead);
            imshow(newFrame);
        }
    }*/
}
