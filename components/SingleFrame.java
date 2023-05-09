package components;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.*;

public class SingleFrame
{
    private Mat        frame;
    private TimeFormat time;

    public SingleFrame(Mat m, int t, int startTime)
    {
        frame = m;
        time = new TimeFormat(t - startTime);
    }


    public Mat getMat()
    {
        return frame;
    }


    public TimeFormat getTime()
    {
        return time;
    }


    public BufferedImage getBufferedImage()
    {
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
