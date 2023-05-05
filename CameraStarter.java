import java.util.*;
import javax.sound.sampled.*;

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
        ArrayList<Byte> allData = new ArrayList<Byte>();
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
            System.out.print("Sample data: [");
            for (int i = 0; i < 10; i++) {
                System.out.print(data[i] + ",");
            }
            System.out.println("]");
            for (byte b : data) {
                allData.add(b);
            }
        }
        return null;
    }

    private static TargetDataLine getTargetDataLine()
    {
        AudioFormat format = new AudioFormat(44100f, 8, 1, true, true);
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        System.out.println(info.getFormats());

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
    { // for testing purposes only
        TimeFormat t = getStartTime();
        DataPlotter dp = new DataPlotter();
        dp.run();
    }
}

class DataPlotter { // for testing purposes later
    public DataPlotter(){};

    public static void run()
    {
        // TODO figure out how to plot data
    }
}
