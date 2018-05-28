/*
 * Created on Mar 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.model;

import mk.game.common.model.*;
import mk.game.common.view.*;

import javax.swing.RootPaneContainer;

import mk.game.connect4.view.Connect4View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author mk
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4Game extends Game implements IGame
{
    private static int defaultXSize = 7;
    private static int defaultYSize = 6;
    private static int defaultConnectSize = 4;
    private int connectSize;
    private int xSize;
    private int ySize;

    public Connect4Game()
    {
        super();
    }

    public Connect4Game(RootPaneContainer parent)
    {
        super(parent);
    }

    public String getName()
    {
        return "Connect4";
    }

    public GameMoveManager getGameMoveManager()
    {
        return new Connect4GameMoveManager(this);
    }

    public IGameView getGameView()
    {
        return new Connect4View(this);
    }

    public IGameView getGameView(RootPaneContainer parent)
    {
        return new Connect4View(this, parent);
    }

    public IGameState getGameState()
    {
        return new Connect4GameState(this, defaultXSize, defaultYSize, defaultConnectSize);
    }

    public GameSettings getGameSettings()
    {
        return new Connect4GameSettings();
    }

    public void init(RootPaneContainer parent)
    {
        xSize = defaultXSize;
        ySize = defaultYSize;
        connectSize = defaultConnectSize;
        super.init(parent);
    }

    public void applySettings()
    {
        super.applySettings();
    }

    public void startNewGame()
    {
        super.startNewGame();
        showGame();
    }

    public GameMove getFirstMove()
    {
        return new Connect4GameMove(xSize / 2);
    }

    public boolean isPossibleMove(int x)
    {
        return (state != null && state.isPossibleMove(new Connect4GameMove(x)));
    }

    public void showHint(IGameMove move)
    {
//		Connect4GameMove move = (Connect4GameMove) aMove ;
        if (move != null)
        {
            view.hint(move);
        }
    }

    public void showWinner()
    {
        Connect4GameState connectState = (Connect4GameState) state;
        Connect4View connect4View = (Connect4View) view;
        connect4View.outline(connectState.getConnectLine());
    }

    public int getDepth()
    {
        return super.getDepth();
    }

    /**
     * @return
     */
    public int getConnectSize()
    {
        return connectSize;
    }

    /**
     * @return
     */
    public int getXSize()
    {
        return xSize;
    }

    /**
     * @return
     */
    public int getYSize()
    {
        return ySize;
    }

    /**
     * @param i
     */
    public void setConnectSize(int i)
    {
        connectSize = i;
    }

    /**
     * @param i
     */
    public void setXSize(int i)
    {
        xSize = i;
    }

    /**
     * @param i
     */
    public void setYSize(int i)
    {
        ySize = i;
    }

    public boolean isDrawIfNoMoves()
    {
        return true;
    }

}
