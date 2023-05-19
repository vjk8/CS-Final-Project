package components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.Mat;

public class LiveTimingGUI extends JPanel {

    private JButton startB;
    private JButton stop;
    private JButton pause;
    private JButton resume;
    private JFrame frame;
    private JLabel label;
    private ThreadedCameraRunner camera;
    private Thread runner;
    private boolean terminated;

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
        label.setIcon(new ImageIcon(b));
    }

    public void run() {
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

        add(startB);
        add(stop);
        add(pause);
        add(resume);
        frame.add(label);
        frame.add(this);
        frame.setVisible(true);

        while (!terminated) {
            refresh(camera.getCompositeFrame().getImage());
        }
    }

    public static void main(String[] args) {
        LiveTimingGUI run = new LiveTimingGUI();
    }
}
