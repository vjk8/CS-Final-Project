package components;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

public class ThreadedCameraRunner
{

    public volatile Queue<SingleFrame> toBeProcessed;
    private volatile CompositeFrame    finishImage;
    private CameraStarter              starter;
    private long                       startTime;
    private Queue<String>              mailbox;
    private volatile boolean           paused;
    private volatile boolean           terminated;
    private VideoCapture               cap;
    private OCRCapture                 ocrc;
    private boolean                    useOCR = false;

    public ThreadedCameraRunner(int soundThreshold)
    {
        starter = new CameraStarter(soundThreshold);
        System.out.println("Sound Threshold: " + soundThreshold);
        basicConfig();
    }


    public ThreadedCameraRunner()
    {
        starter = new CameraStarter();
        basicConfig();
    }


    public void basicConfig()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        toBeProcessed = new LinkedList<SingleFrame>();
        finishImage = new CompositeFrame();
        mailbox = new LinkedList<String>();
        paused = false;
        terminated = false;
        cap = new VideoCapture();
        cap.open(0);
        if (useOCR)
            ocrc = new OCRCapture();
    }


    public void receiveMessage(String message)
    {
        mailbox.add(message);
        if (useOCR)
            ocrc.receiveMessage(message);
    }


    public CompositeFrame getCompositeFrame()
    {
        return finishImage;
    }


    public void execute()
    {
        startTime = starter.getStartTime();

        if (useOCR)
            ocrc.setStartTime(startTime);

        Thread captureThread = new Thread() {
            @Override
            public void run()
            {
                Mat newFrame = new Mat();
                while (!terminated)
                {
                    while (!paused) {
                        // System.out.println("capturing");
                    long sampleTime = System.currentTimeMillis();
                    if (terminated)
                        break;
                    boolean isRead = !terminated && !paused && cap.read(newFrame);
                    if (isRead)
                        toBeProcessed.add(new SingleFrame(newFrame, sampleTime, startTime));
                    }
                    
                }
                Thread.currentThread().interrupt();
                return;
            }
        };

        Thread processThread = new Thread() {
            @Override
            public void run()
            {
                while (!terminated || !toBeProcessed.isEmpty())
                {
                    // System.out.println("processing");
                    if (!toBeProcessed.isEmpty())
                        finishImage.processFrame(toBeProcessed.remove());
                }
                Thread.currentThread().interrupt();
                return;
            }
        };

        Thread controlThread = new Thread() {
            @Override
            public void run()
            {
                while (!terminated)
                {
                    // System.out.println("controlling");
                    if (!mailbox.isEmpty())
                    {
                        String msg = mailbox.remove();
                        if (msg.equals("RESUME"))
                        {
                            paused = false;
                        }
                        else if (msg.equals("PAUSE"))
                        {
                            paused = true;
                            finishImage.addPause();
                        }
                        else if (msg.equals("STOP"))
                        {
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

        controlThread.start();
        captureThread.start();
        processThread.start();
        if (useOCR)
            ocrc.execute();
    }


    public ArrayList<SingleFrame> getOCRStream()
    {
        if (useOCR)
            return ocrc.getOCRStream();
        else
            return null;
    }
}
