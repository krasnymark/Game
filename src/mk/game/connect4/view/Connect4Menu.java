/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.view;

import mk.game.common.view.*;
import mk.game.connect4.model.Connect4Game;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Connect4Menu extends GameMenu // implements ActionListener
{
	public Connect4Menu(Connect4Game game)
	{
		super(game);
	}

	public GameSettingsView getSettings()
	{
		return new Connect4SettingsView(getGame()) ;
	}
}
