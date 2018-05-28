/*
 * Created on Jul 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4;

import mk.game.common.model.Game;

import javax.swing.JApplet;

import mk.game.connect4.model.Connect4Game;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4Applet extends JApplet
{
	Game game ;
	
	public void init()
	{
		game = new Connect4Game(this) ;
		this.setJMenuBar(game.getView().getMenu().createMenuBar());
		game.startNewGame() ;
	}
	
	public void start()
	{
		game.startNewGame() ;
	}

}
