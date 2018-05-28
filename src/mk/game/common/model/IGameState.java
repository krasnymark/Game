package mk.game.common.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/17/12
 * Time: 11:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IGameState
{
    int getValue();

    void setValue(int i);

    void init();

    public IGameState clone();

    IGameState applyMove(IGameMove move, int player) ;

    IGameState takeBack(IGameMove move, int player) ;

    boolean isPossibleMove(IGameMove aMove) ;

    List<IGameMove> getPossibleMoves(int player) ;

    /*
* (non-Javadoc)
*
* @see mk.game.common.model.GameState#evaluate(int, boolean)
*/
    int evaluate(int player);

    int evaluate(int player, boolean checkWin) ;

    /**
     * @return: Was move a winning one for the player
     */
    boolean isMoveWin(IGameMove move, int player) ;

    boolean isLastMoveWin();

    IGameMove getLastMove();

    void setLastMove(IGameMove move);

    Game getGame();

    IGameState simplify();
}
