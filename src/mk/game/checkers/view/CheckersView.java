/*
 * Created on Mar 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.view;

import mk.game.common.model.Game;
import mk.game.common.model.IGame;
import mk.game.common.model.IGameMove;
import mk.game.common.model.IGameState;
import mk.game.common.util.Delay;
import mk.game.common.view.GameMenu;
import mk.game.common.view.GameView;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import mk.game.checkers.model.CheckersGame;
import mk.game.checkers.model.CheckersGameCell;
import mk.game.checkers.model.CheckersGameMove;
import mk.game.checkers.model.CheckersGameState;
import mk.game.common.view.IGameView;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersView extends GameView implements IGameView, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int cellSize = 64;
//	private static final Color myBackgroundColor = Color.lightGray ;
	private int size;
	private CheckersViewCell[][] cells ;
	private CheckersGameMove move = new CheckersGameMove() ;

	public CheckersView(int size, CheckersGame game, RootPaneContainer container)
	{
		super(game, container) ;
		this.size = size;
		initGame() ;
	}

	public CheckersView(int size, CheckersGame game)
	{
		this(size, game, null);
	}

	public void initGame()
	{
		cells = new CheckersViewCell[size][size] ;
		super.initGame() ;
		Color[] playerColors = GameView.getPlayerColors() ;
		playerColors[2] = Color.blue ;
		GameView.setPlayerColors(playerColors) ;
	}
	
	public GameMenu getGameMenu(Game game)
	{
		return (GameMenu) (new CheckersMenu((CheckersGame) game)) ;
	}
	public JPanel getGamePanel()
	{
		JPanel gPanel = new JPanel(); // Game Panel
		int size = getState().getSize() ;
		int boardSize = cellSize * size ;
		gPanel.setLayout(new GridLayout(size, size)) ;
		gPanel.setPreferredSize(new Dimension(boardSize, boardSize));
		MouseListener ml = new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				if (game.isGameOver()) return ;
				CheckersGameState state = getState() ;
				CheckersViewCell cell = (CheckersViewCell) e.getSource() ;
				Point p = getPoint(cell) ;
				if (p == null) return ;
				if (isPieceSelected()) // After 1st click
				{
					if (state.isPossibleMove(move.getFromPoint(), p))
					{
						cell.hilight() ;
					}
				}
				else
				{
					if (state.getPlayer(p) == game.getWhoMovesNext()
					&&  state.hasPossibleMove(p))
					{
						cell.hilight() ;
					}
				}
			}

			public void mouseExited(MouseEvent e)
			{
				if (game.isGameOver()) return ;
				CheckersViewCell cell = (CheckersViewCell) e.getSource();
				cell.deHilight() ;
			}
		};
		// Board
		CheckersGameCell[][] board = getState().getModel() ;
		for (int y = size - 1; y >= 0; y--)
		for (int x = 0; x < size; x++)
		{
			if (CheckersGameState.isEven(x + y))
			{
				CheckersViewCell cell = new CheckersViewCell(board[x][y]);
				cell.addActionListener(this) ;
				cell.addMouseListener(ml) ;
				gPanel.add(cell);
				cells[x][y] = cell;
			}
			else
			{
				Canvas canvas = new Canvas() ;
				canvas.setBackground(Color.white) ;
				gPanel.add(canvas) ;
			}
		}
		return gPanel ;
	}

    @Override
    public GameMenu getGameMenu(IGame game)
    {
        return (GameMenu) (new CheckersMenu((CheckersGame) game)) ;
    }

    /* (non-Javadoc)
      * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
      */
	public void actionPerformed(ActionEvent e)
	{
		if (game.isGameOver()) return ;
		CheckersViewCell cell = (CheckersViewCell) e.getSource() ;
		Point p = getPoint(cell) ;
		if (!isPieceSelected()) // 1st click
		{
			if (getState().getPlayer(p) == game.getWhoMovesNext())
			{
				move.setPiece(getState().getPiece(p)) ;
				move.setFromPoint(p) ;
			}
		}
		else // 2nd click
		{
			move.setToPoint(p) ;
			CheckersGameMove aMove = getState().getPossibleMove(move.getFromPoint(), p) ;
			if (aMove != null)
			{
				game.makeMove(aMove) ;
			}
			else
			{
				// TODO - say it's invalid.
			}
			move.clear() ;
		}
	}
	public CheckersGameState getState()
	{
		return (CheckersGameState) game.getState() ;
	}

	private boolean isPieceSelected()
	{
		return move.getPiece() != null ;
	}

	public void showState(IGameState aState)
	{
		getState().updateModel() ;
		int size = getState().getSize() ;
		for (int y = size - 1; y >= 0; y--)
		for (int x = 0; x < size; x++)
		{
			if (CheckersGameState.isEven(x + y)) cells[x][y].repaint() ;
		}
        super.showState(aState);
	}

	public void applyMove(IGameMove aGameMove, int player)
	{
//		TODO
//		showState(getState()) ;
		CheckersGameMove aMove = (CheckersGameMove) aGameMove ;
		List points = aMove.getAffectedPoints() ;
		getState().updateModel(points) ;
		for (Iterator i = points.iterator(); i.hasNext();)
		{
			Point p = (Point) i.next() ;
			cells[p.x][p.y].repaint() ;
		}
		moveListTableUpdate() ;
	}

	public void takeBack(IGameMove aMove)
	{
		applyMove(aMove, 0);
	}

	public void hint(IGameMove aGameMove)
	{
		CheckersGameMove aMove = (CheckersGameMove) aGameMove ;
		Point p = aMove.getFromPoint() ;
		CheckersViewCell cell = cells[p.x][p.y] ;
		for (int i = 1; i < 3; i++)
		{
			Delay.delay(250) ;
			cell.hilight() ;
			Delay.delay(250) ;
			cell.deHilight() ;
		}
}
	
	private Point getPoint(CheckersViewCell cell)
	{
		for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
		{
			if (cells[x][y] == cell)
			{
				return new Point(x, y) ;
			}
		}
		return null ;
	}

	public void outline(Point p)
	{
	}

	public void outline(Point[] line)
	{
	}

	public static int getCellSize()
	{
		return cellSize;
	}
}
