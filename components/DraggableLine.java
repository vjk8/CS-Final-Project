package components;

import java.awt.Color;

public class DraggableLine implements Comparable {
    private Color color;
    private TimeFormat timestamp;
    private int hipNumberLabel;
    private int xPos;

    public DraggableLine() {
        // TODO complete no-args constructor
        hipNumberLabel = -1;
        xPos = 0;
        timestamp = new TimeFormat();
    }

    public DraggableLine(TimeFormat t, int h, int x) {
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
        color = Color.red;
    }

    public void updateTimestamp() {
        CompositeFrame c = new CompositeFrame();
        timestamp = c.getTimeAtPixel(xPos);
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
