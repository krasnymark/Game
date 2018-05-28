package mk.game.checkers.model;

import mk.game.common.model.GameSettings;

public class CheckersGameSettings extends GameSettings
{
	public static final int SQUARE_SIZE_64 = 64 ;
	public static final int SQUARE_SIZE_100 = 100 ;

	private int boardSize = SQUARE_SIZE_64 ;
	private boolean isRussian = true ;
	private boolean isSuicide = false ;

	public int getBoardSize()
	{
		return boardSize;
	}

	public void setBoardSize(int boardSize)
	{
		this.boardSize = boardSize;
	}

	public boolean isRussian()
	{
		return isRussian;
	}

	public void setRussian(boolean isRussian)
	{
		this.isRussian = isRussian;
	}

	public boolean isSuicide()
	{
		return isSuicide;
	}

	public void setSuicide(boolean isSuicide)
	{
		this.isSuicide = isSuicide;
	}

	public CheckersGameSettings()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
