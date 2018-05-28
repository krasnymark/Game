/*
 * Created on Mar 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.view;

import mk.game.common.model.*;
import mk.game.common.view.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import mk.game.connect4.model.Connect4Game;
import mk.game.connect4.model.Connect4GameMove;
import mk.game.connect4.model.Connect4GameState;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4View extends GameView implements IGameView, ActionListener
{
	private static final int xDefault = 7;
	private static final int yDefault = 6;
	private static final int cellSize = 64;
	private static final Color myBackgroundColor = Color.lightGray;
	private int xSize;
	private int ySize;
	private Connect4Button[] buttons;
	private Connect4Cell[][] cells;
    // Connect4 doesn't repaint itself !!! ???
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public Connect4View(int xSize, int ySize, Connect4Game game, RootPaneContainer container)
	{
		super(game, container);
		this.xSize = xSize;
		this.ySize = ySize;
		initGame();
	}

	public Connect4View(int xSize, int ySize, Connect4Game game)
	{
		this(xSize, ySize, game, null);
	}

	public Connect4View(Connect4Game game, RootPaneContainer container)
	{
		this(game.getXSize(), game.getYSize(), game, container);
	}

	public Connect4View(Connect4Game game)
	{
		this(game.getXSize(), game.getYSize(), game);
	}

	public void initGame()
	{
		cells = new Connect4Cell[xSize][ySize];
		buttons = new Connect4Button[xSize];
		super.initGame();
	}
	
	public GameMenu getGameMenu(IGame game)
	{
		return (GameMenu) (new Connect4Menu((Connect4Game) game));
	}

	public JPanel getGamePanel()
	{
		JPanel gPanel = new JPanel(); // Game Panel

		gPanel.setLayout(new GridLayout(ySize + 1, xSize));
		int boardXSize = cellSize * xSize;
		int boardYSize = cellSize * ySize * 7 / 6;
		gPanel.setPreferredSize(new Dimension(boardXSize, boardYSize));
		MouseListener ml = new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				Connect4Button b = (Connect4Button) e.getSource();
				Connect4Game connect4 = (Connect4Game) game;
				if (connect4.isPossibleMove(getX(b)))
				{
					b.setPlayerColor(Connect4Cell.getFreeColor());
					b.repaint();
				}
			}

			public void mouseExited(MouseEvent e)
			{
				Connect4Button b = (Connect4Button) e.getSource();
				b.setPlayerColor(b.getBlankColor());
				b.repaint();
			}
		};
		// Game Move Buttons
		for (int x = 0; x < xSize; x++)
		{
			Connect4Button button = new Connect4Button(x);
			button.addActionListener(this);
			button.addMouseListener(ml);
			gPanel.add(button);
			buttons[x] = button;
		}
		// Board
		for (int y = ySize - 1; y >= 0; y--)
		for (int x = 0; x < xSize; x++)
		{
			Connect4Cell cell = new Connect4Cell();
			gPanel.add(cell);
			cells[x][y] = cell;
		}
		return gPanel;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		Connect4Button button = (Connect4Button) e.getSource();
		Connect4Game connect4 = (Connect4Game) game;
		if (connect4.isPossibleMove(getX(button)))
		{
			game.makeMove(new Connect4GameMove(button.getX()));
		}
	}
	public void showState(IGameState aState)
	{
		Connect4GameState state = (Connect4GameState) aState;
        for (int x = 0; x < xSize; x++)
        {
            buttons[x].repaint();
        }
		int[][] board = state.getBoard();
		for (int y = 0; y < ySize; y++)
		for (int x = 0; x < xSize; x++)
		{
			Connect4Cell cell = cells[x][y];
			cell.setPlayer(board[x][y]);
			if (!game.isGameOver()) cell.setOutline(false);
			cell.repaint();
		}
		super.showState(state);
	}

    @Override
    public void showGame()
    {
        super.showGame();
        scheduler.schedule(new Runnable()
        {
            public void run()
            {
                showState(game.getState());
            }
        }, 50L, TimeUnit.MILLISECONDS);
    }

    private int getX(Connect4Button button)
	{
        return button.getX();
	}

	public void applyMove(IGameMove aMove, int player)
	{
		Connect4GameMove move = (Connect4GameMove) aMove;
		Connect4Cell cell = cells[move.getX()][move.getY()];
		cell.setPlayer(player);
		cell.repaint();
		moveListTableUpdate();
	}

	public void takeBack(IGameMove aMove)
	{
		Connect4GameMove move = (Connect4GameMove) aMove;
		applyMove(move, 0);
	}

	public void hint(IGameMove aMove)
	{
		Connect4GameMove move = (Connect4GameMove) aMove;
		Connect4Button button = buttons[move.getX()];
//		TODO DON'T HARDCODE
		try
		{
			for (int i = 0; i < 2; i++)
			{
				button.setOutlineColor(Color.white);
				button.repaint();
				Thread.sleep(100);
				button.setOutlineColor(Color.black);
				button.repaint();
				Thread.sleep(100);
			}
		}
		catch (InterruptedException e)
		{
		}
	}

	public void outline(Point[] line)
	{
		for (int i = 0; i < line.length; i++)
		{
			Connect4Cell cell = cells[line[i].x][line[i].y];
			if (cell == null)
				return;
			cell.setOutline(true);
			cell.repaint();
		}

	}
}
