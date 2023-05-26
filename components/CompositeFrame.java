package components;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * CompositeFrame represents the final image with timestamped image slivers.
 */
public class CompositeFrame {
    private volatile Mat composite;
    private volatile ArrayList<TimeFormat> timestamps;

    /**
     * Constructs an empty CompositeFrame
     */
    public CompositeFrame() {
        composite = null;
        timestamps = new ArrayList<TimeFormat>();
    }

    /**
     * appends the right edge of a SingleFrame to the composite image and stores the time. This enables the creation of
     * the finish image
     * @param s the next SingleFrame to be appended to the composite image
     */
    public void processFrame(SingleFrame s) {
        if (s == null) return;
        Rect cropRect = new Rect(0, 0, 1, s.getMat().rows());
        Mat slice = s.getMat().submat(cropRect);
        for (int hReps = 0; hReps < 5; hReps++) {
            timestamps.add(s.getTime());

            if (composite == null) {
                composite = slice;
            } else {
                List<Mat> toBeCombined = Arrays.asList(slice, composite);
                Core.hconcat(toBeCombined, composite);
            }
        }
    }

    /**
     * Gives the TimeFormat elapsed time at a certain x position in the image
     * @param pixelIndex the index (x position) of the pixel within the image
     * @return the TimeFormat elapsed time at pixelIndex
     */
    public TimeFormat getTimeAtPixel(int pixelIndex) {
        if (pixelIndex < timestamps.size()) return timestamps.get(pixelIndex);
        return null;
    }

    /**
     * Getter for the OpenCV Mat
     * @return the finish image as a Mat
     */
    public Mat getMat() {
        return composite;
    }

    /**
     * Getter for a BufferedImage version of the finish image. Deprecated and unwieldy conversion, recommended not to
     * use. Better conversion occurs in LiveTimingGUI
     * @return a BufferedImage version of the finish image Mat
     */
    public BufferedImage getImage() {
        return matToBufferedImage(composite);
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        if (mat == null) return null;
        Mat m;
        try {
            m = mat.clone();
        } catch (CvException cvEx) {
            return null;
        }

        int type = BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels

        if (m.cols() == 0 || m.rows() == 0) return null;
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    // for testing only

    /**
     * For testing purposes only
     * @return the arraylist containing all of the timestamps for the image
     */
    public ArrayList<TimeFormat> getTimestampList() {
        return (ArrayList<TimeFormat>)timestamps.clone();
    }
}
