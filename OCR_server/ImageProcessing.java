package OCR_server;

import org.opencv.core.*;
import org.opencv.imgproc.*;

public class ImageProcessing {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Convert an image to grayscale
     *
     * @param img the image
     * @return the grayscale image
     */
    public static Mat toGrayscale(Mat img) {
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    /**
     * Resize an image
     *
     * @param img the image
     * @param width the new width
     * @param height the new height
     * @return the resized image
     */
    public static Mat resize(Mat img, int width, int height) {
        Mat resized = new Mat();
        Imgproc.resize(img, resized, new Size(width, height));
        return resized;
    }

    /**
     * Invert an image
     *
     * @param img the image
     * @return the inverted image
     */
    public static Mat invert(Mat img) {
        Mat inverted = new Mat();
        Core.bitwise_not(img, inverted);
        return inverted;
    }
}
