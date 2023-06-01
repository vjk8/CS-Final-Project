package OCR_server;

import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;

public class AthleteOCR {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Save the image to a temporary file
     *
     * @param img
     *            the image to save
     * @return the filename of the saved image
     */
    private static String saveImage(Mat img) {
        String filename = "/tmp/" + ((int)(Math.random() * 596969) + 100000) + ".jpg";
        Imgcodecs.imwrite(filename, img);
        return filename;
    }

    /**
     * Attaches a file to the request
     *
     * @param file
     *            the file to attach
     * @param gonnegtion
     *            the gonnegtion
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

    private static class OCRResult {
        public int number;
        public int confidence;

        OCRResult(int n, int c) {
            number = n;
            confidence = c;
        }
    }

    /**
     * Get the number of the athlete in the image Runs one window of the sliding
     * window, works best if img contains only a number and minimal background
     *
     * @param img
     *            The image to be processed
     * @return The number of the athlete in the image
     */
    private static OCRResult runSubimageOCR(Mat img) throws IOException {
        URL url = new URL("http://127.0.0.1:6969/run");
        HttpURLConnection gonnegtion = (HttpURLConnection)url.openConnection();
        gonnegtion.setDoOutput(true);
        gonnegtion.setRequestMethod("POST");
        String filename = saveImage(img);
        File imgFile = new File(filename);
        attachFile(imgFile, gonnegtion);
        BufferedReader reader = new BufferedReader(new InputStreamReader(gonnegtion.getInputStream(), "utf-8"));
        String resultContent = reader.readLine();
        JSONObject obj = new JSONObject(resultContent);
        return new OCRResult(obj.getInt("number"), obj.getInt("confidence"));
    }

    private static int runFJML(Mat img) throws IOException {
        img = ImageProcessing.resize(ImageProcessing.invert(ImageProcessing.toGrayscale(img)), 28, 28);
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                writer.write(img.get(i, j)[0] / 255 + "\n");
            }
        }
        writer.close();
        return -1;
    }

    private static int getRewardEdge(double[] pixel) {
        if (pixel[0] <= 32 && pixel[1] <= 32 && pixel[2] <= 32) {
            // Black pixel, #202020 or less
            return -1;
        }
        if (pixel[0] >= 208 && pixel[1] >= 208 && pixel[2] >= 208) {
            // White pixel, #d0d0d0 or more
            return 5;
        }
        return -5;
    }

    private static int getRewardCenter(double[] pixel) {
        if (pixel[0] <= 32 && pixel[1] <= 32 && pixel[2] <= 32) {
            // Black pixel, #202020 or less
            return 5;
        }
        if (pixel[0] >= 208 && pixel[1] >= 208 && pixel[2] >= 208) {
            // White pixel, #d0d0d0 or more
            return 4;
        }
        return -10;
    }

    private static int[][] get2DPrefixSum(int[][] array) {
        int[][] result = array.clone();
        for (int i = 0; i < result.length; i++) {
            for (int j = 1; j < result[0].length; j++) {
                result[i][j] += result[i][j - 1];
            }
        }
        for (int i = 1; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] += result[i - 1][j];
            }
        }
        return result;
    }

    private static int calculatePrefixSumRect(int[][] prefixSum, int startRow, int startCol, int endRow, int endCol) {
        int ans = prefixSum[endRow][endCol];
        if (startRow > 0) {
            ans -= prefixSum[startRow - 1][endCol];
        }
        if (startCol > 0) {
            ans -= prefixSum[endRow][startCol - 1];
        }
        if (startRow > 0 && startCol > 0) {
            ans += prefixSum[startRow - 1][startCol - 1];
        }
        return ans;
    }

    private static void floodfill(int[][] grid, int startRow, int startCol, int fillNum) {
        final int[] dx = {1, -1, 0, 0}, dy = {0, 0, -1, 1};
        Queue<Integer> bfs = new LinkedList<Integer>();
        bfs.add(startRow * 696969 + startCol);
        grid[startRow][startCol] = fillNum;
        while (!bfs.isEmpty()) {
            int p = bfs.remove();
            int row = p / 696969, col = p % 696969;
            for (int i = 0; i < 4; i++) {
                int nr = row + dx[i], nc = col + dy[i];
                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length && grid[nr][nc] == -1) {
                    grid[nr][nc] = fillNum;
                    bfs.add(nr * 696969 + nc);
                }
            }
        }
    }

    /**
     * Runs the OCR on an image
     *
     * @param img
     *            the image
     * @return the number detected in the image
     */
    public static int getAthleteNumber(Mat img) throws IOException {
        int[][] grid = new int[img.rows()][img.cols()];
        int components = 0;
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                if (img.get(i, j)[0] >= 208 && img.get(i, j)[1] >= 208 && img.get(i, j)[2] >= 208) {
                    // Pixel is white (at least #d0d0d0)
                    grid[i][j] = -1;
                } else {
                    grid[i][j] = -2;
                }
            }
        }
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                if (grid[i][j] == -1) {
                    // System.out.println("Performing floodfill for " + i + ", "
                    // + j);
                    floodfill(grid, i, j, components);
                    components++;
                }
            }
        }
        int[][] boundingBoxes = new int[components][4]; // Min row, Min col, Max
                                                        // row, Max col
        for (int i = 0; i < components; i++) {
            boundingBoxes[i][0] = 696969;
            boundingBoxes[i][1] = 696969;
        }
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                if (grid[i][j] >= 0 && grid[i][j] < components) {
                    boundingBoxes[grid[i][j]][0] = Math.min(boundingBoxes[grid[i][j]][0], i);
                    boundingBoxes[grid[i][j]][1] = Math.min(boundingBoxes[grid[i][j]][1], j);
                    boundingBoxes[grid[i][j]][2] = Math.max(boundingBoxes[grid[i][j]][2], i);
                    boundingBoxes[grid[i][j]][3] = Math.max(boundingBoxes[grid[i][j]][3], j);
                }
            }
        }
        int best = -1, bestConf = -2;
        for (int c = 0; c < components; c++) { // the superior programming language
            if (boundingBoxes[c][2] - boundingBoxes[c][0] > 30 && boundingBoxes[c][3] - boundingBoxes[c][1] > 30) {
                System.out.println("Running OCR for box " + c + " with bounds " + boundingBoxes[c][0] + " " +
                                   boundingBoxes[c][1] + " " + boundingBoxes[c][2] + " " + boundingBoxes[c][3]);
                Mat rawSubmat =
                    img.submat(boundingBoxes[c][0], boundingBoxes[c][2], boundingBoxes[c][1], boundingBoxes[c][3]);
                // submat = ImageProcessing.deskew(submat);
                for (int angle = -15; angle <= 15; angle += 5) {
                    Mat submat = ImageProcessing.rotate(rawSubmat, angle);
                    OCRResult result = runSubimageOCR(submat);
                    if (result.confidence > bestConf) {
                        bestConf = result.confidence;
                        best = result.number;
                    }
                }
            }
        }
        return best;
    }

    public static void main(String[] args) {
        try {
            // runFJML(img);
            String[] files = {"./OCR_server/data/adarsh_1.jpg", "./OCR_server/data/david_5_1.jpg",
                              "./OCR_server/data/david_5_2.jpg", "./OCR_server/data/david_5_3.jpg"};
            for (String filename : files) {
                System.out.println("===========================================================\nOCR for file " +
                                   filename);
                Mat img = Imgcodecs.imread(filename);
                System.out.println();
                System.out.println("Result: " + getAthleteNumber(img));
                System.out.println("===========================================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
