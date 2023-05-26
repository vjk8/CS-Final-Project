package components;

import java.awt.Color;

public class DraggableLine implements Comparable {
    private Color color;
    private TimeFormat timestamp;
    private int hipNumberLabel;
    private int xPos;
    private CompositeFrame frame;

    public DraggableLine(CompositeFrame c) {
        // TODO complete no-args constructor
        hipNumberLabel = -1;
        xPos = 5;
        timestamp = new TimeFormat();
        this.frame = c;
        updateTimestamp();
    }

    public DraggableLine(TimeFormat t, int h, int x, CompositeFrame c) {
        // TODO complete constructor
        timestamp = t;
        hipNumberLabel = h;
        xPos = x;
        this.frame = c;
        updateTimestamp();
    }

    public int getXPos() {
        return xPos;
    }

    public int changeXPos(int newX) {
        xPos = newX;
        updateTimestamp();
        return xPos;
    }

    public void setColor() {
        color = Color.red;
    }

    public void updateTimestamp() {
        timestamp = frame.getTimeAtPixel(xPos);
    }

    public TimeFormat getTimestamp() {
        return timestamp;
    }

    public int getHipNumber() {

        return hipNumberLabel;
    }

    public int compareTo(Object other) {
        return timestamp.compareTo(other);
    }
}
