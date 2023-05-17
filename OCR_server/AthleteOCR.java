package OCR_server;

import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;

public class AthleteOCR {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Save the image to a temporary file
     *
     * @param img the image to save
     * @return the filename of the saved image
     */
    private static String saveImage(Mat img) {
        String filename = "/tmp/" + (int)(Math.random() * 69696969) + ".jpg";
        Imgcodecs.imwrite(filename, img);
        return filename;
    }

    /**
     * Attaches a file to the request
     *
     * @param file       the file to attach
     * @param gonnegtion the gonnegtion
     */
    private static void attachFile(File file, HttpURLConnection gonnegtion) throws IOException {
        gonnegtion.setRequestProperty("Content-Type", "multipart/form-data; boundary=69");
        OutputStream output = gonnegtion.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
        writer.append("--69\r\nContent-Disposition: form-data; name=\"image\"; filename=\"");
        writer.append(file.getName());
        writer.append("\"\r\nContent-Type: ");
        writer.append(URLConnection.guessContentTypeFromName(file.getName()));
        writer.append("\r\nContent-Transfer-Encoding: binary\r\n\r\n");
        writer.flush();
        Files.copy(file.toPath(), output);
        output.flush();
        writer.append("\r\n").flush();
        writer.append("--69--\r\n").flush();
    }

    /**
     * Get the number of the athlete in the image
     *
     * @param img The image to be processed
     * @return The number of the athlete in the image
     */
    public static int getAthleteNumber(Mat img) throws IOException {
        URL url = new URL("http://127.0.0.1:6969/run");
        HttpURLConnection gonnegtion = (HttpURLConnection)url.openConnection();
        gonnegtion.setDoOutput(true);
        gonnegtion.setRequestMethod("POST");
        String filename = saveImage(img);
        File imgFile = new File(filename);
        attachFile(imgFile, gonnegtion);
        BufferedReader reader = new BufferedReader(new InputStreamReader(gonnegtion.getInputStream(), "utf-8"));
        String resultContent = reader.readLine();
        resultContent = resultContent.substring(1, resultContent.length() - 3);
        int result = Integer.valueOf(resultContent);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Test");
        Mat img = Imgcodecs.imread("./OCR_server/data/test_1.jpg");
        try {
            System.out.println(getAthleteNumber(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
