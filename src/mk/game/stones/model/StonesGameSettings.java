package mk.game.stones.model;

import mk.game.common.model.GameSettings;

public class StonesGameSettings extends GameSettings
{
    private int rowCount; // Optional # of rows to initialize the mk.game

    public int getRowCount()
    {
        return rowCount;
    }

    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }
}
