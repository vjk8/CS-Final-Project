package OCR_server;

import org.opencv.core.*;

public class AthleteOCR {
    static {
        System.loadLibrary("ocr");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static native int getAthleteNumber(Mat img);

    public static void main(String[] args) {
        System.out.println("Test");
    }
}
