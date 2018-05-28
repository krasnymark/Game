/*
 * Created on Jun 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.connect4.view;

import mk.game.common.model.*;
import mk.game.common.view.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import mk.game.connect4.model.Connect4Game;

/**
 * @author mk
 */
public class Connect4SettingsView extends GameSettingsView
{
	private JSpinner xSize ;
	private JSpinner ySize ;
	private JSpinner connectSize ;
	private Connect4Game thisGame ;

	public Connect4SettingsView(IGame game)
	{
		super(game);
	}
	
	public JPanel getSettingsPanel()
	{
		thisGame = (Connect4Game) game ;
		SpinnerModel xModel = new SpinnerNumberModel(thisGame.getXSize(), 4, 9, 1); 
		SpinnerModel yModel = new SpinnerNumberModel(thisGame.getYSize(), 4, 9, 1); 
		SpinnerModel cModel = new SpinnerNumberModel(thisGame.getConnectSize(), 3, 5, 1); 

		xSize = new JSpinner(xModel) ;
		ySize = new JSpinner(yModel) ;
		connectSize = new JSpinner(cModel) ;
		JLabel gameDim = new JLabel("Dimensions") ;
		JLabel boardSize = new JLabel("Board Size") ;
		JLabel connectSizeLabel = new JLabel("Connect Size") ;
		this.setXSize(thisGame.getXSize()) ;
		ySize.getModel().setValue(new Integer(thisGame.getYSize())) ;
		connectSize.getModel().setValue(new Integer(thisGame.getConnectSize())) ;

		JPanel settingsPanel = new JPanel() ;
		settingsPanel.setLayout(new GridBagLayout()) ;
		GridBagConstraints gbc = new GridBagConstraints() ;

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
		gbc.fill = GridBagConstraints.NONE ;
//		settingsPanel.add(gameDim, gbc) ;

		gbc.gridy = 1 ;
		gbc.gridwidth = 1 ;
		settingsPanel.add(boardSize, gbc) ;
		gbc.gridx = 1 ;
		settingsPanel.add(xSize, gbc) ;
		gbc.gridx = 2 ;
		settingsPanel.add(ySize, gbc) ;

		gbc.gridy = 2 ;
		gbc.gridx = 0 ;
		settingsPanel.add(connectSizeLabel, gbc) ;
		gbc.gridx = 1 ;
		gbc.gridwidth = 1 ;
		settingsPanel.add(connectSize, gbc) ;

		return settingsPanel ;
	}

	public int getXSize()
	{
		return ((Integer) xSize.getModel().getValue()).intValue() ;
	}

	public void setXSize(int newSize)
	{
		xSize.getModel().setValue(new Integer(newSize)) ;
	}

	public int getYSize()
	{
		return ((Integer) ySize.getModel().getValue()).intValue() ;
	}

	public void setYSize(int newSize)
	{
		ySize.getModel().setValue(new Integer(newSize)) ;
	}

	public int getConnectSize()
	{
		return ((Integer) connectSize.getModel().getValue()).intValue() ;
	}

	public void setConnectSize(int newSize)
	{
		connectSize.getModel().setValue(new Integer(newSize)) ;
	}
	
	public void applySettings()
	{
		thisGame.setXSize(getXSize()) ;
		thisGame.setYSize(getYSize()) ;
		thisGame.setConnectSize(getConnectSize()) ;
		super.applySettings() ;
	}

}
