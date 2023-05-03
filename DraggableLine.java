import java.awt.Color;

public class DraggableLine implements Comparable {
    private Color color;
    private TimeFormat timestamp;
    private String hipNumberLabel;
    private int xPos;

    public DraggableLine() {
        // TODO complete no-args constructor
        hipNumberLabel = "...";
        xPos = -1;
        timestamp = new TimeFormat();
    }

    public DraggableLine(TimeFormat t, String h, int x) {
        // TODO complete constructor
        timestamp = t;
        hipNumberLabel = h;
        xPos = x;
    }

    public void setColor() {
        // TODO complete method
        color = Color.red;
    }

    public void updateTimestamp() {
        // TODO complete method
        CompositeFrame c = new CompositeFrame();
        timestamp = c.getTimeAtPixel(xPos);
    }

    public int compareTo(Object other) {
        // TODO complete comparator
        
        return timestamp.compareTo(other);
    }

    public void processEvent(MouseEvent e) {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

    }
}
