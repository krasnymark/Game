/*
 * Created on Mar 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.model;

import mk.game.common.model.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4GameState extends GameState implements Cloneable
{
	private static final int base = 10 ;
	private static final int maxConnectSize = 5 ;
	private static int[] lineValues ;
	private int connectSize = 4 ;
	private int xSize = 7 ;
	private int ySize = 6 ;
	private int board[][] ;
	private int yMax = 0 ;
	private Point connectLine[] ;

	public Connect4GameState(Connect4GameState aState)
	{
		this(aState.getGame(), aState.getXSize(), aState.getYSize(), aState.getConnectSize(), aState.getBoard()) ;
	}

	public Connect4GameState(Game game, int xSize, int ySize, int connectSize, int aBoard[][])
	{
		super(game);
		this.xSize = xSize ;
		this.ySize = ySize ;
		this.connectSize = connectSize ;
		board = new int[xSize][ySize] ;
		for (int x = 0; x < xSize; x++)
		for (int y = 0; y < ySize; y++)
		{
			if (aBoard == null)
			{
				board[x][y] = 0;
			}
			else
			{
				board[x][y] = aBoard[x][y];
				if (board[x][y] > 0 && yMax < y) yMax = y ;
			}
		}
		connectLine = new Point[connectSize] ;
		setLastMove(new Connect4GameMove()) ;
		value = 0;
	}

	public Connect4GameState(Game game, int xSize, int ySize, int connectSize)
	{
		this(game, xSize, ySize, connectSize, null);
	}
	
	public Connect4GameState clone()
	{
		Connect4GameState newState = new Connect4GameState(this) ;
		return newState ;
	}

	public int getValue()
	{
		return value;
	}

	private int getValue(Point p)
	{
		return getValue(p.x, p.y) ;
	}
	private int getValue(int x, int y)
	{
//		if (outOfBounds(x, y))
//		{
//			System.out.println("getValue outOfBounds: (" + x + "," + y + ")");
//			return 0;
//		}
		return board[x][y] ;
	}

	private boolean outOfBounds(Point p)
	{
		return outOfBounds(p.x, p.y) ;
	}
	private boolean outOfBounds(int x, int y)
	{
		return (x < 0 || x >= xSize || y < 0 || y >= ySize ) ;
	}

	/**
	 * @return: Was move a winning one for the player
	 */
	public boolean isMoveWin(IGameMove aMove, int player)
	{
		Point direction = new Point(1,0) ;
		Point start = ((Connect4GameMove) aMove).asPoint() ;
		if (checkDirectionFromPoint(start, direction)) return true ;
		direction.setLocation(1, 1) ;
		if (checkDirectionFromPoint(start, direction)) return true ;
		direction.setLocation(-1, 1) ;
		if (checkDirectionFromPoint(start, direction)) return true ;
		direction.setLocation(0, 1) ;
		if (checkDirectionFromPoint(start, direction)) return true ;
		return false ;
	}

	/**
	 * @return: Was move a winning one for the player
	 */
	public boolean checkDirectionFromPoint(Point point, Point direction)
	{
		int player = getValue(point) ;
		int playerValue = 0 ;
		Point p = new Point() ;
		p.setLocation(point) ;
		while (p.x >= 0 && p.x < xSize 
		    && p.y >= 0 && p.y < ySize
		    && getValue(p) == player)
		{
			playerValue++ ;
			p.translate(direction.x, direction.y) ;
		}
		p.setLocation(point) ;
		p.translate(-direction.x, -direction.y) ;
		while (p.x >= 0 && p.x < xSize 
			 && p.y >= 0 && p.y < ySize
			 && getValue(p) == player)
		{
			playerValue++ ;
			p.translate(-direction.x, -direction.y) ;
		}
//		if (playerValue == connectSize) setWin(player) ;
		return (playerValue == connectSize) ;
	}

	public int evaluate(int player, boolean checkWin)
	{
		game.setWinner(0) ;
		int opponent = GameMoveManager.getOpponent(player) ;
		int xLast = xSize - connectSize + 1 ;
		int yLast = ySize - connectSize + 1 ;
		if (yLast > yMax + 1) yLast = yMax + 1 ;
		int xFirst = connectSize - 1 ;
		value = 0;
		Point direction = new Point(1,0) ;
		for (int x = 0 ; x < xLast ; x++)
		for (int y = 0 ; y <= yMax ; y++)
		{
			value += evaluateLine(x, y, direction, player, opponent, checkWin);
		}
		direction.setLocation(1,1);
		for (int x = 0 ; x < xLast ; x++)
		for (int y = 0 ; y < yLast ; y++)
		{
			value += evaluateLine(x, y, direction, player, opponent, checkWin);
		}
		direction.setLocation(-1,1);
		for (int x = xFirst ; x < xSize ; x++)
		for (int y = 0 ; y < yLast ; y++)
		{
			value += evaluateLine(x, y, direction, player, opponent, checkWin);
		}
		direction.setLocation(0,1);
		for (int x = 0 ; x < xSize ; x++)
		for (int y = 0 ; y < yLast ; y++)
		{
			if (getValue(x,y) == 0) break;
			value += evaluateLine(x, y, direction, player, opponent, checkWin);
		}
		return value ;
	}

		private int evaluateLine(int x, int y, Point dir, int playerOne, int playerTwo, boolean checkWin)
		{
	//		int playerTwo = GameMoveManager.getOpponent(playerOne) ;
			int intValue = 0 ;
			int playerTwoValue = 0 ;
			int playerOneValue = 0 ;
			for (int i = 0 ; i < connectSize ; i++)
			{
				int v = getValue(x + i * dir.x, y + i * dir.y) ;
				if (v == playerOne) playerOneValue++ ;
				else 
				if (v == playerTwo) playerTwoValue++ ;
				if (playerOneValue > 0 && playerTwoValue > 0) 
				{
					return 0 ;
				} 
			}
			if (playerOneValue > 0)
			{
				if (playerOneValue == connectSize && game.getWinner() != playerTwo)
				{
					playerOneValue++ ;
					if (checkWin)
					{ 
						game.setWinner(playerOne) ;
						outline(x, y, dir) ;
					}
				}
				intValue += getLineValues()[playerOneValue] ; // Math.pow(base,playerOneValue) ;
			}
			else if (playerTwoValue > 0)
			{
				if (playerTwoValue == connectSize && game.getWinner() != playerOne)
				{
					playerTwoValue++ ;
					if (checkWin)
					{ 
						game.setWinner(playerTwo) ;
						outline(x, y, dir) ;
					}
				}
				intValue -= getLineValues()[playerTwoValue] ; // Math.pow(base,playerTwoValue) ;
			}
			return intValue ;
		}

	public void outline(int x, int y, Point dir)
	{
		for (int i = 0 ; i < connectSize ; i++)
		{
			connectLine[i] = new Point(x + i * dir.x, y + i * dir.y) ;
		}
	}

	public IGameState applyMove(IGameMove aMove, int player)
	{
		Connect4GameMove move = (Connect4GameMove) aMove ;
		int x = move.getX() ;
		for (int y = 0 ; y < ySize ; y++)
		{
			if (board[x][y] == 0)
			{
				board[x][y] = player ;
				move.setY(y) ;
				setLastMove(new Connect4GameMove(x, y)) ;
				if (yMax < y) yMax = y ;
				break ;
			}
		}
		return this ;
	}

	public IGameState takeBack(IGameMove aMove, int player)
	{
		Connect4GameMove move = (Connect4GameMove) aMove ;
		int x = move.getX() ;
		for (int y = ySize-1 ; y >= 0 ; y--)
		{
			if (board[x][y] > 0)
			{
				this.takeBack(x, y) ;
				break ;
			}
		}
		return this ;
	}

	public IGameState takeBack(int x, int y)
	{
		if (!outOfBounds(x, y)) board[x][y] = 0 ;
		return this ;
	}

	public IGameState takeBack(Point p)
	{
		return this.takeBack(p.x, p.y) ;
	}

	public boolean isPossibleMove(IGameMove aMove)
	{
		Connect4GameMove move = (Connect4GameMove) aMove ;
		int x = move.getX() ;
		return (isPossibleMove(x)) ;
	}

	public boolean isPossibleMove(int x)
	{
		return (board[x][ySize-1] == 0) ;
	}

	public List getPossibleMoves(int player)
	{
		return getPossibleMoves() ;
	}

	public List getPossibleMoves()
	{
		List list = new ArrayList() ;
		for (int x = 0 ; x < xSize ; x++)
		{
			if (isPossibleMove(x)) list.add(new Connect4GameMove(x)) ;
		}
		return list ;
	}
	/**
	 * @return
	 */
	public int[][] getBoard()
	{
		return board ;
	}

	public void setBoard(int[][] aBoard)
	{
		board = aBoard ;
	}

	public String toString()
	{
		String s = "State Value = " + getValue() 
		         + " Win: " + game.getWinner() 
		         + "\nBoard\n" ;
		for (int y = ySize - 1 ; y >= 0 ; y--)
		{
			for (int x = 0 ; x < xSize ; x++)
			{
				s += " " + (board[x][y] < 0 ? 2 : board[x][y]) ;
			}
			s += "\n" ;
		}
		return s ;
	}
	/**
	 * @return
	 */
	public int getConnectSize()
	{
		return connectSize ;
	}

	public void setConnectSize(int n)
	{
		connectSize = n ;
	}

	/**
	 * @return
	 */
	public Point[] getConnectLine()
	{
		return connectLine ;
	}

	/**
	 * @return
	 */
	public int getXSize()
	{
		return xSize ;
	}

	/**
	 * @return
	 */
	public int getYSize()
	{
		return ySize ;
	}

	/**
	 * @param i
	 */
	public void setXSize(int i)
	{
		xSize = i ;
	}

	/**
	 * @param i
	 */
	public void setYSize(int i)
	{
		ySize = i ;
	}

	/**
	 * @return
	 */
	public static int[] getLineValues()
	{
		if (lineValues == null)
		{
			lineValues = new int[maxConnectSize + 2] ;
			for (int i = 0 ; i < lineValues.length ; i++)
			{
				lineValues[i] = (new Double(Math.pow(base,i))).intValue() ;
			}
		}
		return lineValues ;
	}

	public void init()
	{
		// TODO Auto-generated method stub
		for (int x = 0; x < xSize; x++)
		for (int y = 0; y < ySize; y++)
		{
			board[x][y] = 0;
		}
	}
}
