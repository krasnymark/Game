/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

/**
 * @author mk To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGameCell
{
	public static final int CHECKERS_CELL_BLACK = 1 ;
	public static final int CHECKERS_CELL_WHITE = 2 ;

	int type ;
	CheckersGamePiece piece = null ;

	public CheckersGameCell(int type)
	{
		this(type, null) ;
	}

	public CheckersGameCell(int type, CheckersGamePiece piece)
	{
		this.type = type ;
		this.piece = piece ;
	}

	/**
	 * 
	 */
	public CheckersGameCell()
	{
		super() ;
		// TODO Auto-generated constructor stub
	}

	public String toString()
	{
		String p = piece == null ? " " : piece.shortDescr() ;
		String s = "" + getPlayer() + p ;
		return s ;
	}

	/**
	 * @return Returns the piece.
	 */
	public CheckersGamePiece getPiece()
	{
		return piece ;
	}
	/**
	 * @param piece The piece to set.
	 */
	public void setPiece(CheckersGamePiece piece)
	{
		this.piece = piece ;
	}

	public void setPiece(int player, boolean isPawn)
	{
		if (piece == null)
		{
			piece = new CheckersGamePiece(player, isPawn) ;
		}
		else
		{
			piece.setPlayer(player) ;
			piece.setPawn(isPawn) ;
		}
	}
	/**
	 * @return Returns the player.
	 */
	public int getPlayer()
	{
		return piece == null ? 0 : piece.getPlayer() ;
	}
	/**
	 * @param player The player to set.
	 */
	public void setPlayer(int player)
	{
		if (piece != null) piece.setPlayer(player) ;
	}
}