package components;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class CompositeFrame {
    private Mat composite;
    private ArrayList<TimeFormat> timestamps;

    public CompositeFrame() {
        composite = null;
        timestamps = new ArrayList<TimeFormat>();
        Imgcodecs imagecodecs = new Imgcodecs();
    }

    public void processFrame(SingleFrame s) {
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

    public TimeFormat getTimeAtPixel(int pixelIndex) {
        if (pixelIndex < timestamps.size()) return timestamps.get(pixelIndex);
        return null;
    }

    public void addPause() {
        // TODO complete method
    }

    public Mat getMat() {
        return composite;
    }

    public BufferedImage getImage() {
        return matToBufferedImage(composite);
    }

    private static BufferedImage matToBufferedImage(Mat m) {
        if (m == null) return null;
        int type = BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        try {
            m.get(0, 0, b); // get all the pixels
        }
        catch (Exception e) {
            return null;
        }
        
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    // for testing only

    public ArrayList<TimeFormat> getTimestampList() {
        return timestamps;
    }
}
