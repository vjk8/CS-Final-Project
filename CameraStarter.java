import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
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
        TargetDataLine line = getTargetDataLine();
        if (line == null) return null;
        // TODO complete method
        return null;
    }

    private TargetDataLine getTargetDataLine()
    {
        AudioFormat format = new AudioFormat(44100.0f, 64, 1, false, true);
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("AUDIO LINE NOT SUPPORTED");
            return null;
        }
        try {
            line = (TargetDataLine)AudioSystem.getLine(info);
            line.open(format);
        }
        catch (LineUnavailableException ex) {
            System.out.println("AUDIO LINE UNAVAILABLE");
            return null;
        }
        return line;
    }
}

class PlotSoundData { // for testing purposed later
}
