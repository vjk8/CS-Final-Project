package components;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.*;

/**
 * SingleFrame holds a frame of the camera capture with a timestamp 
 */
public class SingleFrame {
    private Mat frame;
    private TimeFormat time;

    /**
     * constructs a new SingleFrame storing Mat and elapsed time data
     * @param m the Mat representing the camera-read image
     * @param t the system time at which the frame was captured
     * @param startTime the start time of the capture, used to calculate elapsed time
     */
    public SingleFrame(Mat m, long t, long startTime) {
        frame = m;
        time = new TimeFormat((int)(t - startTime));
    }

    /**
     * getter for the mat
     * @return the OpenCV mat representing the image
     */
    public Mat getMat() {
        return frame;
    }

    /**
     * getter for the elapsed time
     * @return the elapsed time for the SingleFrame
     */
    public TimeFormat getTime() {
        return time;
    }

    /**
     * DEPRECATED AND ONLY FOR TESTING, conversts the mat into a buffered image
     * @return a BufferedImage representing the frame.
     */
    public BufferedImage getBufferedImage() {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = frame.channels() * frame.cols() * frame.rows();
        byte[] b = new byte[bufferSize];
        frame.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(frame.cols(), frame.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        System.out.println(image.toString());
        return image;
    }
}
