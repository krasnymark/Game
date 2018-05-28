package mk.game.common.model;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/17/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IGameMove
{
    String getDescr();

    long getTime();

    void setTime(long time);

    int getValue();

    void setValue(int value);
}
