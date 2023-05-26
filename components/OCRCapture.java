package components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Handles capturing a stream from a second camera used for OCR hip number
 * recognition
 */
public class OCRCapture {
    private volatile ArrayList<SingleFrame> OCRstream;
    private long start;
    private volatile boolean terminated;
    private volatile boolean paused;
    private VideoCapture cap;
    private Queue<String> mailbox;

    /**
     * Constructs an OCRCapture when the start time is not known and must be set later on when known
     */
    public OCRCapture(int capIndex) {
        OCRstream = new ArrayList<SingleFrame>();
        this.start = 0;
        cap = new VideoCapture();
        mailbox = new LinkedList<String>();
        cap.open(capIndex);
        System.out.println("OCR CAMERA OPENED");
    }

    /**
     * Handles the running of the OCR capture in threaded synchronous form
     */
    public void execute() {
        Thread captureThread = new Thread() {
            @Override
            public void run() {
                Mat newFrame = new Mat();
                while (!terminated && !paused) {
                    long sampleTime = System.currentTimeMillis();
                    if (terminated) break;
                    boolean isRead = !terminated && !paused && cap.read(newFrame);
                    if (isRead) OCRstream.add(new SingleFrame(newFrame, sampleTime, start));
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
                        } else if (msg.equals("PAUSE")) {
                            paused = true;
                        } else if (msg.equals("STOP")) {
                            terminated = true;
                            paused = true;
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

    /**
     * sets the start time to that given by the CameraStarter and fed through the ThreadedCameraRunner
     * @param startTime the system time of the race's start
     */
    public void setStartTime(long startTime) {
        this.start = startTime;
    }

    /**
     * Getter for the OCR stream of SingleFrames
     * @return an ArrayList<SingleFrame> containing OCR captures
     */
    public ArrayList<SingleFrame> getOCRStream() {
        return OCRstream;
    }

    /**
     * handles communication between other components to pause, resume, and stop.
     * @param message the message to send the OCRCapture, either PAUSE, RESUME, or STOP.
     */
    public void receiveMessage(String message) {
        mailbox.add(message);
    }
}
