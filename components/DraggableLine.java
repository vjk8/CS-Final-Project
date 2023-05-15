package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;

public class DraggableLine implements Comparable, MouseListener {
    private Color color;
    private TimeFormat timestamp;
    private String hipNumberLabel;
    private int xPos;
    private boolean dragged = false;

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

    public boolean isDragged()
    {
        return dragged;
    }

    public int compareTo(Object other)
    {
        // TODO complete comparator

        return timestamp.compareTo(other);
    }


    public void paintComponent(Graphics g) 
    {
        g.setColor(color);
        g.drawLine(xPos, 0, xPos, 200);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    // get new x position
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        xPos = e.getX();
    }

    @Override
    // translate the point by adding the x position, y value does not change
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        e.translatePoint(xPos, 0);
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        dragged = true;
        
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

        /*

    Point curr;
    Point prev;
     * public TApp()
    {
        curr = new Point(0,0);
        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawLine((int)curr.getX(), 0, (int)curr.getX(), 200);
    }
    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            prev = new Point(e.getScreenX(), e.getScreenY());
        }
    }

    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent e)
        {
            Point current = new Point(e.getScreenX(), e.getScreenY());

            curr.translate((int)(current.getX() - prev.getX()), (int)(current.getY() - prev.getY()));

            prev = current;
            repaint();
        }
    }
     */
}
