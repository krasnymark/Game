/**
 * GameView
 *
 * GameView represents an abstract GUI of a mk.game.
 * It provides a default implementation of a TimeMovePanel.
 * that has players' times and move list.
 */
package mk.game.common.view;

import mk.game.common.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * @author mk
 */
public abstract class GameView extends JPanel implements IGameView, ActionListener
{
	private static Font largerFont = new Font("Courier New", Font.BOLD, 20);
	protected Game game;
	private GameMenu menu;
    private JPanel timeMovePanel = new JPanel();
	private JPanel statusPanel = new JPanel(); // Status List
	private static Color[] playerColors;
	private static final Color myBackgroundColor = Color.lightGray;
	private TableModel moveListModel;
	private JTable moveListTable;
	private RootPaneContainer parentContainer;

	/**
	 * 
	 */
	public GameView(Game game)
	{
		this(game, null);
	}

	/**
	 * 
	 */
	public GameView(Game game, RootPaneContainer container)
	{
		super();
		setGame(game);
		setParentContainer(container);
		setMenu(getGameMenu(game));
	}

	@Override
    public void initGame()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 3;
		gbc.weighty = 3;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel gamePanel = getGamePanel();
		this.add(gamePanel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		JPanel movePanel = getTimeMovePanel();
        movePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(movePanel, gbc);

		getParentContainer().getContentPane().removeAll();
		getParentContainer().getContentPane().add(this);
		try
		{
			JFrame frame = (JFrame) getParentContainer();
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle(game.getName());
			frame.setJMenuBar(getMenu().createMenuBar());
			frame.getContentPane().add(this);
			frame.pack();
			frame.setVisible(true);
		}
		catch (Exception e)
		{
		}
	}

    @Override
    public void showGame()
    {
        JFrame frame = (JFrame) getParentContainer();
        frame.setVisible(true);
        frame.toFront();
    }

    /* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public abstract void actionPerformed(ActionEvent event);

    @Override
    public void hint(IGameMove move)
    {
        try
        {
            for (int i = 0; i < 2; i++)
            {
                int player = game.getWhoMovesNext();
                applyMove(move, player);
                Thread.sleep(200);
                takeBack(move);
                Thread.sleep(200);
            }
        }
        catch (InterruptedException e)
        {
        }
    }

	public void dispose()
	{
		removeAll();
//		frame.dispose();
	}

	private JPanel getTimeMovePanel()
	{
		timeMovePanel = new JPanel();
		JLabel playerLevel = new JLabel(game.getPlayerLevel().toString());
		String initTime = "00:00";
		JLabel playerTimer1 = new JLabel(initTime);
		JLabel playerTimer2 = new JLabel(initTime);
		game.getGameTimer(1).addChangeListener
		(
			EventHandler.create(ChangeListener.class, playerTimer1, "text", "source.timeElapsed")
		);
		game.getGameTimer(2).addChangeListener
		(
			EventHandler.create(ChangeListener.class, playerTimer2, "text", "source.timeElapsed")
		);
		JButton playerImage1 = new JButton();
		JButton playerImage2 = new JButton();
		playerImage1.setPreferredSize(new Dimension(20, 20));
		playerImage2.setPreferredSize(new Dimension(20, 20));
		playerImage1.setBackground(getPlayerColor(GameMoveManager.PLAYER_ONE));
		playerImage2.setBackground(getPlayerColor(GameMoveManager.PLAYER_TWO));
		JPanel timePanel = new JPanel();
		timePanel.add(new JLabel("Time:"));
		timePanel.add(playerImage1);
		timePanel.add(playerTimer1);
		timePanel.add(playerImage2);
		timePanel.add(playerTimer2);
		statusPanel.add(playerLevel);

		moveListModel = new AbstractTableModel() 
		{
			public int getColumnCount()
			{
				return 3;
			}
			public int getRowCount()
			{
				return game.getMoveNumber();
			}
			public Object getValueAt(int row, int col)
			{
				if (col == 0) return row + 1; // Move Number
				return game.getMove(row * 2 + col - game.getWhoIsFirst());
			}
		};

		TableCellRenderer headerRenderer = new DefaultTableCellRenderer() 
		{
			public Component getTableCellRendererComponent
				(JTable t, Object value, boolean selected, boolean focussed, int row, int col) 
			{
				if (col > 0)
				{
					setBackground(getPlayerColor(col)); 
					setForeground(getPlayerColor(col)); 
				} 
				return super.getTableCellRendererComponent(t, value, selected, focussed, row, col);
			}
		};

		TableCellRenderer tableRenderer = new DefaultTableCellRenderer() 
		{
			public Component getTableCellRendererComponent
				(JTable t, Object value, boolean selected, boolean focussed, int row, int col) 
			{
				setBackground(myBackgroundColor); 
				return super.getTableCellRendererComponent(t, value, selected, focussed, row, col);
			}
		};
		moveListTable = new JTable(moveListModel)
		{
			public String getToolTipText(MouseEvent e)
			{
				String tip = null;
				Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				if (col > 0 && row >=0)
				{
					IGameMove move = (IGameMove) getValueAt(row, col);
					if (move != null) tip = (row+1) + " - " + move.getDescr();
				}
				return tip;
			}
		};

		moveListTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		moveListTable.getColumnModel().getColumn(1).setPreferredWidth(75);
		moveListTable.getColumnModel().getColumn(2).setPreferredWidth(75);
		moveListTable.getColumnModel().getColumn(0).setHeaderValue("Move");
		moveListTable.getColumnModel().getColumn(1).setHeaderValue(playerImage1);
		moveListTable.getColumnModel().getColumn(2).setHeaderValue(playerImage2);
		moveListTable.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
		moveListTable.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);

		moveListTable.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
		moveListTable.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
		moveListTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
		moveListTable.getColumnModel().setColumnSelectionAllowed(false);

//		moveListTable.setShowGrid(false);
		moveListTable.setGridColor(myBackgroundColor);
		moveListTable.setRowSelectionAllowed(false);
		moveListTable.setCellSelectionEnabled(false);
		moveListTable.setColumnSelectionAllowed(false);

		JScrollPane scrollPane = new JScrollPane(moveListTable);
		timeMovePanel.setLayout(new BorderLayout());
		timeMovePanel.add(timePanel, BorderLayout.NORTH);
		timeMovePanel.add(scrollPane, BorderLayout.CENTER);
//		timeMovePanel.add(statusPanel, BorderLayout.SOUTH);
		timeMovePanel.setBackground(myBackgroundColor);
		timeMovePanel.setPreferredSize(new Dimension(180, 400));
        setPlayerLevel(game.getPlayerLevel()) ;
		return timeMovePanel;
	}

    public void showState(IGameState state)
    {
        JFrame frame = (JFrame) getParentContainer();
        if (!frame.isVisible())
        {
			frame.setVisible(true);
			frame.toFront();
        }
        moveListTableUpdate() ;
    }

    public void exit()
    {
        ((Window) getParentContainer()).dispose();
    }

    @Override
    public void setPlayerLevel(Game.PlayerLevel newLevel)
    {
        JLabel playerLevel = new JLabel(game.getPlayerLevel().toString()) ;
        statusPanel.removeAll();
        statusPanel.add(playerLevel);
        statusPanel.repaint();
        timeMovePanel.remove(statusPanel);
        timeMovePanel.add(statusPanel, BorderLayout.SOUTH) ;
    }

	protected void moveListTableUpdate()
	{
		moveListTable.tableChanged(new TableModelEvent(moveListModel));
	}

    public Game getGame()
	{
		return game;
	}

    public void setGame(Game game)
	{
		this.game = game;
	}

	public static Color[] getPlayerColors()
	{
		if (playerColors == null) // Initialize
		{
			playerColors = new Color[3];   // index = value
			playerColors[0] = Color.white; // Free
			playerColors[1] = Color.red;   // Human
			playerColors[2] = Color.black; // Computer
		}
		return playerColors;
	}

	private static Color getPlayerColor(int player)
	{
		return getPlayerColors()[player];
	}

	@Override
    public GameMenu getMenu()
	{
		return menu;
	}

	/**
	 * @param menu
	 */
	@Override
    public void setMenu(GameMenu menu)
	{
		this.menu = menu;
	}


	public RootPaneContainer getParentContainer()
	{
		if (parentContainer == null) this.parentContainer =  new JFrame();
		return parentContainer;
	}

	private void setParentContainer(RootPaneContainer container)
	{
		parentContainer = container;
	}

	public static void setPlayerColors(Color[] playerColors)
	{
		GameView.playerColors = playerColors;
	}

	private static void setFont(Container container, Font font)
	{
		for (int i = 0; i < container.getComponentCount(); i++)
		{
			container.getComponent(i).setFont(font);
		}
	}

	public static void setLargerFont(Container container)
	{
		setFont(container, largerFont);
	}

    public void showInfo(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
