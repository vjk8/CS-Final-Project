package tests;
import components.*;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CameraRunnerTester {

    public static void unthreadedTest() {
        CameraRunner cr = new CameraRunner();
        cr.run();
        System.out.println(cr.getCompositeFrame().getTimestampList());
    }

    public static void threadedTest() {
        ThreadedCameraRunner tcr = new ThreadedCameraRunner(128);
        long executeStartTime = System.currentTimeMillis();
        tcr.execute();
        while (System.currentTimeMillis() - executeStartTime <= 20000) {
            //BufferedImage b = tcr.getCompositeFrame().getImage();
            //if (b != null)
                //imshow(b);
            //else System.out.println("finish image is null");
        }
        tcr.receiveMessage("STOP");
        imshow(tcr.getCompositeFrame().getImage());
    }

    private static void imshow(BufferedImage b) {
        if (b == null) return;
        long startTime = System.currentTimeMillis();
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(b)));
        frame.pack();
        frame.setVisible(true);
        if (System.currentTimeMillis() - startTime >= 10000) {
            frame.dispose();
        }
    }
    public static void main(String[] args) {
        threadedTest();
        System.out.println("TERMINATED");
    }
}
