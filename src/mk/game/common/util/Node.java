package mk.game.common.util;

import java.io.Serializable;

/**
 * @author MK
 *
 */

public class Node<T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4545944958511250826L;
	private T element ;
	private Node<T> next ; // one-way link

	public Node(T element, Node<T> next)
	{
		super();
		this.element = element;
		this.next = next;
	}

	public Node(T element)
	{
		this(element, null);
	}

	public T getElement()
	{
		return element;
	}

	public void setElement(T element)
	{
		this.element = element;
	}

	public Node<T> getNext()
	{
		return next;
	}

	public void setNext(Node<T> next)
	{
		this.next = next;
	}

	public String toString()
	{
		return element.toString() + (next == null ? "" : ", " + next) ; 
	}
}
