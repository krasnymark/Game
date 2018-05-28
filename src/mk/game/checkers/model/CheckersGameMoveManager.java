/*
 * Created on Dec 26, 2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.model ;

import mk.game.common.model.Game ;
import mk.game.common.model.GameMoveManager ;

import java.awt.Point ;

/**
 * @author mk To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersGameMoveManager extends GameMoveManager
{
	public static Point[] directions = null ;

	/**
	 * @param defaultMin
	 * @param defaultMax
	 * @param game
	 */
	public CheckersGameMoveManager(Game game)
	{
		super(game) ;
		// TODO Auto-generated constructor stub
	}

	public static int getDirection(int player)
	{
		return (player == GameMoveManager.PLAYER_ONE) ? 1 : -1 ;
	}

	public static Point[] getDirections()
	{
		if (directions == null)
		{
			directions = new Point[4] ;
			directions[0] = new Point(1, 1) ;
			directions[1] = new Point(-1, 1) ;
			directions[2] = new Point(-1, -1) ;
			directions[3] = new Point(1, -1) ;
		}
		return directions ;
	}

	// After a take in dir continue looking in 3 directions but not back where started
	public static Point[] getDirectionsAfter(Point dir)
	{
		Point[] dirs = new Point[3] ;
		int n = 0 ;
		for (int i = 0; i < getDirections().length; i++)
		{
			if (directions[i].x == -dir.x && directions[i].y == -dir.y) continue ;
			dirs[n++] = directions[i] ;
		}
		return dirs ;
	}
	
	public static String printPoint(Point p)
	{
		return "(" + (p.x + 1) + "," + (p.y + 1) + ")" ;
	}

}