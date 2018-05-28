/*
 * Created on Apr 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.model;

import mk.game.common.util.*;

import java.sql.Time;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GameTimer implements Runnable
{
	private final static long quant = 250 ;
	private long timeElapsedMillis = 0 ;
	private long lastMoveTimeMillis = 0 ;
	private boolean isStopped = true ;
	private IGamePlayer parent ;
	private Thread timer = new Thread(this) ;
	private EventListenerList listenerList = new EventListenerList();
	private ChangeEvent changeEvent = null ;
	private TimerEvent timerEvent = null ;
	private Semaphore startWatch = new Semaphore(0, "Timer.startWatch") ;
	private Semaphore stopWatch = new Semaphore(0, "Timer.stopWatch") ;

	public GameTimer(IGamePlayer parent)
	{
		setParent(parent) ;
		timer.start() ;
	}

	public void addChangeListener(ChangeListener l)
	{
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l)
	{
		listenerList.remove(ChangeListener.class, l);
	}

	public void addTimerListener(ITimerListener l)
	{
		listenerList.add(ITimerListener.class, l);
	}

	public void removeTimerListener(ITimerListener l)
	{
		listenerList.remove(ITimerListener.class, l);
	}

	protected void fireChangeEvent()
	{
		// Notify listeners
		for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
		{
			listener.stateChanged(getChangeEvent());
		}
	}

	protected void fireTimerWatchStartedEvent()
	{
		// Notify listeners
		for (ITimerListener listener : listenerList.getListeners(ITimerListener.class))
		{
			listener.timerWatchStarted(getTimerEvent());
		}
	}

	public void run()
	{
		while (true)
		{
			if (isStopped())
			{
				startWatch._wait_() ;
			}
			else
			{
				long started = System.currentTimeMillis() ;
				stopWatch._wait_(quant) ;
				long delta = System.currentTimeMillis() - started ;
				setLastMoveTimeMillis(getLastMoveTimeMillis() + delta) ;
				setTimeElapsedMillis(getTimeElapsedMillis() + delta) ;
			}
		}
	}

	public void startWatch()
	{
		setStopped(false) ;
		startWatch.signal() ;
		setLastMoveTimeMillis(0) ;
		fireTimerWatchStartedEvent() ;
	}

	public void stopWatch()
	{
		setStopped(true) ;
		stopWatch.signal() ;
	}

	public void resumeWatch()
	{
		setStopped(false) ;
		startWatch.signal() ;
	}

	/**
	 * @return
	 */
	private boolean isStopped()
	{
		return isStopped;
	}

	public void reset()
	{
		setTimeElapsedMillis(0) ;
	}

	/**
	 * @return
	 */
	public String getTimeElapsed()
	{
		return formatMillis(getTimeElapsedMillis()) ; // mm:ss
	}

	public String getLastMoveTime()
	{
		return formatMillis(getLastMoveTimeMillis()) ; // mm:ss
	}

	public static String formatMillis(long ms)
	{
		return (new Time(ms)).toString().substring(3) ; // mm:ss
	}

	public String toString()
	{
		return " Time Elapsed: " + getTimeElapsed() + " [" + getTimeElapsedMillis() + "]" ;
	}

	public long getTimeElapsedMillis()
	{
		return timeElapsedMillis;
	}

	/**
	 * @param b
	 */
	private void setStopped(boolean b)
	{
		isStopped = b;
	}

	public void rollbackTimeElapsedMillis(long time)
	{
		setTimeElapsedMillis(getTimeElapsedMillis() - time) ;
	}

	public void rollForwardTimeElapsedMillis(long time)
	{
		rollbackTimeElapsedMillis(-time) ;
	}

	public void rollbackLastTimeElapsedMillis()
	{
		rollbackTimeElapsedMillis(getLastMoveTimeMillis()) ;
	}

	private void setTimeElapsedMillis(long l)
	{
		timeElapsedMillis = l;
		fireChangeEvent() ;
	}

	public long getLastMoveTimeMillis()
	{
		return lastMoveTimeMillis ;
	}
	public void setLastMoveTimeMillis(long lastMoveTimeMillis)
	{
		this.lastMoveTimeMillis = lastMoveTimeMillis ;
	}

	public IGamePlayer getParent()
	{
		return parent;
	}

	public void setParent(IGamePlayer parent)
	{
		this.parent = parent;
	}

	public ChangeEvent getChangeEvent()
	{
		if (changeEvent == null)
		{
			changeEvent = new ChangeEvent(this) ;
		}
		return changeEvent;
	}

	public TimerEvent getTimerEvent()
	{
		if (timerEvent == null) 
		{
			timerEvent = new TimerEvent(this) ;
		}
		return timerEvent;
	}
}
