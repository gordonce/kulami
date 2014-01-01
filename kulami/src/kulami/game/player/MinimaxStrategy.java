/**
 * 
 */
package kulami.game.player;

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
	private static final int POSITIE_INFINITY = 65;
	/**
	 * Negative infinity is set to the number of points for all panels plus 1
	 * negated
	 */
	private static final int NEGATIVE_INFINITY = -65;
	private int level;
	
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
		Pos move = null;
		int eval = max(1, level);
		
		return move;
	}

	private int max(int player, int depth) {
		int maxVal = NEGATIVE_INFINITY;
		return maxVal;
	}
	
	private int min(int player, int depth) {
		int minVal = POSITIE_INFINITY;
		return minVal;
	}
}
