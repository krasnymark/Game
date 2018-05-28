package mk.game.common.util;

import java.util.Date;

public class Log
{
	private static Date start = null ;
	
	public static void print(String message)
	{
		print(message, 0) ;
	}
	
	public static void print(String message, int indent)
	{
		Date currentTime = new Date() ;
		if (start == null) start = currentTime ;
		long timeElapsed = currentTime.getTime() - start.getTime() ;
		String time = padLeft("" + timeElapsed, 5) ;
		System.out.println(time + " - " + indent(indent) + message) ;
	}

	public static String indent(int size)
	{
		return replicate("\t", size) ;
	}

	public static String padRight(String s, int size)
	{
		return (s + space(size)).substring(0, size) ;
	}

	public static String padLeft(String s, int size)
	{
		return (space(size) + s).substring(s.length()) ;
	}

	public static String space(int size)
	{
		return replicate(" ", size) ;
	}

	public static String replicate(String s, int size)
	{
		StringBuffer rep = new StringBuffer() ;
		for (int i = 0 ; i < size ; i++)
		{
			rep.append(s) ;
		}
		return rep.toString() ;
	}
}
