/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.model;


/**
 * @author mk GamePiece represents a piece in a mk.game where not all the pieces
 *         are of equal value. It can be used to communicate a recommended next
 *         move, or a proposed move for testing. It must know its value used by
 *         the evaluate method. Subclasses should add the data necessary to
 *         represent the piece itself.
 */
public abstract class GamePiece
{
	int player ;

	/**
	 * @return Returns the player.
	 */
	public int getPlayer()
	{
		return player ;
	}

	/**
	 * @param player
	 *           The player to set.
	 */
	public void setPlayer(int player)
	{
		this.player = player ;
	}

	/**
	 * 
	 */
	public GamePiece(int player)
	{
		super() ;
		setPlayer(player) ;
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof GamePiece)
		{
			GamePiece piece = (GamePiece) obj ;
			if (piece.getPlayer() == getPlayer()) return true ;
		}
		return false ;
	}

	/**
	 * @return Returns the value.
	 */
	public abstract int getValue() ;

	public abstract String getName() ;

}