package components;

import java.lang.Thread;
import java.util.LinkedList;
import java.util.Queue;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

public class ThreadedCameraRunner {

    private Queue<SingleFrame> toBeProcessed;
    public CompositeFrame finishImage;
    private CameraStarter starter;
    private long startTime;
    private Queue<String> mailbox;
    private boolean paused;
    private boolean terminated;
    private VideoCapture cap;

    public ThreadedCameraRunner(int soundThreshold) {
        starter = new CameraStarter(soundThreshold);
        basicConfig();
    }

    public ThreadedCameraRunner() {
        starter = new CameraStarter();
        basicConfig();
    }

    public void basicConfig() {
        toBeProcessed = new LinkedList<SingleFrame>();
        finishImage = new CompositeFrame();
        mailbox = new LinkedList<String>();
        paused = false;
        terminated = false;
        cap = new VideoCapture();
        cap.open(0);
    }

    public void receiveMessage(String message) {
        mailbox.add(message);
    }

    public void execute() {
        startTime = starter.getStartTime();

        Thread captureThread = new Thread() {
            @Override
            public void run() {
                Mat newFrame = new Mat();
                while (!terminated) {
                    while (!paused) {
                        long sampleTime = System.currentTimeMillis();
                        boolean isRead = cap.read(newFrame);
                        if (isRead) toBeProcessed.add(new SingleFrame(newFrame, sampleTime, startTime));
                    }
                }
            }
        };

        Thread processThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    if (!toBeProcessed.isEmpty()) finishImage.processFrame(toBeProcessed.remove());
                }
            }
        };

        Thread controlThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    if (!mailbox.isEmpty()) {
                        String msg = mailbox.remove();
                        if (msg.equals("RESUME")) {
                            paused = false;
                        } else if (msg.equals("PAUSE")) {
                            paused = true;
                        } else if (msg.equals("STOP")) {
                            terminated = true;
                        }
                    }
                }
            }
        };
    }
}
