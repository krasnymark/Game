/**
 * 
 */
package mk.game.common.model;

/**
 * @author mark
 *
 */
public class HintEvent extends MoveEvent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param obj
	 */
	public HintEvent(Object obj)
	{
		super(obj);
		// TODO Auto-generated constructor stub
	}

	public void applyMove(Game game, int player)
	{
		game.showHint(getMove(), player) ;
	}

}
