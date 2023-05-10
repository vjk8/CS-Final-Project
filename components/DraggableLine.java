package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import org.w3c.dom.events.MouseEvent;

public class DraggableLine implements Comparable, MouseListener {
    private Color color;
    private TimeFormat timestamp;
    private String hipNumberLabel;
    private int xPos;

    public DraggableLine()
    {
        // TODO complete no-args constructor
        hipNumberLabel = "...";
        xPos = -1;
        timestamp = new TimeFormat();
    }

    public DraggableLine(TimeFormat t, String h, int x)
    {
        // TODO complete constructor
        timestamp = t;
        hipNumberLabel = h;
        xPos = x;
    }

    public void setColor()
    {
        // TODO complete method
        color = Color.red;
    }

    public void updateTimestamp()
    {
        // TODO complete method
        CompositeFrame c = new CompositeFrame();
        timestamp = c.getTimeAtPixel(xPos);
    }

    public int compareTo(Object other)
    {
        // TODO complete comparator

        return timestamp.compareTo(other);
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(Color.red);
        g.drawLine(10, 10, 100, 100);
    }

    @Override public void mouseClicked(java.awt.event.MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    // get new x position
    public void mousePressed(java.awt.event.MouseEvent e)
    {
        // TODO Auto-generated method stub
        xPos = e.getX();
    }

    @Override
    // translate the point by adding the x position, y value does not change
    public void mouseReleased(java.awt.event.MouseEvent e)
    {
        // TODO Auto-generated method stub
        e.translatePoint(xPos, 0);
    }

    @Override public void mouseEntered(java.awt.event.MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override public void mouseExited(java.awt.event.MouseEvent e)
    {
        // TODO Auto-generated method stub
    }
}
