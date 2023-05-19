package components;

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

    public int getXPos() {
        return xPos;
    }

    public int changeXPos(int newX) {
        xPos = newX;
        return xPos;
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
}
