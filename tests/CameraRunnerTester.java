package tests;
import archive.CameraRunner;
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
        ThreadedCameraRunner tcr = new ThreadedCameraRunner(50);
        long executeStartTime = System.currentTimeMillis();
        tcr.execute();
        while (System.currentTimeMillis() - executeStartTime <= 20000) {
            System.out.println(new TimeFormat((int)(System.currentTimeMillis() - executeStartTime)));
            imshow(tcr.getCompositeFrame().getImage()); // error-prone line
        }
        tcr.receiveMessage("STOP");
        imshow(tcr.getCompositeFrame().getImage());
        System.out.println(tcr.getCompositeFrame().getTimestampList());
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
