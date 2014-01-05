/**
 * 
 */
package kulami.gui;

import kulami.game.board.Pos;

/**
 * Adapter interface for user input from the <code>GameDisplay</code>
 * 
 * @author gordon
 * 
 */
public interface GameDisplayAdapter {
	/**
	 * User clicked tile at position <code>pos</code>
	 * 
	 * @param pos
	 */
	public void tileClicked(Pos pos);
}
