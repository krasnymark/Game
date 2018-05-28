/*
 * Created on May 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.model;

import mk.game.common.model.Game.PlayerType;
import mk.game.common.model.Game.TimeType;



/**
 * @author mk
 *
 * Wraps all the generic settings for save, restore, and set purposes.
 * Subclasses should add mk.game specific settings
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GameSettings
{
	private boolean isSaveOnExit = true;
	private boolean isThinkAhead = false;
	private int processorCount = 1;
	private TimeType timeType = TimeType.Unlimited;  
	private IGamePlayer[] IGamePlayer = new IGamePlayer[3]; // Skip 0 to use player as an index

	public GameSettings() // Default Settings
	{
		super();
		// TODO Auto-generated constructor stub
		IGamePlayer[1] = new GamePlayer("1", PlayerType.Human);
		IGamePlayer[2] = new GamePlayer("2", PlayerType.Computer);
	}

	public boolean isSaveOnExit()
	{
		return isSaveOnExit;
	}

	public void setSaveOnExit(boolean isSaveOnExit)
	{
		this.isSaveOnExit = isSaveOnExit;
	}

	public IGamePlayer getGamePlayer(int player)
	{
		return IGamePlayer[player];
	}

	public void setGamePlayer(int player, IGamePlayer IGamePlayer)
	{
		this.IGamePlayer[player] = IGamePlayer;
	}

	public IGamePlayer[] getGamePlayer()
	{
		return IGamePlayer;
	}

	public void setGamePlayer(IGamePlayer[] IGamePlayer)
	{
		this.IGamePlayer = IGamePlayer;
	}

	public int getProcessorCount()
	{
		return processorCount;
	}

	public void setProcessorCount(int processorCount)
	{
		this.processorCount = processorCount;
	}

	public TimeType getTimeType()
	{
		return timeType;
	}

	public void setTimeType(TimeType timeType)
	{
		this.timeType = timeType;
	}

	public boolean isThinkAhead()
	{
		return isThinkAhead && IGamePlayer[1].isHuman() != IGamePlayer[2].isHuman();
	}

	public void setThinkAhead(boolean isThinkAhead)
	{
		this.isThinkAhead = isThinkAhead;
	}

}
