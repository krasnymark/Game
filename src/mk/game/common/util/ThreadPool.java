/**
 * 
 */
package mk.game.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MK
 * PoolRunnable adds a Semaphore to each Runnable task to signal main thread when the task is done.
 */
public class ThreadPool
{
	private int minPoolSize ;
	private int maxPoolSize ;
	private int poolSize ;
	private List <WorkerThread> workerThreads ;
	private Semaphore mutex = new Semaphore(1, "ThreadPool.mutex") ;
	private Semaphore workerNeeded = new Semaphore(0, "ThreadPool.workerNeeded") ;
	private Semaphore workerAvailable ;
	private Queue <Runnable> buffer = new Queue <Runnable> () ;
	// TODO Set a timeout for resizing pool down

	public ThreadPool(int poolSize)
	{
		this(poolSize, poolSize * 2) ;
	}

	public ThreadPool(int poolSize, int maxPoolSize)
	{
		this(1, poolSize, maxPoolSize) ;
	}

	public ThreadPool(int poolSize, int minPoolSize, int maxPoolSize)
	{
		super();
		this.poolSize = poolSize ;
		this.minPoolSize = minPoolSize ;
		this.maxPoolSize = maxPoolSize ;
		workerThreads = new ArrayList<WorkerThread>(this.maxPoolSize) ;
		init() ;
	}

	public class WorkerThread extends Thread
	{
		private boolean isKeepAlive = true ;

		public WorkerThread(String name)
		{
			super(name);
		}
		
		public void run()
		{
			while (isKeepAlive())
			{
				workerNeeded._wait_() ;
				mutex._wait_() ;
				Runnable task = buffer.poll() ;
				mutex.signal() ;
				task.run() ;
				workerAvailable.signal() ;
			}
		}

		public boolean isKeepAlive()
		{
			return isKeepAlive;
		}

		public void setKeepAlive(boolean isKeepAlive)
		{
			this.isKeepAlive = isKeepAlive;
		}
	}

	public void init()
	{
		resizeUp() ;
	}

	public void initSemaphore()
	{
		workerAvailable = new Semaphore(this.poolSize, "ThreadPool.workerAvailable") ;
	}

	public void resizeUp()
	{
		initSemaphore() ;
		int oldSize = workerThreads.size() ;
		for (int i = oldSize ; i < this.poolSize ; i++)
		{
			WorkerThread worker = new WorkerThread("TP-" + i) ;
			workerThreads.add(worker) ;
			worker.start() ;
		}
	}

	public void resizeDown()
	{
		initSemaphore() ;
		int oldSize = workerThreads.size() ;
		for (WorkerThread worker : workerThreads.subList(oldSize, this.poolSize))
		{
			worker.setKeepAlive(false) ;
		}
		workerThreads.subList(oldSize, this.poolSize).clear() ;
	}

	public void runTask(Runnable task)
	{
		workerAvailable._wait_() ;
		mutex._wait_() ;
		buffer.add(task) ;
		mutex.signal() ;
		workerNeeded.signal() ;
	}

//	public boolean isDone()
//	{
//		return buffer.isEmpty() ;
//	}

	public int getPoolSize()
	{
		return poolSize;
	}

	public boolean setPoolSize(int poolSize)
	{
		boolean canResize = false ;
		mutex._wait_() ;
		if (buffer.isEmpty())
		{
			if (poolSize < minPoolSize) poolSize = minPoolSize ;
			if (poolSize > maxPoolSize) poolSize = maxPoolSize ;
			if (this.poolSize != poolSize)
			{
				canResize = true ;
				this.poolSize = poolSize ;
				if (workerThreads.size() < poolSize) resizeUp() ;
				if (workerThreads.size() > poolSize) resizeDown() ;
			}
		}
		mutex.signal() ;
		return canResize ;
	}
}
