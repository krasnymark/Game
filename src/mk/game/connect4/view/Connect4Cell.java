/*
 * Created on Mar 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.view;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4Cell extends Connect4Button
{
	private int player ;
	private boolean outline ;
	private static Color[] playerColors ;

	/**
	 * 
	 */
	public Connect4Cell()
	{
		this(0) ;
	}
	public Connect4Cell(int player)
	{
		super();
		setPlayer(player) ;
	}

	public void init()
	{
//		spacing = 5 ;
		setOutline(false) ;
		setBlankColor(Color.white) ;
		setOutlineColor(frameColor) ;
	}

	@Override
    public void paint(Graphics g)
	{
		paintCell(g, getPlayerColor(player)) ;
	}

	/**
	 * @return
	 */
	public int getPlayer()
	{
		return player;
	}

	/**
	 * @param i
	 */
	public void setPlayer(int i)
	{
		player = i;
		if (player == 0) init() ;
	}

	public static Color[] getPlayerColors()
	{
		if (playerColors == null)
		{
			playerColors = new Color[3] ;   // index = value
			playerColors[0] = Color.white ; // Free
			playerColors[1] = Color.red ;   // Human
			playerColors[2] = Color.black ; // Computer
		}
		return playerColors;
	}

	public static Color getPlayerColor(int player)
	{
		return getPlayerColors()[player];
	}
	/**
	 * @return
	 */
	public boolean isOutline()
	{
		return outline;
	}

	/**
	 * @param b
	 */
	public void setOutline(boolean b)
	{
		outline = b;
		if (isOutline())
			setOutlineColor(playerColors[0]) ;
		else
			setOutlineColor(frameColor) ;

	}

}
