package components;
import java.util.Queue;
import org.opencv.core.*;

public class CameraRunner implements Runnable {
    private Queue<SingleFrame> toBeProcessed;
    private CompositeFrame finishImage;

    public CameraRunner()
    {
        // TODO complete constructor
    }

    public void run()
    {
        // TODO complete method
    }

    public void receiveMessage()
    {
        // TODO complete method
    }

    public CompositeFrame getImage()
    {
        // TODO complete getter
        return null;
    }

    public Mat pause()
    {
        return null;
    }
}
