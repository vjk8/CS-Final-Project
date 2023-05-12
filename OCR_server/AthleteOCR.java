package OCR_server;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;

public class AthleteOCR {
    static {
        System.loadLibrary("ocr");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Get the number of the athlete in the image
     *
     * The image will be converted to grayscale, resized to 28x28, and inverted.
     *
     * @param img The image to be processed
     * @return The number of the athlete in the image
     */
    public static int getAthleteNumber(Mat img) {
        img = ImageProcessing.invert(ImageProcessing.toGrayscale(ImageProcessing.resize(img, 28, 28)));
        double[] imgArray = new double[28 * 28];
        for (int i = 0; i < 28 * 28; i++) {
            imgArray[i] = img.get(i / 28, i % 28)[0] / 255.0;
        }
        return getAthleteNumber(imgArray);
    }

    /**
     * Get the number of the athlete in the image
     *
     * Takes an array of doubles representing the image.
     * This array should be of length 28*28, and the values should be between 0 and 1.
     * The array is stored in row-major order.
     *
     * @param img The image to be processed
     * @return The number of the athlete in the image
     */
    private static native int getAthleteNumber(double[] img);

    public static void main(String[] args) {
        System.out.println("Test");
        Mat img = Imgcodecs.imread("./OCR_server/data/test_1.jpg");
        System.out.println(getAthleteNumber(img));
    }
}
