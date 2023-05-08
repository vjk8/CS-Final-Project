package components;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.sound.sampled.*;

public class CameraStarter {
    private static int threshold;
    private static boolean isSound;
    private static final int DEFAULT_THRESHOLD = 200;

    public CameraStarter()
    {
        threshold = DEFAULT_THRESHOLD;
        isSound = true;
    }

    public CameraStarter(boolean soundBased)
    {
        threshold = DEFAULT_THRESHOLD;
        isSound = soundBased;
    }

    public CameraStarter(int soundThreshold, boolean soundBased)
    {
        threshold = soundThreshold;
        isSound = soundBased;
    }

    public CameraStarter(int soundThreshold)
    {
        threshold = soundThreshold;
        isSound = true;
    }

    public static long getStartTime()
    {
        if (isSound)
            return getSoundStartTime();
        else
            return getKeyboardStartTime();
    }

    private static long getSoundStartTime()
    {
        double fractOfSecond = 0.05;
        ArrayList<Byte> allData = new ArrayList<Byte>();
        TargetDataLine line = getTargetDataLine();
        if (line == null) return -1;
        int numBytesRead;
        byte[] data = new byte[(int)(line.getBufferSize() * 2 * fractOfSecond)];
        line.start();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <
               10000) { // checks for 10 seconds
            long sampleTime = System.currentTimeMillis();
            numBytesRead = line.read(data, 0, data.length);
            int[] RI = rangeAndMaxIndex(data);
            if (RI[0] > threshold) {
                long ret = sampleTime + RI[1] / 8;
                System.out.println(ret);
                return ret;
            }
            System.out.println(RI[0] + " range at time " +
                               (sampleTime - startTime));
            for (byte b : data) {
                allData.add(b);
            }
        }
        return -1;
    }

    private static long getKeyboardStartTime()
    {
        KeyboardListener kbl = new KeyboardListener();
        long startTime = System.currentTimeMillis();
        long sampleTime = System.currentTimeMillis();
        while (sampleTime - startTime < 10000) {
            sampleTime = System.currentTimeMillis();
            if (kbl.isKeyPressed(KeyEvent.VK_ENTER) ||
                kbl.isKeyPressed(KeyEvent.VK_SPACE)) {
                System.out.println(sampleTime);
                return sampleTime;
            }
        }
        return -1;
    }

    private static int[] rangeAndMaxIndex(byte[] a)
    {
        byte max = Byte.MIN_VALUE;
        byte min = Byte.MAX_VALUE;
        int absMax = Byte.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < a.length; i++) {
            byte b = a[i];
            if (b < min) {
                min = b;
            }
            if (b > max) {
                max = b;
            }
            if (Math.abs(b) > absMax) {
                absMax = Math.abs(b);
                maxIndex = i;
            }
        }
        return new int[] {(int)(max - min), maxIndex};
    }

    private int indexOfMax(byte[] a)
    { // superseded by rangeAndMaxIndex
        int max = Byte.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i]) > max) {
                max = Math.abs(a[i]);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static TargetDataLine getTargetDataLine()
    {
        AudioFormat format = new AudioFormat(8000f, 8, 1, true, true);
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
}

class KeyboardListener {
    private static Map<Integer, Boolean> pressedKeys =
        new HashMap<Integer, Boolean>();

    // no-args constructor by default

    static
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (KeyboardListener.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED)
                    pressedKeys.put(event.getKeyCode(), true);
                else if (event.getID() == KeyEvent.KEY_RELEASED)
                    pressedKeys.put(event.getKeyCode(), false);
                return false;
    }
});
}

public static boolean isKeyPressed(int keyCode)
{
    return pressedKeys.getOrDefault(keyCode, false);
}

public static boolean isKeyPressed()
{
    return !pressedKeys.isEmpty();
}
}
