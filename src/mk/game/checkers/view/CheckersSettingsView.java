/*
 * Created on Jun 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.checkers.view;

import mk.game.common.model.Game;
import mk.game.common.model.IGame;
import mk.game.common.view.GameSettingsView;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mk.game.checkers.model.CheckersGameSettings;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckersSettingsView extends GameSettingsView
{
	private JRadioButton boardSize_64 ;
	private JRadioButton boardSize_100 ;
	private ButtonGroup boardSizeGroup ;

	/**
	 * 
	 */
	public CheckersSettingsView(IGame game)
	{
		super(game);
	}
	
	public JPanel getSettingsPanel()
	{
		CheckersGameSettings gameSettings = (CheckersGameSettings) getSettings() ;

		JPanel settingsPanel = new JPanel() ;
		settingsPanel.setLayout(new GridBagLayout()) ;
		GridBagConstraints gbc = new GridBagConstraints() ;
		
		JLabel gameType = new JLabel("Type:") ;
		JRadioButton gameTypeRegular = new JRadioButton("Regular") ;
		JRadioButton gameTypeSuicide = new JRadioButton("Suicide") ;
		ButtonGroup gameTypeGroup = new ButtonGroup() ;
		gameTypeGroup.add(gameTypeRegular) ;
		gameTypeGroup.add(gameTypeSuicide) ;
		gameTypeRegular.setSelected(!gameSettings.isSuicide()) ;
		gameTypeSuicide.setSelected( gameSettings.isSuicide()) ;
	
		JLabel boardSize = new JLabel("Board:") ;
		String _64 = "" + CheckersGameSettings.SQUARE_SIZE_64 ;
		String _100 = "" + CheckersGameSettings.SQUARE_SIZE_100 ;
		boardSize_64 = new JRadioButton(_64) ;
		boardSize_100 = new JRadioButton(_100) ;
		boardSize_64.setActionCommand(_64) ;
		boardSize_100.setActionCommand(_100) ;
		boardSizeGroup = new ButtonGroup() ;
		boardSizeGroup.add(boardSize_64) ;
		boardSizeGroup.add(boardSize_100) ;
		boardSize_64.setSelected(gameSettings.getBoardSize() == CheckersGameSettings.SQUARE_SIZE_64) ;
		boardSize_100.setSelected(gameSettings.getBoardSize() == CheckersGameSettings.SQUARE_SIZE_100) ;

		int pad = 10 ;
		gbc.gridx = 0 ;
		gbc.gridy = 0 ;
		gbc.gridwidth = 3 ;
		gbc.gridheight = 1 ;
		gbc.weightx = 1 ;
		gbc.weighty = 1 ;
		gbc.ipadx = 5 ;
		gbc.ipady = 1 ;
		gbc.insets = new Insets(pad,pad,pad,pad) ;
		gbc.fill = GridBagConstraints.BOTH ;

		gbc.gridwidth = 1 ;
		settingsPanel.add(gameType, gbc) ;
		gbc.gridx = 1 ;
		settingsPanel.add(gameTypeRegular, gbc) ;
		gbc.gridx = 2 ;
		settingsPanel.add(gameTypeSuicide, gbc) ;

		gbc.gridx = 0 ;
		gbc.gridy = 1 ;
		gbc.gridwidth = 1 ;
		settingsPanel.add(boardSize, gbc) ;
		gbc.gridx = 1 ;
		settingsPanel.add(boardSize_64, gbc) ;
		gbc.gridx = 2 ;
		settingsPanel.add(boardSize_100, gbc) ;

		gbc.gridx = 0 ;
		gbc.gridy = 2 ;
		gbc.gridwidth = 1 ;
//		settingsPanel.add(clockType, gbc) ;
//		gbc.gridx = 1 ;
//		settingsPanel.add(clockTypeFixed, gbc) ;
//		gbc.gridx = 2 ;
//		settingsPanel.add(clockTypeUnlimited, gbc) ;

		return settingsPanel ;
	}
	
	public void applySettings()
	{
		CheckersGameSettings gameSettings = (CheckersGameSettings) getSettings() ;
		gameSettings.setBoardSize((new Integer(boardSizeGroup.getSelection().getActionCommand())).intValue()) ;
		super.applySettings() ;
	}

}
