/*
 * Created on Mar 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4Button extends JButton
{
	static final long serialVersionUID = 12345;

	protected static Color frameColor = Color.yellow;
	protected static Color freeColor = Color.red;
	protected Color blankColor = Color.lightGray;
	protected Color outlineColor = Color.black;
	protected Color playerColor = blankColor;
	protected int spacing = 10;
	protected int padding = 16;
	private int xx;

	public Connect4Button()
	{
		super();
	}

	public Connect4Button(int x)
	{
		super();
		setX(x);
	}

	protected void paintCell(Graphics g, Color playerColor) 
	{
		super.paint(g);
		Dimension size = getSize();
		int w = size.width;
		int h = size.height;
		int d = (w + h) / 2;
		int sp = d / spacing;
		int pd = d / padding;
		int sd = sp + pd;
        Paint paint = g.getColor();
		g.setColor(frameColor);
		g.fillRect(0, 0, w, h);
		g.setColor(outlineColor);
		g.fillOval(sp, sp, w - sp * 2, h - sp * 2);
		g.setColor(playerColor);
		g.fillOval(sd, sd, w - sd * 2, h - sd * 2);
	}

    @Override
	public void paint(Graphics g)
	{
		paintCell(g, getPlayerColor());
	}

	public Color getPlayerColor()
	{
		return playerColor;
	}

	public int getX()
	{
		return xx;
	}

	/**
	 * @param color
	 */
	public void setPlayerColor(Color color)
	{
		playerColor = color;
	}

	/**
	 * @param x
	 */
	public void setX(int x)
	{
		xx = x;
	}

	/**
	 * @return
	 */
	public Color getBlankColor()
	{
		return blankColor;
	}

	/**
	 * @param color
	 */
	public void setBlankColor(Color color)
	{
		blankColor = color;
	}

	/**
	 * @return
	 */
	public Color getOutlineColor()
	{
		return outlineColor;
	}

	/**
	 * @param color
	 */
	public void setOutlineColor(Color color)
	{
		outlineColor = color;
	}

	public static Color getFreeColor()
	{
		return freeColor;
	}
}
