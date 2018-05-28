/*
 * Created on May 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.view;

import mk.game.common.model.*;
import mk.game.common.model.Game.TimeType;
import mk.game.common.model.Game.PlayerType;
import mk.game.common.model.Game.PlayerLevel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * @author mk
 *
 */
public abstract class GameSettingsView extends JPanel
{
	protected IGame game;
	protected GameSettings settings ;
	protected JFrame frame ;
	protected JDialog dialog ;

	private IGamePlayer[] gamePlayer;
	private SpinnerNumberModel processorCountModel ;
	private JCheckBox thinkAhead ;

	private Dimension myFieldSize = new Dimension(100,20) ;
	private Dimension mySpinnerSize = new Dimension(30,20) ;

	public GameSettingsView(IGame game)
	{
		super();
		setGame(game);
		settings = game.getSettings() ;
		gamePlayer = game.getGamePlayer() ;

		//Make sure we have nice window decorations.
//		JFrame.setDefaultLookAndFeelDecorated(true);
		//Create and set up the window.
		frame = new JFrame("Game Settings");
		dialog = new JDialog(frame, "Game Settings", true);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
		//Create and ...
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel contentPane = new JPanel() ;
		//... set up the content pane.
		contentPane.setLayout(new BorderLayout()) ;
		contentPane.setOpaque(true); //content panes must be opaque
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		Icon icon = null ;
		tabbedPane.addTab("General", icon, getGeneralPanel(), "General Settings");
		JPanel settingsPanel = getSettingsPanel() ;
		GameView.setLargerFont(settingsPanel) ;
		tabbedPane.addTab(game.getName(), icon, settingsPanel, game.getName() + " Settings");
		dialog.setContentPane(contentPane);
		//Display the window.
		dialog.pack();
		dialog.setVisible(true);
	}

	public abstract JPanel getSettingsPanel() ;

	public void applySettings()
	{
		settings.setProcessorCount(processorCountModel.getNumber().intValue()) ;
		settings.setThinkAhead(thinkAhead.isSelected()) ;
		game.applySettings() ;
		game.restart() ;
	}

	public JPanel getPlayerPanel(int player)
	{
		JPanel playerPanel = new JPanel() ;
		playerPanel.setLayout(new GridBagLayout()) ;
		GridBagConstraints gbc = new GridBagConstraints() ;
		int pad = 10 ;
		gbc.gridwidth = 1 ;
		gbc.gridheight = 1 ;
		gbc.weightx = 1 ;
		gbc.weighty = 1 ;
		gbc.ipadx = 5 ;
		gbc.ipady = 1 ;
		gbc.insets = new Insets(pad,pad,pad,pad) ;
		gbc.fill = GridBagConstraints.NONE ;

		gbc.gridx = 0 ;
		gbc.gridy = 0 ;
		JLabel playerLabel = new JLabel("Player: " + player ) ;
		playerPanel.add(playerLabel, gbc) ;

		gbc.gridx = 0 ;
		gbc.gridy = 1 ;
		JLabel playerTypeLabel = new JLabel("Type:") ;
		JComboBox playerType = new JComboBox(PlayerType.values()) ;
		playerType.setSelectedItem(game.getGamePlayer(player).getPlayerType()) ;
		playerPanel.add(playerTypeLabel, gbc) ;
		gbc.gridx = 1 ;
		playerType.setPreferredSize(myFieldSize) ;
		playerPanel.add(playerType, gbc) ;
		playerType.addActionListener(EventHandler.create
		(ActionListener.class, gamePlayer[player], "playerType", "source.selectedItem")) ;

		gbc.gridx = 0 ;
		gbc.gridy = 2 ;
		JLabel playerLevelLabel = new JLabel("Level:") ;
		JComboBox playerLevel = new JComboBox(PlayerLevel.values());
        playerLevel.setSelectedItem(game.getGamePlayer(player).getPlayerLevel()) ;
		playerPanel.add(playerLevelLabel, gbc) ;
		gbc.gridx = 1;
        playerLevel.setPreferredSize(myFieldSize);
        playerPanel.add(playerLevel, gbc) ;
		playerLevel.addActionListener(EventHandler.create
                (ActionListener.class, gamePlayer[player], "playerLevel", "source.selectedItem")) ;

		gbc.gridx = 0 ;
		gbc.gridy = 3 ;
		JLabel playerNameLabel = new JLabel("Name:") ;
		JTextField playerName = new JTextField(game.getGamePlayer(player).getName()) ;
		playerPanel.add(playerNameLabel, gbc) ;
        gbc.gridx = 1;
		playerName.setPreferredSize(myFieldSize) ;
		playerPanel.add(playerName, gbc) ;
		playerName.addActionListener(EventHandler.create
                (ActionListener.class, gamePlayer[player], "playerName", "source.text")) ;

		gbc.gridx = 0 ;
		gbc.gridy = 4 ;
		JLabel playerTimeLabel = new JLabel("Time:") ;
		SpinnerModel playerTimeModel = new SpinnerNumberModel(game.getGamePlayer(player).getTimeLimit(), 0, 150, 1); 
		JSpinner playerTime = new JSpinner(playerTimeModel) ;
		playerTime.getModel().setValue(game.getGamePlayer(player).getTimeLimit()) ;
		playerTime.setPreferredSize(mySpinnerSize) ;
		playerPanel.add(playerTimeLabel, gbc) ;
		gbc.gridx = 1 ;
		gbc.anchor = GridBagConstraints.WEST ;
		playerPanel.add(playerTime, gbc) ;
		playerTime.addChangeListener(EventHandler.create
				(ChangeListener.class, gamePlayer[player], "timeLimit", "source.selectedItem")) ;

		return playerPanel ;
	}

	public JPanel getGeneralPanel()
	{
		JPanel generalPanel = new JPanel() ;
		
		JLabel clockTypeLabel = new JLabel("Time:") ;
		JRadioButton clockTypeFixed = new JRadioButton(TimeType.Fixed.toString()) ;
		JRadioButton clockTypeUnlimited = new JRadioButton(TimeType.Unlimited.toString()) ;
		ButtonGroup clockTypeGroup = new ButtonGroup() ;
		clockTypeGroup.add(clockTypeFixed) ;
		clockTypeGroup.add(clockTypeUnlimited) ;

		processorCountModel = new SpinnerNumberModel(game.getProcessorCount(), 1, 8, 1); 
		JSpinner processorCount = new JSpinner(processorCountModel) ;
		JLabel processorCountLabel = new JLabel("Processor Count") ;
		processorCount.getModel().setValue(game.getProcessorCount()) ;
		processorCount.setPreferredSize(mySpinnerSize) ;

		thinkAhead = new JCheckBox("Think Ahead") ;
		thinkAhead.setSelected(settings.isThinkAhead()) ;

		generalPanel.setLayout(new GridBagLayout()) ;
		GridBagConstraints gbc = new GridBagConstraints() ;

		int pad = 10 ;
		gbc.gridwidth = 1 ;
		gbc.gridheight = 1 ;
		gbc.weightx = 1 ;
		gbc.weighty = 1 ;
		gbc.ipadx = 5 ;
		gbc.ipady = 1 ;
		gbc.insets = new Insets(pad,pad,pad,pad) ;
		gbc.fill = GridBagConstraints.NONE ;
		gbc.gridx = 0 ;
		gbc.gridy = 0 ;
		generalPanel.add(getPlayerPanel(1), gbc) ;
		gbc.gridx = 1 ;
		generalPanel.add(getPlayerPanel(2), gbc) ;

		gbc.gridy = 1 ;
		gbc.gridx = 0 ;
		gbc.anchor = GridBagConstraints.EAST ;
		generalPanel.add(processorCountLabel, gbc) ;
		gbc.gridy = 2 ;
		generalPanel.add(clockTypeLabel, gbc) ;

		gbc.gridx = 1 ;
		gbc.gridy = 1 ;
		gbc.anchor = GridBagConstraints.WEST ;
		generalPanel.add(processorCount, gbc) ;
		gbc.gridy = 2 ;
		generalPanel.add(clockTypeFixed, gbc) ;
		gbc.gridy = 3 ;
		generalPanel.add(clockTypeUnlimited, gbc) ;
		gbc.gridy = 4 ;
		generalPanel.add(thinkAhead, gbc) ;

		return generalPanel ;
	}

	public JPanel getButtonPanel()
	{
		JButton okButton = new JButton("OK") ;
		JButton applyButton = new JButton("Apply") ;
		JButton cancelButton = new JButton("Cancel") ;
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				close() ;
				applySettings() ;
			}
		}) ;
		applyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				apply() ;
			}
		}) ;
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				close() ;
			}
		}) ;
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(okButton) ;
		buttonPanel.add(cancelButton) ;
		buttonPanel.add(applyButton) ;
		return buttonPanel ;
	}
	
	public void show()
	{
	}

	/**
	 * @return
	 */
	public GameSettings getSettings()
	{
		return game.getSettings() ;
	}

	/**
	 * @return
	 */
	public IGame getGame()
	{
		return game;
	}

	/**
	 * @param game
	 */
	public void setGame(IGame game)
	{
		this.game = game;
	}
	
	public void apply()
	{
		applySettings() ;
		dialog.setVisible(true) ;
	}
	
	public void close()
	{
		dialog.setVisible(false) ;
		dialog.dispose() ;
	}

}
