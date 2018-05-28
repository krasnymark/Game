package mk.game.common.util;

/**
 * @author mark
 *
 */
public class Semaphore
{
	private int initialCount ;
	private int count ;
	private String name ;
	private boolean debug = false ;
	
	public Semaphore(int count, String name, boolean debug)
	{
		super();
		// TODO Auto-generated constructor stub
		this.initialCount = count;
		this.count = count;
		this.name = name;
		this.debug = debug;
	}

	public Semaphore(int count, String name)
	{
		this(count, name, false) ;
	}
	
	public Semaphore(int count)
	{
		this(count, null) ;
	}
	
	public synchronized void _wait_() // throws InterruptedException
	{
		long timeout = 0 ;
		_wait_(timeout) ;
	}
	
	public synchronized void _wait_(long timeout) // throws InterruptedException
	{
//		print(Thread.currentThread().getName() + " checking: " + getDescr()) ;
		count-- ;
		if (count < 0)
		{
			print(Thread.currentThread().getName() + " waiting on: " + getDescr()) ;
			try
			{
				wait(timeout) ;
			}
			catch (InterruptedException e)
			{}
		}
	}
	
	public synchronized void _wait_(int n) // Preload with N
	{
		for (int i = 0 ; i < n ; i++)
		{
			_wait_() ;
		}
	}
	
	public synchronized void signal()
	{
		count++ ;
		notify() ;
		print(Thread.currentThread().getName() + " signaled: " + getDescr()) ;
	}
	
	public synchronized void signal(int n) // Preload with N signals
	{
		for (int i = 0 ; i < n ; i++)
		{
			signal() ;
		}
	}

	public String getDescr()
	{
		return getName() + " [" + count + "]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void print(String message)
	{
		if (debug) Log.print(message, 6) ;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}
	
	public synchronized void resetCount()
	{
		count = initialCount ;
	}
}
