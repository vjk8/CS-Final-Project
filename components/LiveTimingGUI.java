package components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import java.awt.image.DataBufferByte;

public class LiveTimingGUI
    extends JPanel
{

    private JButton              startB;
    private JButton              stop;
    private JButton              pause;
    private JButton              resume;
    private volatile JFrame      frame;
    private volatile JLabel      label;
    private ThreadedCameraRunner camera;
    private Thread               runner;
    private volatile boolean     terminated;

    // start button stop button and run analysis button (create post timing gui
    // and call its run)
    public LiveTimingGUI()
    {
        startB = new JButton("Start");
        stop = new JButton("Stop");
        pause = new JButton("Pause");
        resume = new JButton("Resume");
        frame = new JFrame();
        label = new JLabel();
        frame.setSize(200, 200);
        camera = new ThreadedCameraRunner();
        terminated = false;
    }


    public void refresh(BufferedImage b)
    {
        if (b != null)
        {
            label.setIcon(new ImageIcon(b));
            System.out.println("b is not null");
        }
        else
            System.out.println("b is null");
    }


    public void run()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                camera.execute();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                camera.receiveMessage("STOP");
                terminated = true;
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                camera.receiveMessage("PAUSE");
            }
        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                camera.receiveMessage("RESUME");
            }
        });

        add(startB);
        add(stop);
        add(pause);
        add(resume);
        frame.add(label);
        frame.add(this);
        frame.setVisible(true);

        while (!terminated)
        {
            Mat compositeMat = camera.getCompositeFrame().getMat();
            if (compositeMat == null)
                System.out.print("\t");
            else
            {
                System.out.println(camera.getCompositeFrame().getMat().size());
                refresh(matToBufferedImage(compositeMat));
            }

        }
    }


    private static BufferedImage matToBufferedImage(Mat m)
    {
        System.out.println("in m to b");
        try
        {
            System.out.println("in try");
            if (m == null)
                return null;
            int type = BufferedImage.TYPE_3BYTE_BGR;
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            try
            {
                m.get(0, 0, b); // get all the pixels
            }
            catch (java.lang.Exception e)
            {
                System.out.println("unknown exception");
                return null;
            }

            if (m.cols() == 0 || m.rows() == 0)
                return null;
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels =
                ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }
        catch (CvException cve)
        {
            System.out.println("CvException in buffered image conversion");
            return null;
        }

    }


    public static void main(String[] args)
    {
        LiveTimingGUI LTG = new LiveTimingGUI();
        LTG.run();
    }
}
