/**
 * 
 */
package kulami.game;

import kulami.game.board.Pos;

/**
 * The <code>CompPlayerAdapter</code> interface is implemented by classes that
 * want to be notified when a <code>CompPlayer</code> has chosen its next move.
 * 
 * @author gordon
 * 
 */
public interface CompPlayerAdapter {
	/**
	 * The <code>CompPlayer</code> has chosen to place a marble at position
	 * <code>pos</code>.
	 * 
	 * @param pos
	 */
	public void madeMove(Pos pos);
}
