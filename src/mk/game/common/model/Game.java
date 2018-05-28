/*
 * Created on Apr 18, 2004
 */
package mk.game.common.model;

import mk.game.common.util.*;
import mk.game.common.view.*;

import java.awt.Cursor;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.swing.RootPaneContainer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * @author mk
 */
public abstract class Game implements IGame, IMoveListener, ITimerListener
{
	private static final Logger logger = Logger.getLogger(Game.class);

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public static enum TimeType {Fixed, Unlimited};
	public static enum PlayerType {Human, Computer};
	public static enum PlayerLevel {Beginner, Intermediate, Advanced, Expert, Master};

	private PlayerLevel playerLevel = PlayerLevel.Beginner;
	private int processorCount = 1;
//	private int depth = 1;
	private int whoIsFirst;
	private IGamePlayer[] gamePlayer = new GamePlayer[3]; // Skip 0 to use player as an index
	private boolean talk = true;
	private boolean gameOver = false;
	private boolean isPlayerReady = true; // Duplicates manager.isWorking
	private Stack <IGameMove> moves = new Stack <IGameMove> ();
	private Stack <IGameMove> replay = new Stack <IGameMove> ();
	private int whoMovesNext = 1;
	private int winner = 0;
	private boolean isPaused = false;
	private boolean isSuspended = false;
    private boolean isInitialized = false;
	private MoveEvent moveEvent = new MoveEvent(new GameMove()); // Reuse same objects
	private HintEvent hintEvent = new HintEvent(new GameMove()); // for Signals
	private EventListenerList listenerList = new EventListenerList();
	private ChangeEvent changeEvent = null;

	protected GameMoveManager manager;
	protected IGameState state; // In a Single Thread Model - that's all we need!
	protected IGameView view;
	protected GameSettings settings;
    protected String rules;

	private VelocityUtils velocityUtils;

	public Game()
	{
		this(null);
	}

	public Game(RootPaneContainer parent)
	{
		super();
		// TODO Auto-generated constructor stub
//		BeanFactory factory = new ClassPathXmlApplicationContext("gameContext.xml");
//		velocityUtils = (VelocityUtils) factory.getBean("velocityUtils");
//		init(parent);
        initGame();
	}

    @Override
    public void help()
    {
    }

	public void waitShow()
	{
		view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void waitHide()
	{
		view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
    public void startNewGame()
	{
        init();
		printU("New Game:"); // Level = " + getPlayerLevel());
		print("" + gamePlayer[1]);
		printU("" + gamePlayer[2]);
		gamePlayer[1].resetTimer();
		gamePlayer[2].resetTimer();
		setGameOver(false);
		setWinner(0);
		moves.clear();
		replay.clear();
		manager.reset();
		int player = GameMoveManager.PLAYER_ONE;
		setWhoIsFirst(player);
		setWhoMovesNext(player);
		state.init();
		manager.getAndStorePossibleMoves(state, player);
		getView().showState(state);
		print("" + state);
		gamePlayer[player].startWatch();
	}

    @Override
    public void showGame()
    {
        getView().showGame();
    }

    public void pause()
	{
		setPaused(!isPaused());
		if (isPaused())
		{
			gamePlayer[whoMovesNext].stopWatch();
		}
		else
		{
			gamePlayer[whoMovesNext].resumeWatch();
			manager.resume();
		}
	}

	public void goFirst()
	{
		startNewGame();
		int player = GameMoveManager.PLAYER_TWO;
		setWhoIsFirst(player);
		applyMove(getFirstMove(), player);
	}

	@Override
    public IGameMove getFirstMove()
	{
		int player = GameMoveManager.PLAYER_TWO;
		List  <IGameMove> moves = state.getPossibleMoves(player);
		int i = new Long(Math.round((moves.size() - 1) * Math.random())).intValue();
		return moves.get(i);
	}

	@Override
    public void init()
	{
		init(null);
	}

	@Override
    public void init(RootPaneContainer parent)
	{
        if (isInitialized) return;
        // V
        setView(getGameView());
        isInitialized = true;
	}

    public boolean isInitialized()
    {
        return isInitialized;
    }

    private void initGame()
    {
        // M
        setState(getGameState());
        setGameSettings(getGameSettings());
//		applySettings();
        gamePlayer[1] = settings.getGamePlayer(1);
        gamePlayer[2] = settings.getGamePlayer(2);
        gamePlayer[1].getTimer().addTimerListener(this);
        gamePlayer[2].getTimer().addTimerListener(this);
        // C
        setManager(getGameMoveManager());
        manager.addMoveListener(this);
    }

    public boolean needToCloneState()
    {
        return false;
    }

	@Override
    public void showHint(IGameMove aMove, int player)
	{
		if (aMove != null)
		{
			view.hint(aMove);
		}
	}

	@Override
    public void showHint(IGameMove aMove)
	{
		showHint(aMove, GameMoveManager.PLAYER_ONE);
	}

	@Override
    public void applySettings()
	{
		manager.setProcessorCount(settings.getProcessorCount());
		settings.setProcessorCount(manager.getProcessorCount()); // In case the Pool could not be resized
	}

	@Override
    public void restart()
	{
		setState(getGameState());
		getView().dispose();
		setView(getGameView(getView().getParentContainer()));
		startNewGame();
	}

	protected boolean isDraw()
	{
		return isGameOver() && getWinner() == 0;
	}

	public void timerWatchStarted(TimerEvent timerEvent)
	{
		int player = getWhoMovesNext();
		setPlayerReady(false);
		if (gamePlayer[player].isHuman())
		{
			if (settings.isThinkAhead())
			manager.thinkAhead(state, getDepth(), player);
		}
		else
		{
			manager.getBestMove(state, getDepth(), player, moveEvent);
		}
	}

	public void makeMove(IGameMove move)
	{
		if (manager.isWorking() || isGameOver()) return;
		replay.clear();
		int player = getWhoMovesNext();
		if (isSuspended())
		{
			gamePlayer[player].startWatch(); // To activate Think-Ahead and signal Move-Processor
		}
		setPaused(false);
		setSuspended(false);
		applyMove(move, player);
	}

	public void applyMove(MoveEvent moveEvent, int player)
	{
		moveEvent.applyMove(this, player);
	}
	
	public void gameOver(String message)
	{
		setGameOver(true);
		if (!isDraw()) showWinner();
        view.showInfo("Game over", message);
	}
	
	private String whoWon(int player)
	{
		return "Player " + player + " wins";
	}

	@Override
    public void applyMove(IGameMove move, int player)
	{
		gamePlayer[player].stopWatch();
		int opponent = GameMoveManager.getOpponent(player);
        if (move == null)
		{
			gameOver(isDrawIfNoMoves() ? "It's a draw" : whoWon(opponent));
		}
		else
		{
			move.setTime(gamePlayer[player].getTimer().getLastMoveTimeMillis());
			state.applyMove(move, player);
			moves.push(move);
			view.applyMove(move, player);
			boolean checkWin = true;
            move.setValue(state.evaluate(player, checkWin));
			printMove(move, player);
			if (getWinner() > 0)
			{
				gameOver(whoWon(player));
			}
		}
		setWhoMovesNext(opponent); // Before [!] returning in case of a take-back.
		if (isGameOver()) return;
		manager.getAndStorePossibleMoves(state, opponent);
		if (!isSuspended())
		{
			gamePlayer[opponent].startWatch();
		}
	}

	@Override
    public void takeBack()
	{
		if (moves.size() > 0)
		{
			// TODO Really need to sync with applyMove->setWhoMovesNext
			int player = getWhoMovesNext(); 
			if (!isSuspended())
			{
				setPaused(true);
				setSuspended(true);
				gamePlayer[player].stopWatch();
				gamePlayer[player].getTimer().rollbackLastTimeElapsedMillis();
			}
			int opponent = GameMoveManager.getOpponent(player);
			IGameMove move = moves.pop();
			replay.push(move);
			state.takeBack(move, opponent);
			setGameOver(false);
			setWinner(0);
			manager.clearPossibleMovesCache();
            view.takeBack(move);
            view.showState(state);
			// Adjust Player Timer and tell it to fireChangedEvent
			gamePlayer[opponent].getTimer().rollbackTimeElapsedMillis(move.getTime());
			setWhoMovesNext(opponent);
			manager.getAndStorePossibleMoves(state, opponent);
		}
	}

	public void replay()
	{
		if (replay.size() > 0)
		{
            int player = getWhoMovesNext();
			IGameMove move = replay.pop();
			// TODO applyMove fires MoveEvent and messes up the moves/replay relation. Need to wait for real move
			applyMove(move, player);
			gamePlayer[player].getTimer().rollForwardTimeElapsedMillis(move.getTime());
		}
	}

	public void printMove(IGameMove move, int player)
	{
		printU("");
		print(getMoveNumber() + " " + gamePlayer[player] + " "
			+ "[" + getDepth() + "]" + move.getDescr() + " " 
			+ manager.getCount() + "\n" + state);
	}

	@Override
    public void hint(int player)
	{
		manager.getBestMove(state, getDepth(), player, hintEvent);
	}

	public void hint()
	{
		hint(getWhoMovesNext());
	}

	public void moveNow()
	{
		manager.setMoveNow(true);
	}

	public GameTimer getGameTimer(int player)
	{
		return gamePlayer[player].getTimer();
	}

	public int getMoveNumber()
	{
		return (moves.size() + 1) / 2;
	}

	public IGameMove getMove(int i)
	{
		if (0 <= i && i < moves.size()) return moves.get(i);
		else return null;
	}

	public IGameMove getLastMove()
	{
		return moves.isEmpty() ? null : moves.lastElement();
	}

	public void exit()
	{
        view.exit();
	}

	/**
	 * @return
	 */
	public boolean isTalk()
	{
		return talk;
	}

	/**
	 * @param b
	 */
	public void setTalk(boolean b)
	{
		talk = b;
	}

	public static String printInfo(String s)
	{
		String prefix = dateFormat.format(new Date());
		boolean isAddThreadInfo = false;
		if (isAddThreadInfo) prefix += " " + Thread.currentThread().getName();
		return prefix + " " + s;
	}

	public void printLine(String s)
	{
		if (isTalk())
		{
			System.out.println(s);
		}
	}

	public void print(String s)
	{
		printLine(printInfo(s));
	}

	public void printU(String s)
	{
		String l = printInfo(s);
		String u = "";
		for (int i = 0; i < l.length(); i++)
		{
			u += "-";
		}
		printLine(l);
		printLine(u);
	}

	/**
	 * @return
	 */
	public int getWhoIsFirst()
	{
		return whoIsFirst;
	}

	/**
	 * @param i
	 */
	public void setWhoIsFirst(int i)
	{
		whoIsFirst = i;
	}

	/**
	 * @return
	 */
	@Override
    public IGameState getState()
	{
		if (state == null) state = getGameState();
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(IGameState state)
	{
		this.state = state;
	}

	/**
	 * @return
	 */
	@Override
    public GameMoveManager getManager()
	{
		if (manager == null) manager = getGameMoveManager();
		return manager;
	}

	/**
	 * @param manager
	 */
	public void setManager(GameMoveManager manager)
	{
		this.manager = manager;
	}

	/**
	 * @return
	 */
	@Override
    public IGameView getView()
	{
		if (view == null) view = getGameView();
		return view;
	}

	/**
     * @param view
     */
	public void setView(IGameView view)
	{
		this.view = view;
	}

	/**
	 * @param b
	 */
	public void setGameOver(boolean b)
	{
		gameOver = b;
	}

	/**
	 * @return
	 */
	public boolean isGameOver()
	{
		return gameOver;
	}

	public boolean isPlayerReady()
	{
		return isPlayerReady;
	}

	public void setPlayerReady(boolean isPlayerTwoReady)
	{
		this.isPlayerReady = isPlayerTwoReady;
	}

	@Override
    public GameSettings getSettings()
	{
		if (settings == null) settings = getGameSettings();
		return settings;
	}

	public void setGameSettings(GameSettings gameSettings)
	{
		this.settings = gameSettings;
	}

	public int getWhoMovesNext()
	{
		return whoMovesNext;
	}

	public void setWhoMovesNext(int player)
	{
		this.whoMovesNext = player;
	}

	public int getWinner()
	{
		return winner;
	}

	public void setWinner(int winner)
	{
		this.winner = winner;
	}

	public int getProcessorCount()
	{
		return processorCount;
	}

	public void setProcessorCount(int processorCount)
	{
		this.processorCount = processorCount;
	}

	public PlayerLevel getPlayerLevel()
	{
		return playerLevel;
	}

	public void setPlayerLevel(PlayerLevel newLevel)
	{
		print("Changed Level from = " + playerLevel + " - to = " + newLevel);
		playerLevel = newLevel;
		if (!gamePlayer[1].isHuman()) gamePlayer[1].setPlayerLevel(playerLevel);
		if (!gamePlayer[2].isHuman()) gamePlayer[2].setPlayerLevel(playerLevel);
        view.setPlayerLevel(playerLevel);
	}

    public int getPlayerLevels()
    {
        return 3;
    }

	public int getDepth(int player)
	{
		return gamePlayer[player].getPlayerLevel().ordinal() + 1;
	}

	@Override
    public int getDepth()
	{
		return getDepth(getWhoMovesNext());
	}

	public IGamePlayer[] getGamePlayer()
	{
		return gamePlayer;
	}

	public IGamePlayer getGamePlayer(int player)
	{
		return gamePlayer[player];
	}

	public boolean isPaused()
	{
		return isPaused;
	}

	public void setPaused(boolean isPaused)
	{
		this.isPaused = isPaused;
		fireChangeEvent();
	}

	public void addChangeListener(ChangeListener l)
	{
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l)
	{
		listenerList.remove(ChangeListener.class, l);
	}

	protected void fireChangeEvent()
	{
		// Notify listeners
		for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
		{
			listener.stateChanged(getChangeEvent());
		}
	}

	public ChangeEvent getChangeEvent()
	{
		if (changeEvent == null)
		{
			changeEvent = new ChangeEvent(this);
		}
		return changeEvent;
	}

	@Override
    public void save()
	{
		VelocityContext ctx = new VelocityContext();
		ctx.put("mk/game", this);
		Template template = velocityUtils.getTemplate("template.xml");
		String xmlString = velocityUtils.mergeWithTemplate(ctx, template);
		logger.debug(xmlString);
	}

    public void showRules()
    {
        view.showInfo(getName() + " Game Rules", getRules());
    }

	public boolean isSuspended()
	{
		return isSuspended;
	}

	public void setSuspended(boolean isSuspended)
	{
		this.isSuspended = isSuspended;
	}

    public String getRules()
    {
        return rules;
    }

    public void setRules(String rules)
    {
        this.rules = rules;
    }
}
