import java.util.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CameraStarter {
    private static int threshold;
    private static boolean isSound;
    private static final int DEFAULT_THRESHOLD = 128;

    public CameraStarter() {
        threshold = DEFAULT_THRESHOLD;
        isSound = true;
    }

    public CameraStarter(boolean soundBased)
    {
        threshold = DEFAULT_THRESHOLD;
        isSound = soundBased;
    }

    public CameraStarter(int th, boolean soundBased)
    {
        threshold = th;
        isSound = soundBased;
    }

    public CameraStarter(int th)
    {
        threshold = th;
        isSound = true;
    }

    public static TimeFormat getStartTime()
    {
        if (isSound) return getSoundStartTime();
        else return getKeyboardStartTime();
    }

    private static TimeFormat getSoundStartTime() {
        double fractOfSecond = 0.05;
        ArrayList<Byte> allData = new ArrayList<Byte>();
        TargetDataLine line = getTargetDataLine();
        if (line == null) return null;
        int numBytesRead;
        byte[] data = new byte[(int)(line.getBufferSize() * 2 * fractOfSecond)];
        line.start();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10000) { // checks for 10 seconds
            long sampleTime = System.currentTimeMillis();
            numBytesRead = line.read(data, 0, data.length);
            int[] RI = rangeAndMaxIndex(data);
            if (RI[0] > threshold) {
                TimeFormat ret = new TimeFormat((int)(sampleTime - startTime + RI[1] / 8));
                System.out.println(ret);
                return ret;
            }
            System.out.println(numBytesRead + " bytes read starting at time " +
                               (sampleTime - startTime));
            for (byte b : data) {
                allData.add(b);
            }
        }
        return null;
    }

    private static TimeFormat getKeyboardStartTime() {
        KeyboardListener kbl = new KeyboardListener();
        long startTime = System.currentTimeMillis();
        long sampleTime = System.currentTimeMillis();
        while (sampleTime - startTime < 10000) {
            sampleTime = System.currentTimeMillis();
            if (kbl.isKeyPressed(KeyEvent.VK_ENTER) || kbl.isKeyPressed(KeyEvent.VK_SPACE)) {
                TimeFormat ret = new TimeFormat((int) (sampleTime - startTime));
                System.out.println(ret);
                return ret;
            }
        }
        return null;
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

    public static void main(String[] args)
    { // for testing purposes only
        TimeFormat t = getStartTime();
        DataPlotter dp = new DataPlotter();
        dp.run();
    }
}

class KeyboardListener {
    private static Map<Integer, Boolean> pressedKeys = new HashMap<Integer, Boolean>();

    // no-args constructor by default

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (KeyboardListener.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) pressedKeys.put(event.getKeyCode(), true);
                else if (event.getID() == KeyEvent.KEY_RELEASED) pressedKeys.put(event.getKeyCode(), false);
                return false;
            }
        });
    }

    public static boolean isKeyPressed(int keyCode) {
        return pressedKeys.getOrDefault(keyCode, false);
    }

    public static boolean isKeyPressed() {
        return !pressedKeys.isEmpty();
    }
}

class DataPlotter { // for testing purposes later
    public DataPlotter(){};

    public static void run()
    {
        // TODO figure out how to plot data
    }
}
