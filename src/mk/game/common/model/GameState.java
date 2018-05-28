package mk.game.common.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * GameState
 * 
 * GameState represents a snapshot of a mk.game. The only data element in this
 * class is value, representing the result of MoveManager's evaluate method
 * applied to this state. The information needed to represent the mk.game
 * configuration itself is specific to the particular mk.game, and therefore
 * belongs in GameState's subclasses.
 */
public abstract class GameState implements Serializable, Cloneable, IGameState
{
	protected Game game ;
	protected IGameMove lastMove ;
	protected int value ;

	public GameState(Game game)
	{
		super();
		// TODO Auto-generated constructor stub
		this.game = game;
	}
	
	public IGameState clone()
	{
		try
		{
			return (IGameState) super.clone() ;
		}
		catch (CloneNotSupportedException e) 
		{
			throw new InternalError(e.toString()) ;
		}
	}

	public IGameState _clone()
	{
		IGameState newState = null ;
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
			ObjectOutputStream os = new ObjectOutputStream(bos) ;
			os.writeObject(this) ;
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray()) ;
			ObjectInputStream is = new ObjectInputStream(bis) ;
			newState = (IGameState) is.readObject() ;
			bis.close() ;
			bos.close() ;
		}
		catch (IOException e) 
		{ game.print("clone " + e) ;}
		catch (ClassNotFoundException e) 
		{ game.print("clone " + e) ;}
		return newState ;
	}

	/**
	 * @return
	 */
	@Override
    public int getValue()
	{
		return value ;
	}

	/**
	 * @param i
	 */
	@Override
    public void setValue(int i)
	{
		value = i ;
	}

	@Override
    public  void init()
	{
		setValue(0) ;
	}

    /*
    * (non-Javadoc)
    *
    * @see mk.game.common.model.GameState#evaluate(int, boolean)
    */
    @Override
    public int evaluate(int player)
    {
        boolean checkWin = false ;
        return evaluate(player, checkWin) ;
    }

    /**
	 * @return: Was last move a winning one
	 */
	@Override
    public boolean isLastMoveWin()
	{
		return isMoveWin(getLastMove(), 0) ;
	}

	/**
	 * @return
	 */
	@Override
    public IGameMove getLastMove()
	{
		return lastMove ;
	}

	/**
	 * @param move
	 */
	@Override
    public void setLastMove(IGameMove move)
	{
		lastMove = move ;
	}

	@Override
    public Game getGame()
	{
		return game;
	}

	public List getPossibleMovesFromCache()
	{
		return getPossibleMovesFromCache(game.getWhoMovesNext()) ;
	}

	public List getPossibleMovesFromCache(int player)
	{
		return game.getManager().getPossibleMovesFromCache(this, player) ;
	}

    @Override
    public IGameState simplify()
    {
        return this;  // self by default
    }
}