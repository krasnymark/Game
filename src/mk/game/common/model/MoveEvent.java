package mk.game.common.model;

import java.util.EventObject;

public class MoveEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int player ;
	
	public MoveEvent(Object obj)
	{
		super(obj);
	}

	public void setSource(Object newSource)
	{
		source = newSource ;
	}

	public void applyMove(Game game, int player)
	{
		game.applyMove(getMove(), player) ;
	}

	public IGameMove getMove()
	{
		return (IGameMove) source ;
	}

	public int getPlayer()
	{
		return player;
	}

	public void setPlayer(int player)
	{
		this.player = player;
	}
}
