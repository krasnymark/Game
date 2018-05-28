package mk.game.common.model;

import mk.game.common.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;


/**
 * GameMoveManager
 *
 * GameMoveManager provides the services needed to decide the computer
 * player's next move.  It's most important method is getBestMove,
 * which by default calls MinMax.getBestMove.
 */
public abstract class GameMoveManager
{
	public static final int PLAYER_ONE = 1;
	public static final int PLAYER_TWO = 2;
	private static final int MAX_VALUE = 1000000; // make sure a GameState value cannot be higher
	private static final int PROCESSOR_COUNT = 1;
	private IGame game;
	private List <IGameMove> possibleMovesCache = new ArrayList <IGameMove>();
	private boolean isPossibleMovesCacheReady = false;
	// minMax - implements MinMax.getBestMove
	private MinMax minMax = null;
	// bestResultSoFar - used to optimize MinMax. Shared by moveProcessorPool threads
	private int bestResultSoFar;
	// Game is the only Listener for Move Events
	private EventListenerList moveListenerList = new EventListenerList();
	// noMove - Special Move event indicating end of the mk.game.
	private MoveEvent noMove = new MoveEvent(new GameMove());
	// moveProcessorPool - Holds threads for moveProcessor to run MinMax
	private ThreadPool moveProcessorPool = new ThreadPool(PROCESSOR_COUNT);
	// moveProcessor - Iterates over player's moves to find the best
	private MoveProcessor moveProcessor = new MoveProcessor();
	// thinkAheadProcessor - Iterates over opponent's moves to prepare responses
	private ThinkAheadProcessor thinkAheadProcessor = new ThinkAheadProcessor();
	// moveProcessorReady - Synchronizes moveProcessor with thinkAheadProcessor.
	private Semaphore moveProcessorReady = new Semaphore(0, "GameMoveManager.moveProcessorReady", true);
	// moveProcessorPause - Waits while mk.game is paused.
	private Semaphore moveProcessorPause = new Semaphore(0, "GameMoveManager.moveProcessorPause");
	// mutex - Protects access to thinkAheadMap and bestResultSoFar.
	private Semaphore mutex = new Semaphore(1, "GameMoveManager.mutex");
	// isWorking - Indicates that manager is busy working on the best move
	private volatile boolean isWorking;
	// isMoveNow - Signals one of moveProcessors to wrap up [come up with something] immediately.
	private volatile boolean isMoveNow;
	// isRequestedToStop - Signals thinkAheadProcessor to stop
	private volatile boolean isRequestedToStop;
//	private volatile boolean isWaiting;
	private Comparator <IGameMove> moveComparator = new Comparator <IGameMove>()
	{
		public int compare(IGameMove move1, IGameMove move2)
		{
			return move1.getValue() - move2.getValue();
		}
	};

	public GameMoveManager(IGame game)
	{
		this(MAX_VALUE, game);
	}

	public GameMoveManager(int maxValue, IGame game)
	{
		this.game = game;
		minMax = new MinMax(this, maxValue);
		noMove.setSource(null);
	}
	
	/**
	 * @author mark
	 * Apr 20, 2007
	 * ThinkAheadProcessor thread runs findBestMove 
	 * for each of the possible moves of the opponent [on opponent's time].
	 * It should never run findBestMove concurrently with MoveProcessor.
	 */
	public class ThinkAheadProcessor extends MoveProcessor implements Runnable
	{
		private Map<IGameMove, IGameMove> thinkAheadMap = new HashMap<IGameMove, IGameMove>();

		public ThinkAheadProcessor()
		{
			super();
			moveProcessor.setName("ThinkAhead");
			moveProcessor.setPriority(moveProcessor.getPriority() - 1);
			moveProcessorStart.setName("ThinkAhead.start");
		}

		public void runOnce()
		{
			mutex._wait_();
			thinkAheadMap.clear();
			mutex.signal();
			setRequestedToStop(false);
			List <IGameMove> possibleMoves = getPossibleMovesFromCache(state, player);
			// Use Human Player depth to presort possibleMoves by value starting from the best.
			game.print("ThinkAhead Sorting...");
			// TODO - Beginner is fast - no need to interrupt, but may change if we adjust player level.
			possibleMoves = findBestMoves(state, game.getDepth(player), player, possibleMoves);
			int opponent = getOpponent(player);
			for (IGameMove testMove : possibleMoves)
			{
				if (isRequestedToStop())
				{
					break;
				}
				// Tell them we are working on this one but not done yet
				mutex._wait_();
				thinkAheadMap.put(testMove, null);
				mutex.signal();
				game.printU("ThinkAhead Testing: " + testMove);
				state.applyMove(testMove, player);
				IGameMove bestMove = findBestMove(state, game.getDepth(opponent), opponent);
				state.takeBack(testMove, player);
				game.printU("ThinkAhead Best: " + bestMove);
				mutex._wait_();
				thinkAheadMap.put(testMove, bestMove);
				mutex.signal();
			}
			moveProcessorReady.signal();
		}

		public Map<IGameMove, IGameMove> getThinkAheadMap()
		{
			return thinkAheadMap;
		}
	}
	
	/**
	 * @author mark
	 * Mar 25, 2007
	 * MoveProcessor thread runs findBestMove [to releave the AWT thread]
	 */
	public class MoveProcessor implements Runnable
	{
		protected IGameState state;
		protected int player;    // Current player
		protected int depth;
		protected MoveEvent moveEvent; // Move or Hint
		protected Thread moveProcessor = new Thread(this, "MoveProcessor");
		protected Semaphore moveProcessorStart = new Semaphore(0, "MoveProcessor.start", true);

		public MoveProcessor()
		{
			super();
			moveProcessor.start();
		}

		public void run()
		{
			while (true)
			{
				moveProcessorStart._wait_();
				game.print(getClass().getName() + " Starting...");
				runOnce();
			}
		}

		public void runOnce()
		{
			game.waitShow();
			setWorking(true);
			IGameMove move = game.getSettings().isThinkAhead()
				? findBestWithThinkAhead()
				: findBestMove(state, depth, player);
			if (game.isPaused()) moveProcessorPause._wait_();
			moveEvent.setSource(move);
			moveEvent.setPlayer(player);
			// Game is the listener. Tell it a move has been made.
			fireMoveEvent(moveEvent, player);
			game.setPlayerReady(true);
			game.waitHide();
			setWorking(false);
		}

		public IGameMove findBestWithThinkAhead()
		{
			Map<IGameMove, IGameMove> thinkAhead = thinkAheadProcessor.getThinkAheadMap();
			IGameMove lastMove = game.getLastMove();
			mutex._wait_();
			// Tell ThinkAhead to wrap up.
			setRequestedToStop(true);
			// Check what ThinkAhead is up to.
			IGameMove move = thinkAhead.get(lastMove);
			boolean isLastMoveIn = thinkAhead.containsKey(lastMove);
			if (move != null || !isLastMoveIn)
			{
				// Hasn't started working on that one - tell it to hurry.
				setMoveNow(true);
			}
			mutex.signal();
			// Wait for it to finish / exit - unless it's the first one.
			if (lastMove != null)
			{
				moveProcessorReady._wait_();
			}
			if (isLastMoveIn) // ThinkAhead was done or has started - and MP waited for it to finish.
			{
				move = thinkAhead.get(lastMove);
			}
			if (move == null) // Hasn't started working on the right one
			{
				move = findBestMove(state, depth, player);
			}
			return move;
		}

		public void start()
		{
			moveProcessorStart.signal();
		}

		public void reset()
		{
			moveProcessorStart.resetCount();
		}

		public void setDepth(int depth)
		{
			this.depth = depth;
		}

		public void setPlayer(int player)
		{
			this.player = player;
		}

		public void setState(IGameState state)
		{
			this.state = state;
		}

		public void setMoveEvent(MoveEvent moveEvent)
		{
			this.moveEvent = moveEvent;
		}
	}

	/**
	 * @author mark
	 * Mar 25, 2007
	 * ThreadPool runs MoveProcessorTask [minMax.getBestMove] for each Available Move
	 */
	public class MoveProcessorTask implements Runnable
	{
		private IGameState state;
		private int player;    // Current player
		private int depth;
		private IGameMove move; // Result
		private Semaphore workDone;

		public MoveProcessorTask(IGameState state, int player, int depth, IGameMove move, Semaphore workDone)
		{
			super();
			this.state = state;
			this.player = player;
			this.depth = depth;
			this.move = move;
			this.workDone = workDone;
		}

		public void run()
		{
			IGameMove bestMove = minMax.getBestMove(state, depth, player, move);
			move.setValue(bestMove == null ? (game.isDrawIfNoMoves() ? 0 : -MAX_VALUE) : bestMove.getValue());
			game.print("Move: " + move + " - Best: " + bestMove + getCount());
			mutex._wait_();
            if (move.getValue() > bestResultSoFar)
            {
                bestResultSoFar = move.getValue();
            }
            if (bestMove != null && bestMove.getValue() == -MAX_VALUE)
            {
                // Found winning move - no point to continue
                game.print("Winning Move: " + move + " - Exiting...");
                setMoveNow(true);
            }
			mutex.signal();
			workDone.signal();
		}

		public IGameMove getMove()
		{
			return move;
		}

		public Semaphore getWorkDone()
		{
			return workDone;
		}
		
		public String toString()
		{
			return "Task depth: " + depth + " move: " + move + " " + state; 
		}
	}
	
	protected void resume()
	{
		moveProcessorPause.signal();
	}

	public void reset()
	{
		moveProcessorReady.resetCount();
		resetCount();
	}

	public void resetCount()
	{
		minMax.resetCount();
	}

	public String getCount()
	{
		int[] count = minMax.getCount();
		String s = " Count";
		for (int i = 0; i < count.length; i++)
		{
			s += "." + count[i];
		}
		return s;
	}

	public static int getOpponent(int player)
	{
		return PLAYER_ONE + PLAYER_TWO - player;
	}

	/**
	 * Search for the BestMove runs in a separate thread.
	 * When BestMove is found a MoveEvent is fired to notify the mk.game.
	 */
	public void thinkAhead(IGameState state, int depth, int player)
	{
		game.print("Starting thinkAhead: player " + player + " depth " + depth);
		thinkAheadProcessor.setState(state.clone());
		thinkAheadProcessor.setDepth(depth);
		thinkAheadProcessor.setPlayer(player);
		thinkAheadProcessor.start();
	}

	/**
	 * Search for the BestMove runs in a separate thread.
	 * When BestMove is found a MoveEvent is fired to notify the mk.game.
	 */
	public void getBestMove(IGameState state, int depth, int player, MoveEvent moveEvent)
	{
		game.print("Starting getBestMove: player " + player + " depth " + depth);
		moveProcessor.setState(state);
		moveProcessor.setDepth(depth);
		moveProcessor.setPlayer(player);
		moveProcessor.setMoveEvent(moveEvent);
		moveProcessor.start();
	}

	public IGameMove findBestMove(IGameState state, int depth, int player)
	{
		return findBestMove(state, depth, player, null);
	}

	public IGameMove findBestMove(IGameState state, int depth, int player, List <IGameMove> possibleMoves)
	{
		possibleMoves = findBestMoves(state, depth, player, possibleMoves);
		return (possibleMoves.isEmpty() ? null : possibleMoves.get(0));
	}

	public List <IGameMove> findBestMoves(IGameState state, int depth, int player)
	{
		return findBestMoves(state, depth, player, null);
	}

	public List <IGameMove> findBestMoves(IGameState state, int depth, int player, List <IGameMove> possibleMoves)
	{
		bestResultSoFar = -MAX_VALUE;
		setMoveNow(false);
		if (depth >= 3)
		{
			// Use Beginner depth to presort possibleMoves by value starting from the best.
			game.print("findBestMoves Sorting... player = " + player + " depth = " + depth);
			possibleMoves = findBestMoves(state, depth / 3, player, possibleMoves);
		}
		else
		{
			if (possibleMoves == null) possibleMoves = getPossibleMoves(state, player);
		}
		IGameMove quickMove = getQuickMove(state, player, possibleMoves);
		if (quickMove != null || game.isGameOver())
		{
			ArrayList <IGameMove> quickList = new ArrayList <IGameMove> ();
			quickList.add(quickMove);
			return quickList; 
		}
		Semaphore workDone = new Semaphore(0, "workDoneSemaphore");
		game.print("Starting findBestMoves... player = " + player + " depth = " + depth);
		for (IGameMove testMove : possibleMoves)
		{
			testMove.setValue(bestResultSoFar); // from tasks already completed.
			IGameState newState = state;
			if (getProcessorCount() > 1) newState = state.clone(); // Can't use the same - tasks run cocurrently!
			MoveProcessorTask task = new MoveProcessorTask(newState, player, depth, testMove, workDone);
			moveProcessorPool.runTask(task);
		}
		workDone._wait_(possibleMoves.size()); // Wait for all to complete.
		Collections.sort(possibleMoves, moveComparator);
		return possibleMoves;
	}

	public void addMoveListener(IMoveListener l)
	{
		moveListenerList.add(IMoveListener.class, l);
	}

	public void removeMoveListener(IMoveListener l)
	{
		moveListenerList.remove(IMoveListener.class, l);
	}

	protected void fireMoveEvent(MoveEvent moveEvent, int player)
	{
		// Notify listeners
		for (IMoveListener listener : moveListenerList.getListeners(IMoveListener.class))
		{
			listener.applyMove(moveEvent, player);
		}
	}

	/**
	 * getPossibleMoves accepts a GameState and asks it to get a List of all
	 * possible GameMoves for the specific player.
	 */
	public List <IGameMove> getPossibleMoves(IGameState state, int player)
	{
		return state.getPossibleMoves(player);
	}

	/**
	 * applyMove merges a GameMove into a GameState, returning the new
	 * GameState. player represents the value associated with either the
	 * computer or human player.
	 */
	public IGameState applyMove(IGameState state, IGameMove move, int player)
	{
		return state.applyMove(move, player);
	}

	/**
	 * takeBack takes back the last GameMove by a player, returning the new GameState
	 */
	public IGameState takeBack(IGameState state, IGameMove move, int player)
	{
		return state.takeBack(move, player);
	}

	/**
	* evaluate analyses a GameState and returns an int score.
	* Last applied aMove could be used to optimize evaluate??? 
	*/
	public int evaluate(IGameState state, IGameMove move, int player)
	{
		return evaluate(state, player);
	}

	/**
	* evaluate analyses a GameState and returns an int score.
	*/
	public int evaluate(IGameState state, int player)
	{
		return state.evaluate(player);
	}

	/**
	 * quickMove determines whether a move can be recommended without
	 * calling the full MinMax method.  For example, it may be worth
	 * checking whether a player can win on the next turn, before
	 * building the full MinMax tree.  The default implementation is to
	 * return null.
	 */
	public IGameMove getQuickMove(IGameState aState)
	{
		return null;
	}
	public IGameMove getQuickMove(IGameState state, int player, List <IGameMove> possibleMoves)
	{
		//	No moves
		if (possibleMoves.isEmpty())
		{
			return null;
		}
		//	Single move - don't think
		else if (possibleMoves.size() == 1)
		{
			return possibleMoves.get(0);
		}
		//	Check if one move ends the mk.game
		for (IGameMove testMove : possibleMoves)
		{
			state.applyMove(testMove, player);
			boolean isWin = state.isMoveWin(testMove, player);
			state.takeBack(testMove, player);
			if (isWin)
			{
				return testMove; 
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public boolean isWorking()
	{
		return isWorking;
	}

	/**
	 * @param b
	 */
	public void setWorking(boolean b)
	{
		isWorking = b;
	}

	public IGame getGame()
	{
		return game;
	}

	public void setGame(Game game)
	{
		this.game = game;
	}

	public synchronized void clearPossibleMovesCache()
	{
//		waitForPrepare();
		possibleMovesCache.clear();
		setPossibleMovesCacheReady(false);
	}

	public List <IGameMove> getAndStorePossibleMoves(IGameState state, int player)
	{
		setPossibleMovesCacheReady(false);
		List <IGameMove> possibleMoves = state.getPossibleMoves(player);
		if (possibleMoves.isEmpty())
		{
			fireMoveEvent(noMove, player);
		}
		setPossibleMovesCache(possibleMoves);
		setPossibleMovesCacheReady(true);
		return possibleMoves;
	}

	public List <IGameMove> getPossibleMovesFromCache(final IGameState state, final int player)
	{
//		mk.game.print("preparePossibleMoves from getPossibleMovesFromCache. " + possibleMovesCache.size());
		if (possibleMovesCache.isEmpty())
		{
			possibleMovesCache = getAndStorePossibleMoves(state, player);
		}
		return possibleMovesCache;
	}

	public void setPossibleMovesCache(List <IGameMove> possibleMovesCache)
	{
		this.possibleMovesCache = possibleMovesCache;
	}

	public boolean isPossibleMovesCacheReady()
	{
		return isPossibleMovesCacheReady;
	}

	public void setPossibleMovesCacheReady(boolean possibleMovesCacheReady)
	{
		this.isPossibleMovesCacheReady = possibleMovesCacheReady;
	}

	public boolean isMoveNow()
	{
		return isMoveNow;
	}

	public void setMoveNow(boolean isMoveNow)
	{
		this.isMoveNow = isMoveNow;
	}

	public boolean isRequestedToStop()
	{
		return isRequestedToStop;
	}

	public void setRequestedToStop(boolean isRequestedToStop)
	{
		this.isRequestedToStop = isRequestedToStop;
	}

	public int getProcessorCount()
	{
		return moveProcessorPool.getPoolSize();
	}

	public void setProcessorCount(int processorCount)
	{
		moveProcessorPool.setPoolSize(processorCount);
	}
}
