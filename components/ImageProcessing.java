package components;

import java.util.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

/**
 * Provides a set of image processing tools in order to facilitate OCR
 * pre-processing. Used mainy by the AthleteOCR class.
 */
public class ImageProcessing
{
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Convert an image to grayscale
     *
     * @param img
     *            the image
     * @return the grayscale image
     */
    public static Mat toGrayscale(Mat img)
    {
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }


    /**
     * Resize an image
     *
     * @param img
     *            the image
     * @param width
     *            the new width
     * @param height
     *            the new height
     * @return the resized image
     */
    public static Mat resize(Mat img, int width, int height)
    {
        Mat resized = new Mat();
        Imgproc.resize(img, resized, new Size(width, height));
        return resized;
    }


    /**
     * Invert an image
     *
     * @param img
     *            the image
     * @return the inverted image
     */
    public static Mat invert(Mat img)
    {
        Mat inverted = new Mat();
        Core.bitwise_not(img, inverted);
        return inverted;
    }


    /**
     * Copied from AthleteOCR for debugging purposes Save the image to a
     * temporary file
     *
     * @param img
     *            the image to save
     * @return the filename of the saved image
     */
    private static String saveImage(Mat img)
    {
        String filename = "/tmp/" + ((int)(Math.random() * 596969) + 100000) + ".jpg";
        Imgcodecs.imwrite(filename, img);
        return filename;
    }


    /**
     * Rotates an image by a certain angle, with positive taken to be
     * counterclockwise
     * 
     * @param img
     *            the image
     * @param angle
     *            the angle
     * @return a rotated image
     */
    public static Mat rotate(Mat img, double angle)
    {
        System.out.println("Before rotating " + angle + " degrees: " + saveImage(img));
        Point center = new Point(img.cols() / 2, img.rows() / 2);
        Mat rotationMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        Mat result = img.clone();
        Imgproc.warpAffine(img, result, rotationMat, img.size());
        System.out.println("After rotating: " + saveImage(result));
        return result;
    }


    public static Mat deskew(Mat img)
    {
        int[][] corners = new int[4][2]; // Left, up, right, down
        corners[0][1] = 696969;
        corners[1][0] = 696969;
        for (int i = 0; i < img.rows(); i++)
        {
            for (int j = 0; j < img.cols(); j++)
            {
                if (img.get(i, j)[0] >= 208 && img.get(i, j)[1] >= 208 && img.get(i, j)[2] >= 208)
                {
                    if (j < corners[0][1])
                    {
                        corners[0][0] = i;
                        corners[0][1] = j;
                    }
                    if (i < corners[1][0])
                    {
                        corners[1][0] = i;
                        corners[1][1] = j;
                    }
                    if (j > corners[2][1])
                    {
                        corners[2][0] = i;
                        corners[2][1] = j;
                    }
                    if (i > corners[3][0])
                    {
                        corners[3][0] = i;
                        corners[3][1] = j;
                    }
                }
            }
        }
        System.out.println(corners[0][0] + " " + corners[0][1]);
        System.out.println(corners[1][0] + " " + corners[1][1]);
        System.out.println(corners[2][0] + " " + corners[2][1]);
        System.out.println(corners[3][0] + " " + corners[3][1]);
        try
        {
            if (corners[0][0] * 2 >= img.rows())
            {
                // Probably skewed clockwise
                return rotate(
                    img,
                    -Math.atan(
                        ((double)corners[2][1] - corners[1][1]) / (corners[2][0] - corners[1][0]))
                        / Math.PI * 180);
            }
            // Probably skewed counterclockwise
            return rotate(
                img,
                -Math
                    .atan(((double)corners[1][1] - corners[0][1]) / (corners[1][0] - corners[0][0]))
                    / Math.PI * 180);
        }
        catch (ArithmeticException e)
        {
            return img;
        }
    }
}
