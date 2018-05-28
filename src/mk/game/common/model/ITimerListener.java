/*
 * Created on May 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.model;

import java.util.EventListener;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ITimerListener extends EventListener
{
	public void timerWatchStarted(TimerEvent e);

}
