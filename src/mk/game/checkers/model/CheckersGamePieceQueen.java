/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model;

import java.awt.Point;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGamePieceQueen extends CheckersGamePiece
{
	public CheckersGamePieceQueen(int player)
	{
		super(player) ;
	}

	public boolean isPawn()
	{
		return false ;
	}

	public boolean isDirectionForTakeOnly(Point dir)
	{
		// TODO Auto-generated method stub
		return false ;
	}

	/* (non-Javadoc)
	 * @see mk.game.common.model.GamePiece#getValue()
	 */
	public int getValue()
	{
		// TODO Auto-generated method stub
		return 35 ;
	}

	/* (non-Javadoc)
	 * @see mk.game.common.model.GamePiece#getName()
	 */
	public String getName()
	{
		// TODO Auto-generated method stub
		return "Queen" ;
	}

	public int getRange()
	{
		return 100 ;
	}

	public String shortDescr()
	{
		return "*" ;
	}
}
