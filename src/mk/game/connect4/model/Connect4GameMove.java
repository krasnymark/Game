/*
 * Created on Mar 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.model;

import mk.game.common.model.*;

import java.awt.Point;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4GameMove extends GameMove
{
	int x;
	int y;

	public Connect4GameMove()
	{
		this(0,0);
	}

	public Connect4GameMove(int x)
	{
	  this(x, 0) ;
	}

	public Connect4GameMove(Point p)
	{
		this(p.x, p.y) ;
	}

	public Connect4GameMove(int x, int y)
	{
		setLocation(x, y);
		value = 0;
	}
	/**
	 * @return
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @param i
	 */
	public void setX(int i)
	{
		x = i;
	}
	
	public String getDelimiter()
	{
		return "," ;
	}

	public String toString()
	{
		return toString(getDelimiter()) + super.toString() ;
	}

	public String toString(String delimiter)
	{
		return "(" + (x + 1) + delimiter + (y + 1) + ")" ;
	}

	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Point asPoint()
	{
		return new Point(getX(), getY());
	}

	/**
	 * @return
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @param i
	 */
	public void setY(int i)
	{
		y = i;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof Connect4GameMove)
		{
			Connect4GameMove move = (Connect4GameMove) obj ;
			return move.equals(this) ;
		}
		else
		{
			return false ;
		}
	}

	public boolean equals(Connect4GameMove move)
	{
		return getX() == move.getX()
		    && getY() == move.getY() ;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return toString(getDelimiter()).hashCode() ;
	}

}
