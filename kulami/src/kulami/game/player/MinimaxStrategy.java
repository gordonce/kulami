/**
 * 
 */
package kulami.game.player;

import java.util.List;

import kulami.game.board.GameMap;
import kulami.game.board.Pos;

/**
 * @author gordon
 * 
 */
public class MinimaxStrategy implements KulamiStrategy {

	/**
	 * Negative infinity is set to the number of points for all panels plus 1
	 */
	private static final int POSITIVE_INFINITY = 65;
	/**
	 * Negative infinity is set to the number of points for all panels plus 1
	 * negated
	 */
	private static final int NEGATIVE_INFINITY = -65;
	private int level;
	private Pos savedMove;

	public MinimaxStrategy(int level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kulami.game.player.KulamiStrategy#choosePos(kulami.game.board.GameMap)
	 */
	@Override
	public Pos choosePos(GameMap gameMap) {
		savedMove = null;
		int eval = max(1, level, gameMap);
		return savedMove;
	}

	private int max(int player, int depth, GameMap gameMap) {
		if (depth == 0 || !hasMarbles(player)) {
			return evaluate(gameMap);
		}
		int maxVal = NEGATIVE_INFINITY;
		List<Pos> possibleMoves = generatePossibleMoves(player, gameMap);
		for (Pos move: possibleMoves) {
			GameMap nextGameMap = performMove(player, move, gameMap);
			int value = min(-player, depth-1, nextGameMap);
			if (value > maxVal) {
				maxVal = value;
				if (depth == level)
					savedMove = move;
			}
		}
		return maxVal;
	}

	private int min(int player, int depth, GameMap gameMap) {
		if (depth == 0 || !hasMarbles(player)) {
			return evaluate(gameMap);
		}
		int minVal = POSITIVE_INFINITY;
		List<Pos> possibleMoves = generatePossibleMoves(player, gameMap);
		for (Pos move: possibleMoves) {
			GameMap nextGameMap = performMove(player, move, gameMap);
			int value = max(-player, depth-1, nextGameMap);
			if (value < minVal) {
				minVal = value;
				if (depth == level)
					savedMove = move;
			}
		}		return minVal;
	}

	/**
	 * @param move 
	 * @param player 
	 * @param gameMap
	 * @return
	 */
	private GameMap performMove(int player, Pos move, GameMap gameMap) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param player
	 * @param gameMap
	 * @return
	 */
	private List<Pos> generatePossibleMoves(int player, GameMap gameMap) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param gameMap
	 * @return
	 */
	private int evaluate(GameMap gameMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param player
	 * @return
	 */
	private boolean hasMarbles(int player) {
		// TODO Auto-generated method stub
		return false;
	}
}
