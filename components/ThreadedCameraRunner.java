package components;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

/**
 * ThreadedCameraRunner handles the real-time control and capturing of camera
 * data used for creating the finish image and optionally runs OCR by offloading
 * to OCRCapture.
 */
public class ThreadedCameraRunner
{

    private volatile Queue<SingleFrame> toBeProcessed;
    private volatile CompositeFrame     finishImage;
    private CameraStarter               starter;
    private long                        startTime;
    private Queue<String>               mailbox;
    private volatile boolean            paused;
    private volatile boolean            terminated;
    private VideoCapture                cap;
    private OCRCapture                  ocrc;
    private boolean                     useOCR = false;
    private SingleFrame                 pauseFrame;

    /**
     * Constructs a new ThreadedCameraRunner with a specified sound threshold
     * for sound-based starting (see CameraStarter documentation for details on
     * the sound thresholding)
     *
     * @param soundThreshold
     *            the sound threshold for starting, from 0 to 256
     */
    public ThreadedCameraRunner(int soundThreshold)
    {
        starter = new CameraStarter(soundThreshold);
        System.out.println("Sound Threshold: " + soundThreshold);
        basicConfig();
    }


    /**
     * Constructs a new ThreadedCameraRunner with the default sound threshold
     * for starting (see CameraStarter documentation for details on the sound
     * thresholding)
     */
    public ThreadedCameraRunner()
    {
        starter = new CameraStarter();
        basicConfig();
    }


    private void basicConfig()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        toBeProcessed = new LinkedList<SingleFrame>();
        finishImage = new CompositeFrame();
        mailbox = new LinkedList<String>();
        paused = true;
        terminated = false;
        cap = new VideoCapture();
        if (useOCR)
            cap.open(1);
        else
            cap.open(0); // change based on desired camera index
        System.out.println("MAIN CAMERA OPENED");
        pauseFrame = new SingleFrame(new Mat(480, 2, CvType.CV_8UC3, new Scalar(0, 255, 0)), 0, 0);
        startTime = 0;
        if (useOCR)
            ocrc = new OCRCapture(1);
    }


    /**
     * Received a control message, either STOP, PAUSE, or RESUME, which is then
     * handled by the control thread to perform the desired action. This is the
     * only way to communicate with the ThreadedCameraRunner.
     *
     * @param message
     *            The message / action item for the ThreadedCameraRunner
     */
    public void receiveMessage(String message)
    {
        mailbox.add(message);
        if (useOCR)
            ocrc.receiveMessage(message);
    }


    /**
     * Getter for the CompositeFrame built by the ThreadedCameraRunner
     *
     * @return the CompositeFrame associated with the ThreadedCameraRunner
     */
    public CompositeFrame getCompositeFrame()
    {
        return finishImage;
    }


    /**
     * ThreadedCameraRunner.execute() should be called exactly once to start the
     * camera with sound-based starting using CameraStarter and run the camera
     * according to control messages from receiveMessage()
     */
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
                    while (!paused)
                    {
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
                    if (!toBeProcessed.isEmpty())
                        finishImage.processFrame(toBeProcessed.poll());
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
                            for (int i = 0; i < 2; i++)
                            {
                                toBeProcessed.add(pauseFrame);
                            }
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


    /**
     * Getter for the ArrayList<SingleFrame> representing the second camera's
     * capture, used for OCR
     *
     * @return the OCR ArrayList<SingleFrame> or null if OCR is not being used
     */
    public ArrayList<SingleFrame> getOCRStream()
    {
        if (useOCR)
            return ocrc.getOCRStream();
        else
            return null;
    }


    /**
     * Getter for the system start time of the execution
     *
     * @return the system start time of the camera run.
     */
    public long getSystemStartTime()
    {
        return startTime;
    }
}
