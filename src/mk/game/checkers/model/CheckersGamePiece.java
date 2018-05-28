/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.GameMoveManager;
import mk.game.common.model.GamePiece;

import java.awt.Point;

/**
 * @author mk To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGamePiece extends GamePiece
{
	public static final int PAWN_VALUE = 50 ;
	public static final int QUEEN_VALUE = 125 ;
	public static final int SINGLE_QUEEN_BONUS = 75 ;
	private boolean isPawn = true ;

	public CheckersGamePiece(int player, boolean isPawn)
	{
		super(player);
		this.isPawn = isPawn;
	}

	public CheckersGamePiece(int player)
	{
		this(player, true) ;
	}

	public CheckersGamePiece(CheckersGamePiece piece)
	{
		this(piece.getPlayer(), piece.isPawn()) ;
	}

	/**
	 * Pawn can take but not move backwards.
	 */
	public boolean isDirectionForTakeOnly(Point dir)
	{
		return isPawn() && (dir.y < 0) == (getPlayer() == GameMoveManager.PLAYER_ONE) ;
	}

	public boolean isQueen()
	{
		return !isPawn() ;
	}

	/**
	 * Pawn answers 1, Q - infinity [board size]. In either case State will set
	 * the limit.
	 */
	public int getRange()
	{
		return isPawn() ? 1 : 100 ;
	}

	public int getValue(CheckersGameState state, Point point)
	{
		return isPawn() ? getPawnValue(state, point) : getQueenValue() ;
	}

	public int getValue()
	{
		return isPawn() ? getPawnValue() : getQueenValue() ;
	}

	public int getPawnValue(CheckersGameState state, Point point)
	{
		return state.getPieceValue(getPlayer(), this, point) ;
	}

	public int getPawnValue()
	{
		return PAWN_VALUE ;
	}

	public int getQueenValue()
	{
		return QUEEN_VALUE ;
	}

	public String getName()
	{
		return isPawn() ? "pawn" : "Queen" ;
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof CheckersGamePiece)
		{
			CheckersGamePiece piece = (CheckersGamePiece) obj ;
			if (piece.getPlayer() == getPlayer() && piece.isPawn() == isPawn()) return true ;
		}
		return false ;
	}
	
	public String toString()
	{
		return getPlayer() + shortDescr() ;
	}
	
	public String shortDescr()
	{
		return isPawn ? " " : "*" ;
	}


	public boolean isPawn()
	{
		return isPawn ;
	}
	public void setPawn(boolean isPawn)
	{
		this.isPawn = isPawn ;
	}
}