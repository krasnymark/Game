/*
 * Created on Mar 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.stones.view;

import mk.game.common.model.IGame;
import mk.game.common.model.IGameMove;
import mk.game.common.model.IGameState;
import mk.game.stones.model.StonesGame;
import mk.game.stones.model.StonesGameMove;
import mk.game.stones.model.StonesGameState;
import mk.game.common.model.Game;
import mk.game.common.view.GameMenu;
import mk.game.common.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * @author mk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StonesView extends GameView implements ActionListener
{
	private static final int cellSize = 64;
	private static final Color myBackgroundColor = Color.lightGray;
    private int rows[]; // Holds Current # of mk.game.stones in each row
    private int moveRow; // Row selected for the move
    private int moveCount; // # of mk.game.stones selected
	private StonesButton[] stones;
    private Button removeButton = new Button("Remove");
    private Label fillTheBlanks = new Label();

	public StonesView(StonesGame game, RootPaneContainer container)
	{
		super(game, container);
		initGame();
	}

	public StonesView(StonesGame game)
	{
		this(game, null);
	}

	public void initGame()
	{
//        int i = 0;
//        for (int row = 0; row < getRows().length; row++)
//        for (int col = 0; col < getRows()[row]; col++)
//        {
//            mk.game.stones[i++] = new StonesButton(row);
//        }
		super.initGame();
//        resetMove();
	}

    private StonesButton[] getStones()
    {
        if (stones == null)
        {
            stones = new StonesButton[getStartingCount()];
        }
        return stones;
    }

    private void resetMove()
    {
        moveRow = -1;
        moveCount = 0;
        removeButton.setEnabled(false);
        for (int i = 0; i < stones.length; i++)
        {
            stones[i].setEnabled(stones[i].isVisible());
            stones[i].setSelected(false);
        }
    }
	
	public GameMenu getGameMenu(Game game)
	{
		return (GameMenu) (new StonesMenu((StonesGame) game));
	}
	public JPanel getGamePanel()
	{
		JPanel gPanel = new JPanel(); // Game Panel
		int ySize = getRows().length + 1;
        int[] rows = Arrays.copyOf(getRows(), getRows().length);
        Arrays.sort(rows);
        int xSize = rows[rows.length - 1];
        gPanel.setLayout(new GridLayout(ySize, 1));
		int boardXSize = cellSize * xSize;
		int boardYSize = cellSize * ySize;
		gPanel.setPreferredSize(new Dimension(boardXSize, boardYSize));
        getStones();
		MouseListener ml = new MouseAdapter()
		{
            @Override
            public void mouseEntered(MouseEvent e)
            {
                togglePreSelected(e, true);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                togglePreSelected(e, false);
            }

            private void togglePreSelected(MouseEvent e, boolean hilite)
            {
                StonesButton stonesButton = (StonesButton) e.getSource();
                if (stonesButton.isEnabled())
                {
                    stonesButton.setPreSelected(hilite);
                    stonesButton.repaint();
                }
            }
		};
        int i = 0;
        for (int row = 0; row < rows.length; row++)
        {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1, xSize));
            for (int col = 0; col < (xSize - rows[row]) / 2; col++)
            {
                rowPanel.add(new JLabel());
            }
            for (int col = 0; col < rows[row]; col++)
            {
                StonesButton stonesButton = new StonesButton(row);
                rowPanel.add(stonesButton);
                stonesButton.addMouseListener(ml);
                stonesButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        selectStone(e.getSource());
                    }

                });
                stones[i++] = stonesButton;
            }
            for (int col = 0; col < (xSize - rows[row]) / 2; col++)
            {
                rowPanel.add(new JLabel());
            }
            gPanel.add(rowPanel);
        }
		// Game Move Button
        JPanel moveButtonPanel = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        moveButtonPanel.setLayout(new BorderLayout(10, 10));
        moveButtonPanel.add(removeButton, BorderLayout.CENTER);
        moveButtonPanel.add(new JLabel(), BorderLayout.NORTH);
        moveButtonPanel.add(new JLabel(), BorderLayout.SOUTH);
        moveButtonPanel.add(new JLabel(), BorderLayout.EAST);
        moveButtonPanel.add(new JLabel(), BorderLayout.WEST);
        gPanel.add(moveButtonPanel);
        removeButton.addActionListener(this);
		return gPanel;
	}

    @Override
    public GameMenu getGameMenu(IGame game)
    {
        return (GameMenu) (new StonesMenu((StonesGame) game)) ;
    }

    private void selectStone(Object source)
    {
        StonesButton stonesButton = (StonesButton) source;
        stonesButton.setPreSelected(false);
        stonesButton.toggleSelected();
        if (stonesButton.isSelected())
        {
            moveCount++;
        }
        else
        {
            moveCount--;
        }
        moveRow = moveCount == 0 ? -1 : stonesButton.getRow();
        for (int i = 0; i < stones.length; i++)
        {
            stones[i].setEnabled(moveRow < 0 || stones[i].getRow() == moveRow);
            stones[i].repaint();
        }
        removeButton.setEnabled(moveCount > 0);
    }

	public void actionPerformed(ActionEvent e)
	{
		StonesGame stonesGame = (StonesGame) game;
        game.makeMove(new StonesGameMove(moveRow, moveCount));
	}

	public void showState(IGameState aState)
	{
		StonesGameState state = (StonesGameState) aState;
        for (int i = 0; i < stones.length; i++)
        {
            stones[i].repaint();
        }
        super.showState(state);
	}

	public void applyMove(IGameMove aMove, int player)
	{
        if (moveCount > 0) // Visual move
        {
            for (int i = 0; i < stones.length; i++)
            {
                if (stones[i].isVisible() && stones[i].isSelected())
                {
                    stones[i].setVisible(false);
                    stones[i].repaint();
                }
            }
        }
        else // Internal CPU move
        {
            StonesGameMove move = (StonesGameMove) aMove;
            int count = 0;
            for (int i = 0; i < stones.length; i++)
            {
                if (stones[i].isVisible() && stones[i].getRow() == move.getRow())
                {
                    count++;
                    if (count > move.getCount()) break;
                    stones[i].setVisible(false);
                    stones[i].repaint();
                }
            }
        }
		moveListTableUpdate();
        resetMove();
	}

	public void takeBack(IGameMove aMove)
	{
        StonesGameMove move = (StonesGameMove) aMove;
        int count = 0;
        for (int i = 0; i < stones.length; i++)
        {
            if (!stones[i].isVisible() && stones[i].getRow() == move.getRow())
            {
                count++;
                if (count > move.getCount()) break;
                stones[i].setVisible(true);
                stones[i].repaint();
            }
        }
        resetMove();
	}

    public int[] getRows() // Current # of mk.game.stones in each row
    {
        StonesGameState stonesGameState = (StonesGameState) game.getState();
        return stonesGameState.getRows();
    }

    public int getStartingCount()
    {
        StonesGameState stonesGameState = (StonesGameState) game.getState();
        return stonesGameState.getStartingCount();
    }

    public void reset()
    {
        for (int i = 0; i < stones.length; i++)
        {
            stones[i].setEnabled(true);
            stones[i].setVisible(true);
            stones[i].setSelected(false);
        }
    }
}
