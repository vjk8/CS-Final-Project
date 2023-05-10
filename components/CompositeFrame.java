package components;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import org.opencv.core.*;

public class CompositeFrame {
    private Mat composite;
    private ArrayList<TimeFormat> timestamps;

    public CompositeFrame()
    {
        composite = null;
        timestamps = new ArrayList<TimeFormat>();
    }

    public void processFrame(SingleFrame s)
    {
        timestamps.add(s.getTime());
        // TODO complete slicing and appending code
    }

    public TimeFormat getTimeAtPixel(int pixelIndex)
    {
        if (pixelIndex < timestamps.size()) return timestamps.get(pixelIndex);
        return null;
    }

    public void addPause()
    {
        // TODO complete method
    }

    public Mat getMat()
    {
        return composite;
    }

    public BufferedImage getImage()
    {
        return matToBufferedImage(composite);
    }

    private static BufferedImage matToBufferedImage(Mat m)
    {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels =
            ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
