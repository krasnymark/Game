package mk.game.checkers.model ;

import mk.game.common.model.IGameMove;

import java.awt.Point;
import java.util.List;
import java.util.Stack;

public class CheckersGameTake extends CheckersGameMove
{
	private CheckersGameTake nextLeg = null ;
	private CheckersGamePiece opponentsPiece = null ;
	private Point opponentsPoint = null ;

	public CheckersGameTake(CheckersGamePiece piece, Point fromPoint, Point toPoint
			, CheckersGamePiece opponentsPiece, Point opponentsPoint)
	{
		super(piece, fromPoint, toPoint) ;
		setOpponentsPiece(opponentsPiece) ;
		setOpponentsPoint(opponentsPoint) ;
	}

	public CheckersGameTake(CheckersGameTake take)
	{
		this(take.getPiece(), take.getFromPoint(), take.getToPoint()
				, take.getOpponentsPiece(), take.getOpponentsPoint()) ;
	}
	
	public void apply(CheckersGameState state, int player)
	{
		state.applyMove(this, player) ;
	}
	
	public void takeBack(CheckersGameState state, int player)
	{
		state.takeBack(this, player) ;
	}

	public Stack reverse()
	{
		// Reverse the legs for a take back.
		Stack <IGameMove> stack = new Stack <IGameMove> () ;
		CheckersGameTake next = this ;
		while (next != null)
		{
			stack.push(next) ;
			next = next.getNextLeg() ;
		}
		return stack ;
	}

	/**
	 * @return Returns the nextLeg.
	 */
	public CheckersGameTake getNextLeg()
	{
		return nextLeg ;
	}

	/**
	 * @param nextLeg
	 *           The nextLeg to set.
	 */
	public void setNextLeg(CheckersGameTake nextLeg)
	{
		this.nextLeg = nextLeg ;
	}

	public CheckersGamePiece getOpponentsPiece()
	{
		return opponentsPiece ;
	}

	public void setOpponentsPiece(CheckersGamePiece opponentsPiece)
	{
		this.opponentsPiece = opponentsPiece ;
	}

	public Point getEndPoint()
	{
		return (nextLeg == null) ? getToPoint() : nextLeg.getEndPoint() ;
	}

	public List <Point> getAffectedPoints()
	{
		List <Point> points = super.getAffectedPoints() ;
		points.add(getOpponentsPoint()) ;
		if (nextLeg != null) points.addAll(nextLeg.getAffectedPoints()) ;
		return points ;
	}

	public boolean equals(CheckersGameMove move)
	{
		if (move instanceof CheckersGameTake)
		{
			CheckersGameTake take = (CheckersGameTake) move ;
			return getFromPoint().equals(move.getFromPoint())
				&& getToPoint().equals(move.getToPoint())
				&& getPiece().equals(move.getPiece()) 
				&& ((getNextLeg() == null && take.getNextLeg() == null)
				|| (getNextLeg().equals(take.getNextLeg()))) ;
		}
		else
		{
			return false ;
		}
	}
	
	public String getName()
	{
		return "take" ;
	}
	
	public String getDelimiter()
	{
		return "+" ;
	}

	public Point getOpponentsPoint()
	{
		return opponentsPoint;
	}

	public void setOpponentsPoint(Point opponentsPoint)
	{
		this.opponentsPoint = opponentsPoint;
	}
}