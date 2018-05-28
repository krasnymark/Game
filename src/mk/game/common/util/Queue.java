package mk.game.common.util;

import java.io.Serializable;

/**
 * @author MK
 *
 */

public class Queue <T> implements Serializable // Linked List implementation. Unsynchronized - see BlockingQueue
{
	private static final long serialVersionUID = -5997115059232511394L;
	private Node<T> head ; // 1st
	private Node<T> tail ; // Last

	public Queue()
	{
		super();
	}
	
	public boolean isEmpty()
	{
		return head == null ;
	}

	public void add(T obj)
	{
		if (isEmpty()) // Initialize
		{
			head = tail = new Node<T>(obj) ;
		}
		else // Add at the end
		{
			Node<T> node = new Node<T>(obj) ;
			tail.setNext(node) ;
			tail = node;
		}
	}

	public T poll() // Read 1st element and remove it.
	{
		T obj = null ;
		if (head != null)
		{
			obj = head.getElement() ;
			if (head == tail)
			{
				head = tail = null ;
			}
			else
			{
				head = head.getNext() ;
			}
		}
		return obj ;
	}

	public Node<T> read(int depth)
	{
		return head ;
	}

	public Node<T> read()
	{
		return read(0) ;
	}

	public void reverse()
	{
		reverse(head) ;
		Node<T> node = head ;
		head = tail ;
		tail = node;
	}

	public void reverse(Node<T> node)
	{
		if (node != null)
		{
			Node<T> next = node.getNext() ;
			if (next != null)
			{
				if (next.getNext() != null)
				{
					reverse(next) ;
				}
				node.setNext(null) ;
				next.setNext(node) ;
			}
		}
	}

	public String toString()
	{
		return "[" + (head == null ? "" : head) + "]" ;
	}
}
