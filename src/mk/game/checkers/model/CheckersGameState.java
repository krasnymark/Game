/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author mk To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGameState extends GameState implements Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Point[] boardPoints ;
	private int squareSize ;
	private int size ;
	private CheckersGameCell[][] board ;
	private CheckersGameCell[][] model ;
	private int[] playerValue = new int[3] ;
	private int[][] playerPieceCount = new int[3][2] ; // index: player [1/2], piece [0/1]
	private boolean isEvaluate = true ;
	private boolean isRussian = true ; // TODO -> Settings

	public CheckersGameState(CheckersGameState aState)
	{
		this(aState.getGame(), aState.getSquareSize(), true) ; // Clone
	}

	public CheckersGameState(Game game)
	{
		this(game, CheckersGameSettings.SQUARE_SIZE_100) ; // Default.
	}

	/**
	 * @param square
	 */
	public CheckersGameState(Game game, int squareSize)
	{
		this(game, squareSize, false) ;
		init() ;
	}

	/**
	 * @param square
	 */
	private CheckersGameState(Game game, int squareSize, boolean noInit)
	{
		super(game);
		this.squareSize = squareSize ;
		if (!isValidSize(squareSize))
		{
			isValidSize(CheckersGameSettings.SQUARE_SIZE_64) ; // Default.
		}
		board = new CheckersGameCell[size][size] ;
		model = new CheckersGameCell[size][size] ;
	}
	
	public CheckersGameState clone()
	{
		CheckersGameState newState = new CheckersGameState(this) ;
		newState.init(this) ;
		return newState ;
	}
	
	public void init(CheckersGameState aState)
	{
		for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
		{
			if (isEven(x + y))
			{
				CheckersGamePiece piece = aState.getBoard()[x][y].getPiece() ;
				if (piece != null) piece = new CheckersGamePiece(piece) ;
				board[x][y] = new CheckersGameCell(CheckersGameCell.CHECKERS_CELL_BLACK, piece) ;
				model[x][y] = new CheckersGameCell(CheckersGameCell.CHECKERS_CELL_BLACK) ;
			}
		}
		for (int player = 1 ; player <= 2 ; player++)
		{
			playerValue[player] = aState.getPlayerValue()[player] ;
			playerPieceCount[player][0] = aState.getPlayerPieceCount()[player][0] ;
			playerPieceCount[player][1] = aState.getPlayerPieceCount()[player][1] ;
		}
		updateModel() ;
	}

//	private int getBoardSquareSize(Game aGame)
//	{
//		CheckersGame mk.game = (CheckersGame) aGame ;
//		CheckersGameSettings gameSettings = (CheckersGameSettings) mk.game.getSettings() ;
//		return gameSettings.getBoardSize() ;
//	}

	public static boolean isEven(int n)
	{
		return (n % 2 == 0) ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#applyMove(mk.game.common.model.GameMove, int)
	 */
	private boolean isValidSize(int squareSize)
	{
		size = new Double(Math.sqrt(squareSize)).intValue() ;
		return (size * size == squareSize && isEven(size)) ;
	}

	public boolean isValidPoint(int x, int y)
	{
		if (0 <= x && x < size
		&&  0 <= y && y < size) return true ;
		return false ;
	}

	public boolean isValidPoint(Point p)
	{
		return isValidPoint(p.x, p.y) ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#applyMove(mk.game.common.model.GameMove, int)
	 */
	public IGameState applyMove(IGameMove move, int player)
	{
		// Delegate to a specific move.
		if (move != null) ((CheckersGameMove) move).apply(this, player) ;
		return this ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#applyMove(mk.game.common.model.GameMove, int)
	 */
	public void applyMove(CheckersGameMove move, int player)
	{
		CheckersGamePiece piece = move.getPiece() ;
		decrPlayerValue(player, piece, move.getFromPoint()) ;
		setPiece(move.getToPoint(), piece) ;
		clearCell(move.getFromPoint()) ;
		if (isPawnToQueenMove(move))
		{
			transformPawnToQueen(move) ;
		}
		incrPlayerValue(player, piece, move.getToPoint()) ;
	}

	public void applyMove(CheckersGameTake take, int player)
	{
		// Kill the opponent
		killPiece(take.getOpponentsPoint()) ;
		// ReEvaluate
		decrPlayerValue(GameMoveManager.getOpponent(player), take.getOpponentsPiece(), take.getOpponentsPoint()) ;
		// Apply common rule
		CheckersGameMove move = (CheckersGameMove) take ;
		applyMove(move, player) ;
		// Continue
		if (take.getNextLeg() != null) applyMove(take.getNextLeg(), player) ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#takeBack(mk.game.common.model.GameMove)
	 */
	public IGameState takeBack(IGameMove move, int player)
	{
		// Delegate to a specific move.
		((CheckersGameMove) move).takeBack(this, player) ;
		return this ;
	}

	public void takeBack(CheckersGameMove move, int player)
	{
		CheckersGamePiece piece = move.getPiece() ;
		decrPlayerValue(player, piece, move.getToPoint()) ;
		if (wasPawnToQueenMove(move))
		{
			transformQueenToPawn(move) ;
		}
		clearCell(move.getToPoint()) ;
		setPiece(move.getFromPoint(), piece) ;
		incrPlayerValue(player, piece, move.getFromPoint()) ;
	}

	public void takeBack(CheckersGameTake take, int player)
	{
		// Reverse
		Stack stack = take.reverse() ;
		CheckersGameTake next ;
		while (!stack.isEmpty() && (next = (CheckersGameTake) stack.pop()) != null)
		{
			CheckersGameMove move = (CheckersGameMove) next ;
			takeBack(move, player) ;
			CheckersGamePiece opponentsPiece = next.getOpponentsPiece() ;
			Point opponentsPoint = next.getOpponentsPoint() ;
			revivePiece(opponentsPoint, opponentsPiece) ;
			// ReEvaluate
			incrPlayerValue(GameMoveManager.getOpponent(player), opponentsPiece, opponentsPoint) ;
		}
	}
	
	public boolean wasPawnToQueenMove(CheckersGameMove move)
	{
		return move.isPawnToQueenMove() ;
	}
	
	public boolean isQueenRow(int y, int player)
	{
		return ((y == 0 && player == 2) || (y == size-1 && player == 1)) ;
	}
		
	public boolean isPawnToQueenMove(CheckersGameMove move)
	{
		// In Russian mk.game.checkers pawn always turns into Queen and continue jumping as such.
		if (isRussian || isEvaluate)
		{
			int player = move.getPiece().getPlayer() ;
			Point endPoint = isRussian ? move.getToPoint() : move.getEndPoint() ;
			if (move.getPiece().isPawn() && isQueenRow(endPoint.y, player))
			{
				move.setPawnToQueenMove(true) ;
//				mk.game.print("isPawnToQueenMove: " + move) ;
			}
		}
		return move.isPawnToQueenMove() ;
	}

	public void transformPawnToQueen(CheckersGameMove move)
	{
		move.getPiece().setPawn(false) ;
//		mk.game.print(move.getPiece() + " - transformPawnToQueen: " + move) ;
	}
	
	public void transformQueenToPawn(CheckersGameMove move) // For take back
	{
		move.getPiece().setPawn(true) ;
//		mk.game.print(move.getPiece() + " - transformQueenToPawn: " + move) ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#isPossibleMove(mk.game.common.model.GameMove)
	 */
	public boolean isPossibleMove(IGameMove aGameMove)
	{
		CheckersGameMove aMove = (CheckersGameMove) aGameMove;
		int player = aMove.getPiece().getPlayer() ;
		for (Iterator i = getPossibleMovesFromCache(player).iterator(); i.hasNext();)
		{
			CheckersGameMove pMove = (CheckersGameMove) i.next();
			if (aMove.equals(pMove)) return true;
		}
		return false;
	}

	public boolean hasPossibleMove(Point point)
	{
		for (Iterator i = getPossibleMovesFromCache().iterator(); i.hasNext();)
		{
			CheckersGameMove move = (CheckersGameMove) i.next();
			if (move.getFromPoint().equals(point)) return true;
		}
		return false;
	}

	public boolean isPossibleMove(Point fromPoint, Point endPoint)
	{
		return getPossibleMove(fromPoint, endPoint) != null ;
	}

	public CheckersGameMove getPossibleMove(Point fromPoint, Point endPoint)
	{
		for (Iterator i = getPossibleMovesFromCache().iterator(); i.hasNext();)
		{
			CheckersGameMove aMove = (CheckersGameMove) i.next();
			if (aMove.getFromPoint().equals(fromPoint) && aMove.getEndPoint().equals(endPoint)) return aMove;
		}
		return null ;
	}

	public List <IGameMove> getPossibleMoves(int player)
	{
		// TODO
		setEvaluate(false) ;
		List <IGameMove> possibleMoves = new ArrayList <IGameMove> () ;
		List <IGameMove> possibleTakes = new ArrayList <IGameMove> () ;
		for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
		{
			if (isEven(x + y) && board[x][y].getPlayer() == player)
			{
				CheckersGamePiece piece = board[x][y].getPiece() ;
				Point point = new Point(x, y) ;
				getPossibleMoves(piece, point, possibleMoves, possibleTakes) ;
			}
		}
//		if (possibleMoves.isEmpty() && possibleTakes.isEmpty()) setWinner(GameMoveManager.getOpponent(player)) ;
		setEvaluate(true) ;
		return (possibleTakes.isEmpty()) ? possibleMoves : possibleTakes ;
	}

	public void getPossibleMoves(CheckersGamePiece piece, Point start, List <IGameMove> possibleMoves, List <IGameMove> possibleTakes)
	{
		Point[] directions = CheckersGameMoveManager.getDirections() ;
		for (int i = 0; i < directions.length; i++)
		{
			getPossibleTakesInDirection(piece, start, directions[i], possibleTakes) ;
			if (possibleTakes.isEmpty() && !piece.isDirectionForTakeOnly(directions[i]))
			getPossibleMovesInDirection(piece, start, directions[i], possibleMoves) ;
		}
	}

	public void getPossibleMovesInDirection(CheckersGamePiece piece, Point start, Point dir, List <IGameMove> possibleMoves)
	{
		Point p = new Point(start) ;
		for (int i = 0; i < piece.getRange(); i++)
		{
			p.translate(dir.x, dir.y) ;
			if (isValidPoint(p) && getPlayer(p) == 0)
			{
				possibleMoves.add(new CheckersGameMove(piece, start, new Point(p))) ; 
			}
			else break ;
		}
	}

	public void getPossibleTakesInDirection(CheckersGamePiece piece, Point start, Point dir, List <IGameMove> possibleTakes)
	{
		Point point = findOpponentInDirection(piece, start, dir) ;
		if (point != null)
		{
			List <IGameMove> moves = new ArrayList <IGameMove> () ;
			getPossibleMovesInDirection(piece, point, dir, moves) ;
			List <IGameMove> possibleMovesInDir = new ArrayList <IGameMove> () ;
			List <IGameMove> possibleTakesInDir = new ArrayList <IGameMove> () ;
			boolean wasNextLeg = false ;
			for (Iterator i = moves.iterator(); i.hasNext();)
			{
				CheckersGameMove move = (CheckersGameMove) i.next() ;
				CheckersGameTake take = new CheckersGameTake(piece, start, move.getToPoint(), getPiece(point), point) ;
				// Have to apply not to take the same piece twice and not to bump into itself.
				applyMove(take, piece.getPlayer()) ;
				Point[] directions = CheckersGameMoveManager.getDirectionsAfter(dir) ;
				boolean isNextLeg = false ;
				for (int j = 0; j < directions.length; j++)
				{
					List <IGameMove> nextLegs = new ArrayList <IGameMove> () ;
					getPossibleTakesInDirection(piece, move.getToPoint(), directions[j], nextLegs) ;
					for (Iterator k = nextLegs.iterator(); k.hasNext();)
					{
						wasNextLeg = true ;
						isNextLeg = true ;
						CheckersGameTake nextLeg = (CheckersGameTake) k.next() ;
						CheckersGameTake withLeg = new CheckersGameTake(take) ;
						withLeg.setNextLeg(nextLeg) ;
						possibleTakesInDir.add(withLeg) ;
					}
				}
				if (!isNextLeg) possibleMovesInDir.add(take) ;
				// Have to restore the state - it was not a move yet, just a possible one.
				takeBack(take, piece.getPlayer()) ;
			}
			if (wasNextLeg)
			{
				possibleTakes.addAll(possibleTakesInDir) ;
			}
			else
			{
				possibleTakes.addAll(possibleMovesInDir) ;
			}
		}
	}
	
	public Point findOpponentInDirection(CheckersGamePiece piece, Point start, Point dir)
	{
		Point p = new Point(start) ;
		int opponent = GameMoveManager.getOpponent(piece.getPlayer()) ;
		for (int i = 0; i < piece.getRange(); i++)
		{
			p.translate(dir.x, dir.y) ;
			if (getPlayer(p) == piece.getPlayer() || !isValidPoint(p)) break ;
			if (getPlayer(p) == opponent) 
			{
				return p ; 
			}
		}
		return null ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mk.game.common.model.GameState#evaluate(int)
	 */
	public int evaluate(int player, boolean checkWin)
	{
		int opponent = GameMoveManager.getOpponent(player);
//		value = playerValue[player] - playerValue[opponent];
		playerValue[1] = playerValue[2] = 0 ;
		for (int i = 0; i < getBoardPoints().length; i++)
		{
			Point point = boardPoints[i] ;
			CheckersGamePiece piece = getPiece(point) ;
			if (piece != null)
			{
				int aValue = piece.getValue(this, point) ;
				int piecePlayer = piece.getPlayer() ;
				playerValue[piecePlayer] += aValue ;
			}
		}
//		if (value != playerValue[player] - playerValue[opponent])
//		{
//			mk.game.print("player: " + player + " value: " + value + " playerValue: " + playerValue[player] + ", " + playerValue[opponent]) ;
//		}
		value = playerValue[player] - playerValue[opponent];
		if (playerPieceCount[player][1] == 1) value += CheckersGamePiece.SINGLE_QUEEN_BONUS;
		if (playerPieceCount[opponent][1] == 1) value -= CheckersGamePiece.SINGLE_QUEEN_BONUS;
		if (getPlayerPieceCount(player) == 0 || getPlayerPieceCount(opponent) == 0)
		{
			value *= 100; // win is by far the best value.
//			mk.game.print("player: " + player + " value: " + value) ;
		}
		if (checkWin)
		{
			if (getPlayerPieceCount(opponent) == 0 
					|| getPossibleMoves(opponent).isEmpty()) game.setWinner(player);
			if (getPlayerPieceCount(player) == 0 
					|| getPossibleMoves(player).isEmpty()) game.setWinner(opponent);
		}
//		if (Math.abs(value) > 1000)
//		{
//			mk.game.print("player: " + player + " value: " + value) ;
//		}

		return value;
	}

    public boolean isMoveWin(IGameMove aMove, int player)
	{
		int opponent = GameMoveManager.getOpponent(player) ;
		return getPlayerPieceCount(opponent) == 0 ;
	}

	public String toString()
	{
		String s = "State Value = " + getValue() + " Win: " + game.getWinner() + "\nBoard\n" ;
		for (int y = size - 1; y >= 0; y--)
		{
			for (int x = 0; x < size; x++)
			{
				s += " " + ((isEven(x + y)) ? board[x][y].toString() : "  ") ;
			}
			s += "\n" ;
		}
		return s ;
	}

	public CheckersGameCell[][] getBoard()
	{
		return board ;
	}

	public void setBoard(CheckersGameCell[][] board)
	{
		this.board = board ;
	}

	public int getSize()
	{
		return size ;
	}

	public void setSize(int size)
	{
		this.size = size ;
	}

	public int getPlayer(int x, int y)
	{
		if (isValidPoint(x, y)) return getBoard()[x][y].getPlayer() ;
		else return 0 ;
	}

	public int getPlayer(Point p)
	{
		return getPlayer(p.x, p.y) ;
	}

	public CheckersGamePiece getPiece(int x, int y)
	{
		if (isValidPoint(x, y)) return getBoard()[x][y].getPiece() ;
		else return null ;
	}

	public CheckersGamePiece getPiece(Point p)
	{
		return getPiece(p.x, p.y) ;
	}

	public void setPiece(Point p, CheckersGamePiece piece)
	{
		if (isValidPoint(p))
		{
			// Update state.
			getBoard()[p.x][p.y].setPiece(piece) ;
		}
	}

	public void setPlayer(Point p, int player)
	{
		if (isValidPoint(p)) getBoard()[p.x][p.y].setPlayer(player) ;
	}

	public void clearCell(Point p)
	{
		setPiece(p, null) ;
		setPlayer(p, 0) ;
	}

	public synchronized void killPiece(Point point)
	{
		clearCell(point) ;
	}

	public synchronized void revivePiece(Point point, CheckersGamePiece piece)
	{
		setPiece(point, piece) ;
//		setPlayer(piece.getPoint(), piece.getPlayer()) ; // piece knows its player
	}

	public void init()
	{
		super.init() ;
		initPlayerValue() ;
		// TODO Auto-generated method stub
		for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
		{
			int type = (isEven(x + y)) ? CheckersGameCell.CHECKERS_CELL_BLACK
				                        : CheckersGameCell.CHECKERS_CELL_WHITE ;
			if (type == CheckersGameCell.CHECKERS_CELL_BLACK)
			{
				if (board[x][y] == null) board[x][y] = new CheckersGameCell(type) ;
				if (model[x][y] == null) model[x][y] = new CheckersGameCell(type) ;
				Point point = new Point(x, y) ;
				clearCell(point) ;
				if (y < size/2-1 || y > size/2)
				{
					int player = (y < size / 2) ? GameMoveManager.PLAYER_ONE
						                         : GameMoveManager.PLAYER_TWO ;
					CheckersGamePiece piece = new CheckersGamePiece(player) ;
					setPiece(point, piece) ;
//					playerValue[player] += piece.getValue() ;
					playerPieceCount[player][0]++ ;
				}
			}
		}
		updateModel() ;
	}
	
	public void updateModel()
	{
		for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
		{
			if (isEven(x + y))
			{
				int player = board[x][y].getPlayer() ;
				CheckersGamePiece piece = board[x][y].getPiece() ;
				boolean isPawn = (piece == null) ? true : piece.isPawn() ;
				model[x][y].setPiece(player, isPawn) ;
			}
		}
	}
	
	public void updateModel(List points)
	{
		for (Iterator i = points.iterator(); i.hasNext();)
		{
			Point p = (Point) i.next() ;
			int player = board[p.x][p.y].getPlayer() ;
			CheckersGamePiece piece = board[p.x][p.y].getPiece() ;
			boolean isPawn = (piece == null) ? true : piece.isPawn() ;
			model[p.x][p.y].setPiece(player, isPawn) ;
		}
	}

	public CheckersGameCell[][] getModel()
	{
		return model;
	}

	public void setModel(CheckersGameCell[][] model)
	{
		this.model = model;
	}

	public void initPlayerValue()
	{
		playerValue[1] = playerValue[2] = 0 ;
		playerPieceCount[1][0] = playerPieceCount[1][1] = 0 ;
		playerPieceCount[2][0] = playerPieceCount[2][1] = 0 ;
	}

	public void incrPlayerValue(int player, CheckersGamePiece piece, Point point)
	{
		if (isEvaluate)
		{
//			playerValue[player] += piece.getValue(this, point) ;
			playerPieceCount[player][pieceIndex(piece)]++ ;
		}
	}

	public void decrPlayerValue(int player, CheckersGamePiece piece, Point point)
	{
		if (isEvaluate)
		{
//			playerValue[player] -= piece.getValue(this, point) ;
			playerPieceCount[player][pieceIndex(piece)]-- ;
		}
	}

	public int pieceIndex(CheckersGamePiece piece)
	{
		return piece.isPawn() ? 0 : 1 ;
	}

	public boolean isEvaluate()
	{
		return isEvaluate;
	}

	public void setEvaluate(boolean isEvaluate)
	{
		this.isEvaluate = isEvaluate;
	}
	
	public int getPieceValue(int player, CheckersGamePiece piece, Point point)
	{
		int pieceValue = piece.getValue() ;
		if (piece.isPawn())
		{
			int mid = size / 2 ;
			int end = size - 1 ;
			// Bonus for controlling center
			int x = (point.x < mid ? point.x : end - point.x) ;
			pieceValue += 2 * x ;
			// Bonus for moving forward
			int y = (player == 1) ? point.y : end - point.y ;
			pieceValue += 2 * y ;
		}
		return pieceValue ;
	}

	public int[][] getPlayerPieceCount()
	{
		return playerPieceCount;
	}

	public int getPlayerPieceCount(int player)
	{
		return playerPieceCount[player][0] + playerPieceCount[player][1] ;
	}

	public void setPlayerPieceCount(int[][] playerPieceCount)
	{
		this.playerPieceCount = playerPieceCount;
	}

	public int[] getPlayerValue()
	{
		return playerValue;
	}

	public void setPlayerValue(int[] playerValue)
	{
		this.playerValue = playerValue;
	}

	public int getSquareSize()
	{
		return squareSize;
	}

	public Point[] getBoardPoints()
	{
		if (boardPoints == null)
		{
			boardPoints = new Point[squareSize / 2] ;
			int k = 0 ;
			for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
			{
				if (isEven(x + y))
				{
					boardPoints[k++] = new Point(x, y) ;
				}
			}
		}
		return boardPoints;
	}
}