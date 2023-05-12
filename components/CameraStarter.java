package components;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.sound.sampled.*;

public class CameraStarter
{
    private static int       threshold;
    private static final int DEFAULT_THRESHOLD = 200;

    public CameraStarter() {
        threshold = DEFAULT_THRESHOLD;
    }

    public CameraStarter(int soundThreshold) {
        threshold = soundThreshold;
    }


    public static long getStartTime()
    {
        double fractOfSecond = 0.05;
        TargetDataLine line = getTargetDataLine();
        if (line == null) return -1;
        byte[] data = new byte[(int)(line.getBufferSize() * 2 * fractOfSecond)];
        line.start();
        long startTime = System.currentTimeMillis();
        System.out.println("AWAITING START");
        while (System.currentTimeMillis() - startTime < 10000) {
            long sampleTime = System.currentTimeMillis();
            line.read(data, 0, data.length);
            int[] RI = rangeAndMaxIndex(data);
            if (RI[0] > threshold) {
                long ret = sampleTime + RI[1] / 8;
                System.out.println("STARTED at system time " + ret);
                return ret;
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
        } catch (LineUnavailableException ex) {
            System.out.println("AUDIO LINE UNAVAILABLE");
            return null;
        }
        return line;
    }
}

