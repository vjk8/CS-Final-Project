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

    public static TimeFormat getStartTime()
    {
        TargetDataLine line = getTargetDataLine();
        if (line == null) return null;
        int numBytesRead;
        byte[] data = new byte[line.getBufferSize() / 5];
        line.start();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            numBytesRead = line.read(data, 0, data.length);
            System.out.println(numBytesRead + " bytes read at time " +
                               (System.currentTimeMillis() - startTime));
        }
        // TODO complete method
        return null;
    }

    private static TargetDataLine getTargetDataLine()
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

    public static void main(String[] args)
    { // for testing purposed only
        TimeFormat t = getStartTime();
    }
}

class PlotSoundData { // for testing purposed later
}
