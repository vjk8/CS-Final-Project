package components;

import java.util.ArrayList;
import java.util.LinkedList;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Mat;
import java.util.Queue;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class OCRCapture {
    private volatile ArrayList<SingleFrame> OCRstream;
    private long start;
    private volatile boolean terminated;
    private volatile boolean paused;
    private VideoCapture cap;
    private Queue<String> mailbox;

    public OCRCapture(long startTime) {
        OCRstream = new ArrayList<SingleFrame>();
        this.start = startTime;
        cap = new VideoCapture();
        mailbox = new LinkedList<String>();
    }

    public OCRCapture() {
        OCRstream = new ArrayList<SingleFrame>();
        this.start = 0;
        cap = new VideoCapture();
        mailbox = new LinkedList<String>();
    }

    public void execute() {

        cap.open(0);
        Thread captureThread = new Thread() {
            @Override
            public void run() {
                Mat newFrame = new Mat();
                while (!terminated && !paused) {
                    long sampleTime = System.currentTimeMillis();
                    if (terminated) break;
                    boolean isRead = !terminated && !paused && cap.read(newFrame);
                    if (isRead) OCRstream.add(new SingleFrame(newFrame, sampleTime, start));
                    System.out.println("OCR frame read with terminated = " + terminated);
                }
                Thread.currentThread().interrupt();
                return;
            }
        };

        Thread controlThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    // System.out.println("control");
                    if (!mailbox.isEmpty()) {
                        String msg = mailbox.remove();
                        if (msg.equals("RESUME")) {
                            paused = false;
                            System.out.println("set paused to false");
                        } else if (msg.equals("PAUSE")) {
                            paused = true;
                            System.out.println("set paused to true");
                        } else if (msg.equals("STOP")) {
                            terminated = true;
                            paused = true;
                            System.out.println("set terminated to true");
                            captureThread.interrupt();
                            cap.release();
                        }
                    }
                }
                Thread.currentThread().interrupt();
                return;
            }
        };

        captureThread.start();
        controlThread.start();
    }

    public void setStartTime(long startTime) {
        this.start = startTime;
    }

    public ArrayList<SingleFrame> getOCRStream() {
        return OCRstream;
    }

    public void receiveMessage(String message) {
        mailbox.add(message);
        System.out.println("RECEIVED MESSAGE " + message);
    }
}