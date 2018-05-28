/*
 * Created on Jun 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mk.game.checkers.view;

import mk.game.common.view.GameView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

import mk.game.checkers.model.CheckersGameCell;

/**
 * @author u382034
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckersViewCell extends JButton
{
	static final long serialVersionUID = 12345 ;
	static final int spacing = 5 ;
	static final int padding = 16 ;

	protected CheckersGameCell gameCell = null ;
	protected boolean isHilighted = false ;

	protected static Color backgroundColor = Color.lightGray ;
	protected static Color freeColor = Color.red ;
	protected Color blankColor = Color.lightGray ;
	protected Color outlineColor = Color.black ;
	protected Color hilightColor = Color.white ;
	protected Color playerColor = blankColor ;

	public CheckersViewCell()
	{
		super() ;
	}

	public CheckersViewCell(CheckersGameCell gameCell)
	{
		super() ;
		setGameCell(gameCell) ;
	}

	protected void paintCell(Graphics g, Color playerColor) 
	{
		super.paint(g) ;
		Dimension size = getSize() ;
		int x = size.width  ;
		int y = size.height ;
		int d = (x + y) / 2 ;
		int sp = d / spacing ;
		int pd = d / padding ;
		g.setColor(backgroundColor) ;
		g.fillRect(0, 0, x, y) ;
		if (getGameCell() == null) return ;
		if (getGameCell().getPlayer() == 0)
		{
			if (isHilighted())
			{
				g.setColor(outlineColor) ;
				g.fillRect(0, 0, x, y) ;
				g.setColor(backgroundColor) ;
				g.fillRect(pd, pd, x-pd*2, y-pd*2) ;
			}
		}
		else
		{
			if (getGameCell().getPiece().isQueen()) sp = sp /2 ;
			int p = sp + pd ;
			g.setColor(isHilighted() ? hilightColor : outlineColor) ;
			g.fillOval(sp, sp, x - sp * 2, y - sp * 2) ;
			g.setColor(GameView.getPlayerColors()[getGameCell().getPlayer()]) ;
			g.fillOval(p, p, x - p * 2, y - p * 2) ;
		}
	}

	public void paint(Graphics g) 
	{
		paintCell(g, this.playerColor) ;
	}

	public CheckersGameCell getGameCell()
	{
		return gameCell ;
	}
	public void setGameCell(CheckersGameCell gameCell)
	{
		this.gameCell = gameCell ;
	}
	public boolean isHilighted()
	{
		return isHilighted ;
	}
	public void setHilighted(boolean isHiLighted)
	{
		this.isHilighted = isHiLighted ;
	}
	public void hilight(boolean hi)
	{
		setHilighted(hi) ;
		repaint() ;
	}
	public void hilight()
	{
		hilight(true) ;
	}
	public void deHilight()
	{
		hilight(false) ;
	}

}
