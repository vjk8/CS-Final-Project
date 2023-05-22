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
        extends JPanel {

    private volatile JButton startB;
    private volatile JButton stop;
    private volatile JButton pause;
    private volatile JButton resume;
    private volatile JFrame frame;
    private volatile JLabel label;
    private ThreadedCameraRunner camera;
    private volatile boolean terminated;

    // start button stop button and run analysis button (create post timing gui
    // and call its run)
    public LiveTimingGUI() {
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

    public void refresh(BufferedImage b) {
        if (b != null) {
            label.setIcon(new ImageIcon(b));
            System.out.println("b is not null");
        } else
            System.out.println("b is null");
    }

    public void run() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.execute();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("STOP");
                terminated = true;
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("PAUSE");
            }
        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.receiveMessage("RESUME");
            }
        });

        Thread refreshThread = new Thread() {
            @Override
            public void run() {
                while (!terminated) {
                    Mat compositeMat = camera.getCompositeFrame().getMat();
                    if (compositeMat == null)
                        System.out.print("\t");
                    else {
                        System.out.println(compositeMat.size());
                        //refresh(matToBufferedImage(compositeMat));
                    }

                }
            }
        };

        add(startB);
        add(stop);
        add(pause);
        add(resume);
        frame.add(this);
        frame.setVisible(true);
        refreshThread.start();
    }

    private static BufferedImage matToBufferedImage(Mat m) {
        System.out.println("in m to b");
        try {
            System.out.println("in try");
            if (m == null)
                return null;
            System.out.println("past m null condition");
            int type = BufferedImage.TYPE_3BYTE_BGR;
            int bufferSize = m.channels() * m.cols() * m.rows();
            System.out.println("buffersize is " + bufferSize);
            byte[] b = new byte[bufferSize];
            System.out.println("made new byte[]");
            try {
                System.out.println("trying to get pixels");
                m.get(0, 0, b); // get all the pixels
                System.out.println("got pixels");
            } catch (java.lang.Exception e) {
                System.out.println("unknown exception");
                return null;
            }

            if (m.cols() == 0 || m.rows() == 0)
                return null;
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        } catch (CvException cve) {
            System.out.println("CvException in buffered image conversion");
            return null;
        }

    }

    public static void main(String[] args) {
        LiveTimingGUI LTG = new LiveTimingGUI();
        LTG.run();
    }
}
