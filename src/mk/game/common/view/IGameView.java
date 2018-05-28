package mk.game.common.view;

import mk.game.common.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/17/12
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IGameView
{
    void initGame();

    JPanel getGamePanel();

    GameMenu getGameMenu(IGame game);

    void showState(IGameState state);

    void applyMove(IGameMove move, int player);

    void takeBack(IGameMove move);

    void hint(IGameMove move);

    void setPlayerLevel(Game.PlayerLevel newLevel);

    IGame getGame();

    GameMenu getMenu();

    void setMenu(GameMenu menu);

    void setCursor(Cursor predefinedCursor);

    void dispose();

    RootPaneContainer getParentContainer();

    boolean isVisible();

    void showInfo(String title, String message);

    void exit();

    void showGame();
}
