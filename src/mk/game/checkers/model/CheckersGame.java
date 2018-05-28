/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.*;
import mk.game.common.view.GameView;

import javax.swing.RootPaneContainer;

import mk.game.checkers.view.CheckersView;

/**
 * @author mk To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGame extends Game
{

	public CheckersGame()
	{
		super() ;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 */
	public CheckersGame(RootPaneContainer parent)
	{
		super(parent) ;
		// TODO Auto-generated constructor stub
	}

    @Override
	public void applyMove(IGameMove move, int player)
	{
		if (move == null) setWinner(GameMoveManager.getOpponent(player)) ;
		super.applyMove(move, player) ;
	}

    @Override
	public GameMoveManager getGameMoveManager()
	{
		return new CheckersGameMoveManager(this) ;
	}

    @Override
	public IGameState getGameState()
	{
		CheckersGameSettings gameSettings = (CheckersGameSettings) getSettings() ;
		return new CheckersGameState(this, gameSettings.getBoardSize());
	}

    @Override
	public GameSettings getGameSettings()
	{
		return new CheckersGameSettings();
	}

    @Override
	public GameView getGameView()
	{
		return getGameView(null) ;
	}

    @Override
	public GameView getGameView(RootPaneContainer parent)
	{
		CheckersGameState state = (CheckersGameState) getState() ;
		return new CheckersView(state.getSize(), this, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.Game#getName()
	 */
    @Override
	public String getName()
	{
		return "Checkers" ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.Game#applySettings()
	 */
    @Override
	public void applySettings()
	{
		// TODO Auto-generated method stub
		super.applySettings() ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.Game#showWinner()
	 */
    @Override
	public void showWinner()
	{
		// TODO Auto-generated method stub
	}

    @Override
	public int getDepth(int player)
	{
		return super.getDepth(player) ;
	}

	@Override
    public boolean isDrawIfNoMoves()
	{
		return false;
	}

}