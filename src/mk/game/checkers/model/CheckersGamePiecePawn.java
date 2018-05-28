/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.GameMoveManager;

import java.awt.Point;

/**
 * @author mk
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGamePiecePawn extends CheckersGamePiece
{

	public CheckersGamePiecePawn(int player)
	{
		super(player) ;
	}

	public boolean isPawn()
	{
		return true ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GamePiece#getValue()
	 */
	public int getValue()
	{
		// TODO Auto-generated method stub
		return 10 ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GamePiece#getName()
	 */
	public String getName()
	{
		// TODO Auto-generated method stub
		return "pawn" ;
	}

	public boolean isDirectionForTakeOnly(Point dir)
	{
		return (dir.y < 0) == (getPlayer() == GameMoveManager.PLAYER_ONE) ;
	}

	public int getRange()
	{
		return 1 ;
	}

	public String shortDescr()
	{
		return " " ;
	}
}