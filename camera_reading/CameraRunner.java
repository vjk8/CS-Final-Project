import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.*;
import java.awt.Graphics;
import javax.swing.*;

public class CameraRunner
{

    public static BufferedImage matToBufferedImage(Mat m)
    {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1)
        {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        System.out.println(image.toString());
        return image;
    }

    public static void testImshow(Mat m) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(matToBufferedImage(m))));
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Imgcodecs imageCodecs = new Imgcodecs();
        System.out.println("Starting Image Read");
        Mat mat = imageCodecs.imread("FILEPATH");
        System.out.println("Image read");
        //System.out.println(mat.dump());
        testImshow(mat);
    }

}


