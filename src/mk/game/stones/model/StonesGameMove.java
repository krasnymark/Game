package mk.game.stones.model;

import mk.game.common.model.GameMove;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/10/12
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class StonesGameMove extends GameMove
{
    private int row;
    private int count;

    public StonesGameMove(int row, int count)
    {
        setRow(row);
        setCount(count);
    }

    public int getRow()
    {
        return row;
    }
    public void setRow(int row)
    {
        this.row = row;
    }

    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return "(" + (row + 1) + "," + (count) + ")" + super.toString();
    }
}
