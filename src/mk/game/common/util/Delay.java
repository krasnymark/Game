package mk.game.common.util;
/**
 * 
 */

/**
 * @author mark
 *
 */
public class Delay
{
	public static void delay(double avgTime)
	{
	try
	{
		Thread.sleep((long)avgTime) ;
	}
	catch (InterruptedException e) 
	{}
	}

	public static void randomDelay(double avgTime)
	{
	try
	{
		Thread.sleep((long)(Math.random() * avgTime)) ;
	}
	catch (InterruptedException e) 
	{}
	}
}
