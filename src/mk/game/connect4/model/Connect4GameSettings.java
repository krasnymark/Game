package mk.game.connect4.model;

import mk.game.common.model.*;

public class Connect4GameSettings extends GameSettings
{
	private int connectSize = 4 ;
	private int xSize = 7 ;
	private int ySize = 6 ;

	public int getConnectSize()
	{
		return connectSize;
	}

	public void setConnectSize(int connectSize)
	{
		this.connectSize = connectSize;
	}

	public int getXSize()
	{
		return xSize;
	}

	public void setXSize(int size)
	{
		xSize = size;
	}

	public int getYSize()
	{
		return ySize;
	}

	public void setYSize(int size)
	{
		ySize = size;
	}

	public Connect4GameSettings()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
