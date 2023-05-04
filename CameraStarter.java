import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class CameraStarter {
    private int threshold;

    public CameraStarter()
    {
        threshold = 0; // some default value
    }

    public CameraStarter(int th)
    {
        threshold = th;
    }

    public TimeFormat getStartTime()
    {
        AudioFormat format = new AudioFormat(44100.0f, 64, 1, false, true);
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        // TODO complete method
        return null;
    }
}

class PlotSoundData { // for testing purposed later

}
