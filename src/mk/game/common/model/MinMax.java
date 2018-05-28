package mk.game.common.model;

import java.util.Iterator;
import java.util.List;

/**
 * MinMax
 * 
 * This class implements an extensible framework for the MinMax algorithm.
 *  
 */
public class MinMax
{
	private int[] count = new int[4];
	private GameMoveManager manager;
	private IGame game;
	private int maxValue;
	private int minValue;

	public MinMax(GameMoveManager aManager, int maxValue)
	{
		manager = aManager;
		game = manager.getGame();
		this.maxValue = maxValue;
		this.minValue = -maxValue;
		resetCount();
	}

	public void resetCount()
	{
		for (int i = 0; i < count.length; i++)
		{
			count[i] = 0;
		}
	}

//	/**
//	 * choseNextMove accepts a board cofiguration and a depth indicator. It
//	 * builds a tree of every possible move starting from the configuation to the
//	 * specified depth. It then uses the MinMax algorithm to choose the next move
//	 * the automated player should make. Note that depth is expressed in rounds.
//	 * I.e., a depth of one means that chooseNextMove builds a tree with one
//	 * computer move under the root node, and one player move under that.
//     * @param state    -
//     * @param depth    -
//     * @param player   -
//     * @return         -
//	 */
//	public GameMove getBestMove(GameState state, int depth, int player)
//	{
//		return findBestMove(state, depth * 2 + 1, player, minValue);
//	}

	/**
	 * Assuming a possible move was made continue with runTask() for parallel processing
	 * The state would be cloned and therefore could be discarded - still need to take back
     *
     * @param state    -
     * @param depth    -
     * @param player   -
     * @param testMove -
     * @return         -
     */
	public GameMove getBestMove(IGameState state, int depth, int player, IGameMove testMove)
	{
		IGameState testState = manager.applyMove(state, testMove, player);
		int opponent = GameMoveManager.getOpponent(player);
		GameMove bestMove = findBestMove(testState, depth * 2, opponent, testMove.getValue());
		testState = manager.takeBack(state, testMove, player);
		return bestMove;
	}

	/**
	 * Calls GameMoveManager.getPossibleMoves to get a List of possible
	 * moves. If it is at the bottom level of the search tree, it evaluates each
	 * move and tests the result against bestResultSoFar. Above the bottom level,
	 * it calls itself for the next level down.
     * @param state    -
     * @param depth    -
     * @param player   -
     * @param bestResultSoFar -
     * @return         -
	 */
	private GameMove findBestMove(IGameState state, int depth, int player, int bestResultSoFar)
	{
		GameMove bestSoFar = null;
		GameMove testMove = null;
		int bestValue = minValue;
		int opponent = GameMoveManager.getOpponent(player);
        // Some games allow state to be simplified
        IGameState testState = state.simplify();
//        boolean usingOriginalState = testState == state;

		List possibleMoves = manager.getPossibleMoves(testState, player);
		count[0]++;
		if (possibleMoves.size() == 1) depth += 1;

		boolean foundBest = false;
		for (Iterator i = possibleMoves.iterator(); i.hasNext();)
		{
			testMove = (GameMove) i.next();
			if (manager.isMoveNow())
			{
				game.print("MinMax - Exit requested: player = " + player + " depth = " + depth);
				break;
			}
//            GameState testState = mk.game.needToCloneState() ? state.clone() : state;
            manager.applyMove(testState, testMove, player);
			if (testState.isMoveWin(testMove, player))
			{
				testMove.value = maxValue;
				foundBest = true;
			}
			else if (depth == 0)
			{
				testMove.value = manager.evaluate(testState, player);
				count[2]++;
			}
			else
			{
				GameMove searchMove = findBestMove(testState, depth - 1, opponent, -bestValue);
				testMove.value = (searchMove == null) ? (game.isDrawIfNoMoves() ? 0 : maxValue) 
				                                      : -searchMove.value;
			}
			if (testMove.value > bestValue)
			{
				bestValue = testMove.value;
				bestSoFar = testMove;
			}
            testState = manager.takeBack(testState, testMove, player);
			count[1]++;
			if (bestValue >= bestResultSoFar && bestResultSoFar != minValue)
			{
				count[3]++;
				// Player found a better response than the bestResultSoFar
				// Opponent will keep the [prev] move when it was reached.
				foundBest = true;
			}
			if (foundBest || manager.isMoveNow()) break;
		}
		if (bestSoFar == null)
		{
			bestSoFar = testMove;
		}
		return bestSoFar;
	}
	public int[] getCount()
	{
		return count;
	}
}
