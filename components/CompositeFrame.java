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
     * Gives the TimeFormat elapsed time at a certain x position in the image, used for querying times in PostTimingGUI
     * @param pixelIndex the index (x position) of the pixel within the image
     * @return the TimeFormat elapsed time at pixelIndex
     */
    public TimeFormat getTimeAtPixel(int pixelIndex) {
        if (pixelIndex < timestamps.size() && pixelIndex >= 0)
            return timestamps.get((timestamps.size() - 1 - pixelIndex));
        return null;
    }

    /**
     * Getter for the OpenCV Mat
     * @return the finish image as a Mat
     */
    public Mat getMat() {
        return composite;
    }
}
