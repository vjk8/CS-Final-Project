import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LiveTimingGUI extends JPanel {

    JButton startB = new JButton("Start");
    JButton stop = new JButton("Stop");
    JButton analysis = new JButton("Run Analysis");
    PostTimingGUI pTG;
    CameraRunner camera;
    Thread runner;
    Mat image;
    // start button stop button and run analysis button (create post timing gui
    // and call its run)
    public LiveTimingGUI()
    {
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        camera = new CameraRunner();

        startB.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e)
            {
                runner = new Thread(camera);
                runner.start();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e)
            {
                image = runner.pause();
            }
        });

        analysis.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e)
            {
                pTG = new PostTimingGUI(image);
            }
        });

        add(startB);
        add(stop);
        add(analysis);
        frame.add(this);
        frame.setVisible(true);
    }
}
