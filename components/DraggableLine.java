package components;

import javax.swing.JTextField;

/**
 * DraggableLine class stores information about each of the draggable lines
 * created in PostTimingGUI. Has methods to find x position, hip number, and
 * seed time.
 */
public class DraggableLine implements Comparable {
    private TimeFormat timestamp;
    private int hipNumberLabel;
    private int xPos;
    private CompositeFrame frame;

    /**
     * editableHipNumber is a JTextField representing the hip number of the
     * athlete marked by this DraggableLine. This field is public to allow
     * editing by other classes such as PostTimingGUI, which displays and sets
     * the values of editableHipNumber according to user input
     */
    public JTextField editableHipNumber;

    /**
     * Initializes a draggable line with no info given.
     *
     * @param c
     *            the image of the finish
     */
    public DraggableLine(CompositeFrame c) {
        hipNumberLabel = -1;
        xPos = 5;
        timestamp = new TimeFormat();
        this.frame = c;
        commonConfig();
    }

    /**
     * Initializes a draggable line with seed time, hip number, and x position.
     *
     * @param t
     *            the finish time of the athlete
     * @param h
     *            the hip number of the athlete
     * @param x
     *            the x position of the line
     * @param c
     *            the image of the finish
     */
    public DraggableLine(TimeFormat t, int h, int x, CompositeFrame c) {
        timestamp = t;
        hipNumberLabel = h;
        xPos = x;
        this.frame = c;
        commonConfig();
    }

    private void commonConfig() {
        updateTimestamp();
        editableHipNumber = new JTextField("-1");
        editableHipNumber.setEditable(true);
        if (hipNumberLabel != -1) {
            editableHipNumber.setText(((Integer)hipNumberLabel).toString());
        }
    }

    /**
     * Returns the x position.
     *
     * @return the x position of the draggable line
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * When the draggable line is dragged, changes the x position. Also updates
     * the time of the athlete.
     *
     * @param newX
     *            the new x position
     * @return
     */
    public int changeXPos(int newX) {
        xPos = newX;
        updateTimestamp();
        return xPos;
    }

    /**
     * Updates the time of the athlete.
     */
    public void updateTimestamp() {
        if (frame == null)
            timestamp = new TimeFormat();
        else
            timestamp = frame.getTimeAtPixel(xPos);
    }

    /**
     * Returns the finish time.
     *
     * @return the finish time of the athlete.
     */
    public TimeFormat getTimestamp() {
        return timestamp;
    }

    /**
     * changes the hip number of the athlete
     * @param input The string input to be parsed for the hip number
     */
    public void setHipNumber(String input) {
        try {
            this.hipNumberLabel = Integer.parseInt(input);
        } catch (NumberFormatException nfex) {
            // do nothing
        }
    }

    /**
     * Returs the hip number.
     *
     * @return the hip number of the athlete.
     */
    public int getHipNumber() {

        return hipNumberLabel;
    }

    /**
     * Compares the draggable line to another based on the finish times.
     *
     * @return the two times compared to each other
     */
    public int compareTo(Object other) {
        DraggableLine ot = (DraggableLine)other;
        return timestamp.compareTo(ot.getTimestamp());
    }
}
