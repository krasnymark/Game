package mk.game.common.model;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/17/12
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IGamePlayer
{
    boolean isHuman();

    void setHuman(boolean isHuman);

    String getName();

    void setName(String name);

    int getTimeLimit();

    void setTimeLimit(int timeLimit);

    GameTimer getTimer();

    void setTimer(GameTimer gameTimer);

    void resetTimer();

    void startWatch();

    void stopWatch();

    void resumeWatch();

    Game.PlayerType getPlayerType();

    void setPlayerType(Game.PlayerType playerType);

    Game.PlayerLevel getPlayerLevel();

    void setPlayerLevel(Game.PlayerLevel playerLevel);
}
