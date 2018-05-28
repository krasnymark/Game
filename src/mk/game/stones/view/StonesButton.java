/*
 * Created on Mar 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.stones.view;

import mk.game.common.view.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StonesButton extends JButton
{
	static final long serialVersionUID = 12345;

	protected static Color frameColor = Color.white;
	protected static Color selectedColor = Color.red;
	protected Color blankColor = Color.lightGray;
	protected Color outlineColor = Color.black ;
	protected int spacing =  8;
	protected int padding = 16;
    private int row;
    private boolean selected;
    private boolean preSelected;

	public StonesButton()
	{
		super();
	}

	public StonesButton(int row)
	{
		super();
        setRow(row);
	}

    public void paint(Graphics g) 
	{
		super.paint(g);
		Dimension size = getSize();
		int x = size.width;
		int y = size.height;
		int d = (x + y) / 2;
		int sp = d / spacing;
		int pd = d / padding;
		int sd = sp + pd;
        setBorder(new RoundedBorder(d / 2));
//        setBounds(sp, sp, x - sp * 2, y - sp * 2);
		g.setColor(frameColor);
//		g.fillRect(0, 0, x, y);
		g.setColor(outlineColor);
		g.fillOval(sp, sp, x - sp * 2, y - sp * 2);
		g.setColor(isSelected() || isPreSelected() ? selectedColor : blankColor);
		g.fillOval(sd, sd, x - sd * 2, y - sd * 2);
	}

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public void toggleSelected()
    {
        this.selected = !isSelected();
    }

    public void togglePreSelected()
    {
        this.preSelected = !isPreSelected();
    }

    public boolean isPreSelected()
    {
        return preSelected;
    }

    public void setPreSelected(boolean preSelected)
    {
        this.preSelected = preSelected;
    }

    @Override
    public String toString()
    {
        return "Row: " + getRow() + " Selected: " + isSelected() + " Enabled: " + isEnabled();
    }
}
