package components;

import java.awt.Color;

/**
 * DraggableLine class stores information about each
 * of the draggable lines created in PostTimingGUI.
 * Has methods to find x position, hip number,
 * and seed time.
 */
public class DraggableLine implements Comparable {
    private Color color;
    private TimeFormat timestamp;
    private int hipNumberLabel;
    private int xPos;
    private CompositeFrame frame;

    /**
     * Initializes a draggable line with no info given.
     * @param c the image of the finish
     */
    public DraggableLine(CompositeFrame c) {
        hipNumberLabel = -1;
        xPos = 5;
        timestamp = new TimeFormat();
        this.frame = c;
        updateTimestamp();
    }

    /**
     * Initializes a draggable line with seed time,
     * hip number, and x position.
     * @param t the finish time of the athlete
     * @param h the hip number of the athlete
     * @param x the x position of the line
     * @param c the image of the finish
     */
    public DraggableLine(TimeFormat t, int h, int x, CompositeFrame c) {
        timestamp = t;
        hipNumberLabel = h;
        xPos = x;
        this.frame = c;
        updateTimestamp();
    }

    /**
     * Returns the x position.
     * @return the x position of the draggable line
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * When the draggable line is dragged, changes the x position.
     * Also updates the time of the athlete.
     * @param newX the new x position
     * @return
     */
    public int changeXPos(int newX) {
        xPos = newX;
        updateTimestamp();
        return xPos;
    }

    /**
     * Sets the color of the draggable line.
     */
    public void setColor() {
        color = Color.red;
    }

    /**
     * Updates the time of the athlete.
     */
    public void updateTimestamp() {
        timestamp = frame.getTimeAtPixel(xPos);
    }

    /**
     * Returns the finish time.
     * @return the finish time of the athlete.
     */
    public TimeFormat getTimestamp() {
        return timestamp;
    }

    /**
     * Returs the hip number.
     * @return the hip number of the athlete.
     */
    public int getHipNumber() {

        return hipNumberLabel;
    }

    /**
     * Compares the draggable line to another based
     * on the finish times.
     * @return the two times compared to each other
     */
    public int compareTo(Object other) {
        return timestamp.compareTo(other);
    }
}
