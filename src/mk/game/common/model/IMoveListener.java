/**
 * 
 */
package mk.game.common.model;

import java.util.EventListener;

/**
 * @author mark
 *
 */
public interface IMoveListener extends EventListener
{
	public void applyMove(MoveEvent moveEvent, int player);
}
