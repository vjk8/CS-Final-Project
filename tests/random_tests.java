package tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.*;

public class random_tests {
    // testing out millisecond timing
    public static BufferedImage getImage() {
        BufferedImage b;
        try {
            b = ImageIO.read(new File("./photo.jpg"));
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            return null;
        }
        return b;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(getImage()));
        frame.setVisible(true);
    }
}
