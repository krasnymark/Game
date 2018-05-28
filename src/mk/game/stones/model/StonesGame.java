/*
 * Created on Mar 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.stones.model;

import mk.game.stones.view.StonesView;
import mk.game.common.model.*;
import mk.game.common.view.GameView;

import javax.swing.*;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StonesGame extends Game
{
    public static final int defRowCount = 4; // Default # of rows to initialize the mk.game

    private int rowCount = defRowCount; // # of rows
    private int startingRows[]; // Optional Initial # of mk.game.stones in each row

	public StonesGame()
	{
		super();
	}

	public StonesGame(RootPaneContainer parent)
	{
		super(parent);
	}

	public String getName()
	{
		return "Stones";
	}

	public GameMoveManager getGameMoveManager()
	{
		return new StonesGameMoveManager(this);
	}

	public GameView getGameView()	
	{
		return new StonesView(this);
	}

	public GameView getGameView(RootPaneContainer parent)	
	{
		return new StonesView(this, parent);
	}

	public IGameState getGameState()
	{
		return new StonesGameState(this, this.getRowCount());
	}

    @Override
    public boolean needToCloneState()
    {
        return true; // State is allowed to change itself
    }

    public GameSettings getGameSettings()
	{
		return new StonesGameSettings();
	}

	public void init(RootPaneContainer parent)
	{
		super.init(parent);
	}
	
	public void applySettings()
	{
		super.applySettings();
	}

	public void startNewGame()
	{
        if (view != null)
        {
            StonesView stonesView = (StonesView) view;
            stonesView.reset();
        }
		super.startNewGame();
	}

	public GameMove getFirstMove()
	{
		return new StonesGameMove(0,0);
	}

	public void showHint(IGameMove move)
	{
//		StonesGameMove move = (StonesGameMove) aMove;
		if (move != null)
		{
			view.hint(move);
		}
	}
	
	public void showWinner()
	{
        // Nothing to show
	}

	public int getDepth()
	{
		return super.getDepth();
	}

    @Override
    public int getPlayerLevels()
    {
        return 5;
    }

    public boolean isDrawIfNoMoves()
	{
		return false;
	}

    public int getRowCount()
    {
        return rowCount == 0 ? defRowCount : rowCount;
    }

    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

    public int[] getStartingRows()
    {
        return startingRows;
    }

    public void setStartingRows(int[] startingRows)
    {
        this.startingRows = startingRows;
    }
}
