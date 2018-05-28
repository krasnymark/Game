/*
 * Created on Mar 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.stones.model;

import mk.game.common.model.Game;
import mk.game.common.model.GameState;
import mk.game.common.model.IGameMove;
import mk.game.common.model.IGameState;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mk
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StonesGameState extends GameState implements Cloneable
{
    private int rowCount; // # of rows
    private int rows[]; // Holds Current # of mk.game.stones in each row
    private int startingCount; // Initial Total # of mk.game.stones
    private int count; // Total # of mk.game.stones. 0 = Game over

    public StonesGameState(StonesGameState aState)
    {
        this(aState.getGame(), aState.getRowCount());
    }

    public StonesGameState(Game game, int[] startingRows)
    {
        super(game);
        init(startingRows);
    }

    public StonesGameState(Game game, int rowCount)
    {
        super(game);
        init(rowCount);
    }

    public StonesGameState(Game game)
    {
        this(game, StonesGame.defRowCount);
    }

    public void init(int[] startingRows)
    {
        super.init();
        rowCount = startingRows.length;
        rows = new int[rowCount];
        count = 0;
        for (int i = 0; i < rowCount; i++)
        {
            rows[i] = startingRows[i];
            count += rows[i];
        }
        startingCount = count;
    }

    public void init(int rowCount)
    {
        super.init();
        int[] startingRows = new int[rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            startingRows[i] = i * 2 + 1;
        }
        init(startingRows);
    }

    public void init()
    {
        super.init();
        int[] startingRows = getStartingRows();
        if (startingRows == null)
        {
            startingRows = new int[rowCount];
            for (int i = 0; i < rowCount; i++)
            {
                startingRows[i] = i * 2 + 1;
            }
        }
        else
        {
            rowCount = startingRows.length;
        }
        rows = new int[rowCount];
        count = 0;
        for (int i = 0; i < rowCount; i++)
        {
            rows[i] = startingRows[i];
            count += rows[i];
        }
        startingCount = count;
    }

    public StonesGameState clone()
    {
        StonesGameState clone = new StonesGameState(this); // (StonesGameState) super.clone();
        clone.setRows(Arrays.copyOf(getRows(), rowCount));
        clone.setRowCount(rowCount);
        clone.setCount(count);
        return clone;
    }

    /**
     * @return: Was move a winning one for the player
     */
    public boolean isMoveWin(IGameMove aMove, int player)
    {
        return isWin();
    }

    private boolean isWin()
    {
        return count == 0;
    }

    public int evaluate(int player, boolean checkWin)
    {
        int rc = 0; // Non empty row count
        List<Integer> eqRows = getEqRows();
        for (int row1 = 0; row1 < rowCount; row1++)
        {
            if (rows[row1] > 0) rc++;
        }
        if (checkWin && isWin())
        {
            game.setWinner(player);
        }
        value = 0;
//        value = isWin() ? 10000 : ((rc - eq * 2) % 2 == 0 ? 1000 : 0);
        int ur = rc - eqRows.size(); // Unique rows
        value += isWin() ? 10000 : 0;
        value += ur == 0 ? 1000 : (ur < 3 ? -1000 : 0);
        value += rc * 100;
        value += count;
        return value;
    }

    @Override
    public IGameState simplify()
    {
        List<Integer> eqRows = getEqRows();
        if (eqRows.size() == 0) return this;
        StonesGameState clone = this.clone();
        for (int row : eqRows) // Remove both if equal length
        {
            StonesGameMove move = new StonesGameMove(row, rows[row]);
            clone.applyMove(move);
        }
        return clone;
    }

    private List<Integer> getEqRows()
    {
        List<Integer> eqRows = new ArrayList<Integer>(); // Rows with equal count
        for (int row1 = 0; row1 < rowCount; row1++)
        {
            for (int row2 = row1 + 1; row2 < rowCount; row2++)
            {
                if (rows[row1] > 0 && rows[row1] == rows[row2])
                {
                    eqRows.add(row1);
                    eqRows.add(row2);
                    // not to be reused!
                    if (row2 == row1 + 1) row1 = row2;
                    break;
                }
            }
        }
        return eqRows;
    }

    public IGameState applyMove(StonesGameMove move)
    {
        rows[move.getRow()] -= move.getCount();
        if (rows[move.getRow()] < 0)
        {
            System.out.println("Consistency Error!");
        }
        count -= move.getCount();
        return this;
    }

    public IGameState applyMove(IGameMove aMove, int player)
    {
        StonesGameMove move = (StonesGameMove) aMove;
        return applyMove(move);
    }

    public IGameState takeBack(IGameMove aMove, int player)
    {
        StonesGameMove move = (StonesGameMove) aMove;
        rows[move.getRow()] += move.getCount();
        count += move.getCount();
        return this;
    }

    public boolean isPossibleMove(IGameMove aMove)
    {
        StonesGameMove move = (StonesGameMove) aMove;
        return move.getRow() < rowCount && move.getCount() <= rows[move.getRow()];
    }

    public List<IGameMove> getPossibleMoves(int player)
    {
        return getPossibleMoves();
    }

    public List<IGameMove> getPossibleMoves()
    {
        List<IGameMove> list = new ArrayList<IGameMove>();
        for (int row = 0; row < rowCount; row++)
        for (int count = 1; count <= rows[row]; count++)
        {
            list.add(new StonesGameMove(row, count));
        }
        return list;
    }

    public String toString()
    {
        String s = "State Value = " + getValue() + " Win: " + game.getWinner() + "\n";
        for (int row = 0; row < getRowCount(); row++)
        {
            s += StringUtils.repeat("* ", rows[row]) + "\n";
        }
        return s;
    }

    public int[] getStartingRows()
    {
        StonesGame stonesGame = (StonesGame) game;
        return stonesGame.getStartingRows();
    }

    public int getStartingCount()
    {
        return startingCount;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public int[] getRows()
    {
        return rows;
    }

    private void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

    private void setRows(int[] rows)
    {
        this.rows = rows;
    }

    private void setCount(int count)
    {
        this.count = count;
    }
}
