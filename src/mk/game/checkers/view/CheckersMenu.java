/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.view;

import mk.game.common.view.GameMenu;
import mk.game.common.view.GameSettingsView;
import mk.game.checkers.model.CheckersGame;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersMenu extends GameMenu // implements ActionListener
{
	public CheckersMenu(CheckersGame game)
	{
		super(game);
	}

	public GameSettingsView getSettings()
	{
		return new CheckersSettingsView(getGame()) ;
	}
}
