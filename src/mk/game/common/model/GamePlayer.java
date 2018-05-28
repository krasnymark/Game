package mk.game.common.model;

import mk.game.common.model.Game.PlayerType;
import mk.game.common.model.Game.PlayerLevel;

public class GamePlayer implements IGamePlayer
{
    private String name;
    private int timeLimit; // In minutes, 0 - unlimited - show elapsed instead of time left.
    private GameTimer timer = new GameTimer(this);
    private PlayerType playerType;
    private PlayerLevel playerLevel;

    public GamePlayer(String name)
    {
        this(name, PlayerType.Human);
    }

    public GamePlayer(String name, PlayerType playerType)
    {
        this(name, playerType, PlayerLevel.Beginner);
    }

    public GamePlayer(String name, PlayerType playerType, PlayerLevel playerLevel)
    {
        this(name, playerType, playerLevel, 0);
    }

    public GamePlayer(String name, PlayerType playerType, PlayerLevel playerLevel, int timeLimit)
    {
        setName(name);
        setPlayerType(playerType);
        setPlayerLevel(playerLevel);
        setTimeLimit(timeLimit);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isHuman()
    {
        return playerType == PlayerType.Human;
    }

    @Override
    public void setHuman(boolean isHuman)
    {
        playerType = isHuman ? PlayerType.Human : PlayerType.Computer;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int getTimeLimit()
    {
        return timeLimit;
    }

    @Override
    public void setTimeLimit(int timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public String toString()
    {
        return " Player: " + getName() + " " + timer + (isHuman() ? "" : " Level: " + getPlayerLevel());
    }

    @Override
    public GameTimer getTimer()
    {
        return timer;
    }

    @Override
    public void setTimer(GameTimer gameTimer)
    {
        this.timer = gameTimer;
    }

    @Override
    public void resetTimer()
    {
        this.timer.reset();
    }

    @Override
    public void startWatch()
    {
        timer.startWatch();
    }

    @Override
    public void stopWatch()
    {
        timer.stopWatch();
    }

    @Override
    public void resumeWatch()
    {
        timer.resumeWatch();
    }

    @Override
    public PlayerType getPlayerType()
    {
        return playerType;
    }

    @Override
    public void setPlayerType(PlayerType playerType)
    {
        this.playerType = playerType;
    }

    @Override
    public PlayerLevel getPlayerLevel()
    {
        return playerLevel;
    }

    @Override
    public void setPlayerLevel(PlayerLevel playerLevel)
    {
        this.playerLevel = playerLevel;
    }

}
