package components;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

public class DraggableLine
    implements Comparable
{
    private Color      color;
    private TimeFormat timestamp;
    private String     hipNumberLabel;
    private int        xPos;

    public DraggableLine()
    {
        // TODO complete no-args constructor
    }


    public DraggableLine(TimeFormat t, String h, int x)
    {
        // TODO complete constructor
    }


    public void setColor()
    {
        // TODO complete method
    }


    public void updateTimestamp()
    {
        // TODO complete method
    }


    public int compareTo(Object other)
    {
        // TODO complete comparator
        return 0;
    }


    public void processEvent(MouseEvent e)
    {
    }


    public void paintComponent(Graphics g)
    {
    }
}
