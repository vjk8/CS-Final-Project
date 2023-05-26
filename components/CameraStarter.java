package components;

import javax.sound.sampled.*;

/**
 * CameraStarter uses the microphone to immediately start races when the
 * amplitude of sound exceeds a certain theshold. Thus, it responds accurately
 * to starting pistols and claps for reliable timing
 */
public class CameraStarter
{
    private static int       threshold;
    private static final int DEFAULT_THRESHOLD = 200;

    /**
     * No-args constructor creates a new CameraStarter with a default threshold.
     * Likely, the threshold will need fine-tuning, so this constructor is not
     * recommended.
     */
    public CameraStarter()
    {
        threshold = DEFAULT_THRESHOLD;
    }


    /**
     * Constructs a CameraStarter with a user-specified constructor. This is the
     * recommended constructor.
     * 
     * @param soundThreshold
     *            the threshold, from 0 through 256, for the sound amplitude to
     *            exceed in order for the start to be registered.
     */
    public CameraStarter(int soundThreshold)
    {
        threshold = soundThreshold;
    }

    /**
     * Blocks for 10 seconds while waiting for the sound threshold to be exceeded. If the threshold is not exceeded in this interval, returns -1 and may lead to downstream errors.
     * @return The start time, in system milliseconds (hence the long data type)
     */
    public static long getStartTime()
    {
        double fractOfSecond = 0.05;
        TargetDataLine line = getTargetDataLine();
        if (line == null)
            return -1;
        byte[] data = new byte[(int)(line.getBufferSize() * 2 * fractOfSecond)];
        line.start();
        long startTime = System.currentTimeMillis();
        System.out.println("AWAITING START");
        while (System.currentTimeMillis() - startTime < 10000)
        {
            long sampleTime = System.currentTimeMillis();
            // System.out.println("awaiting at time " + sampleTime);
            line.read(data, 0, data.length);
            int[] RI = rangeAndMaxIndex(data);
            if (RI[0] > threshold)
            {
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
        for (int i = 0; i < a.length; i++)
        {
            byte b = a[i];
            if (b < min)
            {
                min = b;
            }
            if (b > max)
            {
                max = b;
            }
            if (Math.abs(b) > absMax)
            {
                absMax = Math.abs(b);
                maxIndex = i;
            }
        }
        return new int[] { (int)(max - min), maxIndex };
    }


    private static TargetDataLine getTargetDataLine()
    {
        AudioFormat format = new AudioFormat(8000f, 8, 1, true, true);
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        System.out.println(info.getFormats());

        if (!AudioSystem.isLineSupported(info))
        {
            System.out.println("AUDIO LINE NOT SUPPORTED");
            return null;
        }
        try
        {
            line = (TargetDataLine)AudioSystem.getLine(info);
            line.open(format);
        }
        catch (LineUnavailableException ex)
        {
            System.out.println("AUDIO LINE UNAVAILABLE");
            return null;
        }
        return line;
    }
}
