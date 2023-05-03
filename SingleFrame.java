import java.awt.image.BufferedImage;
import org.opencv.core.*;

public class SingleFrame {
    private Mat frame;
    private double time;

    public SingleFrame(Mat m, double t) {
        frame = m;
        t = time;
    }

    public Mat getMat() {
        // TODO complete getter
        return null;
    }

    public double getTime() {
        // TODO complete getter
        return 0.0;
    }

    public BufferedImage getImage() {
        // TODO complete converter
        return null;
    }


}
