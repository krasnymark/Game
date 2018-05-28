package mk.game.common.model;

import mk.game.common.view.*;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/27/12
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IGame
{
    void exit();

    String getRules();

    void setPlayerLevel(Game.PlayerLevel level);

    int getPlayerLevels();

    void help();

    void startNewGame();

    IGameMove getFirstMove();

    void init();

    void init(RootPaneContainer parent);

    String getName();

    void showWinner();

    boolean isDrawIfNoMoves();

    GameMoveManager getGameMoveManager();

    IGameState getGameState();

    GameSettings getGameSettings();

    IGameView getGameView();

    IGameView getGameView(RootPaneContainer parent);

    void showHint(IGameMove aMove, int player);

    void showHint(IGameMove aMove);

    void applySettings();

    void restart();

    void applyMove(IGameMove move, int player);

    void takeBack();

    void replay();

    void hint(int player);

    IGameState getState();

    GameMoveManager getManager();

    IGameView getView();

    GameSettings getSettings();

    void save();

    int getDepth();

    int getDepth(int player);

    void waitShow();

    boolean isPaused();

    void setPlayerReady(boolean b);

    void waitHide();

    boolean isGameOver();

    IGameMove getLastMove();

    void print(String s);

    void printU(String s);

    void pause();

    void goFirst();

    void moveNow();

    void hint();

    IGamePlayer getGamePlayer(int player);

    int getProcessorCount();

    IGamePlayer[] getGamePlayer();

    void showRules();

    boolean isInitialized();

    void showGame();
}
