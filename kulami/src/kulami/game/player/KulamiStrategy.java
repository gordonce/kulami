/**
 * 
 */
package kulami.game.player;

import kulami.game.Game;
import kulami.game.board.Pos;

/**
 * A <code>CompPlayer</code> uses an object of a class that implements the
 * <code>KulamiStrategy</code> interface to make moves.
 * 
 * @author gordon
 * 
 */
public interface KulamiStrategy {
	/**
	 * Choose a position for the next move.
	 * 
	 * @param a
	 *            reference to the <code>Game</code>
	 * @return the choson position
	 */
	public Pos choosePos(Game game);
}
