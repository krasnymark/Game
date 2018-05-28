/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.GameMove;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mk
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGameMove extends GameMove
{
	private static final long serialVersionUID = 1L;
	private CheckersGamePiece piece ;
	private Point fromPoint ;
	private Point toPoint ;
	private boolean isPawnToQueenMove = false ; // For take back

	/**
	 * @param piece
	 * @param fromPoint
	 * @param toPoint
	 */
	public CheckersGameMove(CheckersGamePiece piece, Point fromPoint, Point toPoint)
	{
		setPiece(piece) ;
		setFromPoint(fromPoint) ;
		setToPoint(toPoint) ;
	}

	public CheckersGameMove(Point fromPoint, Point toPoint)
	{
		this(null, fromPoint, toPoint) ;
	}

	public CheckersGameMove()
	{
		this(new Point(0, 0), new Point(0, 0)) ;
	}

	public CheckersGameMove(CheckersGameMove move)
	{
		this(move.getPiece(), move.getFromPoint(), move.getToPoint()) ;
	}

	public void apply(CheckersGameState state, int player)
	{
		state.applyMove(this, player) ;
	}
	
	public void takeBack(CheckersGameState state, int player)
	{
		state.takeBack(this, player) ;
	}

	public Point getFromPoint()
	{
		return fromPoint ;
	}

	public void setFromPoint(Point fromPoint)
	{
		this.fromPoint = fromPoint ;
	}

	public CheckersGamePiece getPiece()
	{
		return piece ;
	}

	public void setPiece(CheckersGamePiece piece)
	{
		this.piece = piece ;
	}

	public Point getToPoint()
	{
		return toPoint ;
	}

	public void setToPoint(Point toPoint)
	{
		this.toPoint = toPoint ;
	}

	public Point getEndPoint()
	{
		return getToPoint() ;
	}

	public List <Point> getAffectedPoints()
	{
		List <Point> points = new ArrayList <Point> () ;
		points.add(getFromPoint()) ;
		points.add(getToPoint()) ;
		return points ;
	}

	public boolean isPawnToQueenMove()
	{
		return isPawnToQueenMove ;
	}
	public void setPawnToQueenMove(boolean isPawnToQueenMove)
	{
		this.isPawnToQueenMove = isPawnToQueenMove ;
	}

	public void clear()
	{
		setPiece(null) ;
	}

	public boolean equals(CheckersGameMove move)
	{
		if (move instanceof CheckersGameTake)
		{
			CheckersGameMove take = (CheckersGameMove) move ;
			return take.equals(this) ;
		}
		else
		{
			return getFromPoint().equals(move.getFromPoint())
				&& getToPoint().equals(move.getToPoint())
				&& getPiece().equals(move.getPiece()) ;
		}
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof CheckersGameMove)
		{
			CheckersGameMove move = (CheckersGameMove) obj ;
			return move.equals(this) ;
		}
		else
		{
			return false ;
		}
	}
	
	public String getDelimiter()
	{
		return "-" ;
	}

	public String toString(String delimiter)
	{
		return getPiece().getPlayer() + ":" 
			+ CheckersGameMoveManager.printPoint(getFromPoint()) + getDelimiter()
			+ CheckersGameMoveManager.printPoint(getToPoint()) ;
	}

	public String toString()
	{
		return toString(getDelimiter()) + super.toString() ;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return toString(getDelimiter()).hashCode() ;
	}
}