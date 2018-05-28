/*
 * Created on Jun 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.stones.view;

import mk.game.common.model.Game;
import mk.game.common.model.IGame;
import mk.game.common.view.GameSettingsView;
import mk.game.stones.model.StonesGame;

import javax.swing.*;
import java.awt.*;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StonesSettingsView extends GameSettingsView
{
	private JSpinner rowCount;
	private StonesGame thisGame;

	public StonesSettingsView(IGame game)
	{
		super(game);
	}
	
	public JPanel getSettingsPanel()
	{
		thisGame = (StonesGame) game;
		SpinnerModel rowCountModel = new SpinnerNumberModel(thisGame.getRowCount(), 3, 7, 1);

        JLabel rowCountLabel = new JLabel("Rows");
		rowCount = new JSpinner(rowCountModel);
		rowCount.getModel().setValue(thisGame.getRowCount());

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		int pad = 10;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.ipadx = 5;
		gbc.ipady = 1;
		gbc.insets = new Insets(pad,pad,pad,pad);
		gbc.fill = GridBagConstraints.NONE;

		gbc.gridy = 1;
		gbc.gridwidth = 1;
        gbc.gridx = 0;
		settingsPanel.add(rowCountLabel, gbc);
		gbc.gridx = 1;
		settingsPanel.add(rowCount, gbc);

		return settingsPanel;
	}

	public int getRowCount()
	{
		return (Integer) rowCount.getModel().getValue();
	}

    public void setRowCount(int newSize)
	{
		rowCount.getModel().setValue(newSize);
	}
	
	public void applySettings()
	{
		thisGame.setRowCount(getRowCount());
		super.applySettings();
	}

}
