package mk.game.common.model;

import java.io.Serializable;

/**
 * GameMove represents a move (as opposed to the state of a mk.game). It can be
 * used to communicate a recommended next move, or a proposed move for testing.
 * Its subclasses should add the data necessary to represent the move itself.
 * It may happen that there are no moves any more. 
 * Depending on the mk.game it could mean a draw or a win.
 */
public class GameMove implements Serializable, IGameMove
{
	private static final long serialVersionUID = 1L;
	protected int value = 0 ; // move's value as returned by the evaluate method
	protected long time = 0 ; // Time spent on this move
	
	@Override
    public String getDescr()
	{
		return " Move: " + this + " Time: " + GameTimer.formatMillis(time) ;
	}

	@Override
    public long getTime()
	{
		return time ;
	}
	@Override
    public void setTime(long time)
	{
		this.time = time ;
	}
	@Override
    public int getValue()
	{
		return value ;
	}
	@Override
    public void setValue(int value)
	{
		this.value = value ;
	}

	public String toString()
	{
		return "[" + value + "]" ;
	}
	
	public int hashCode()
	{
		return this.toString().hashCode() ;
	}
}