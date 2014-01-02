/**
 * 
 */
package kulami.game;

import kulami.game.board.Board;
import kulami.game.board.Marbles;
import kulami.gui.GameObserver;

/**
 * @author gordon
 *
 */
public interface GameObservable {

	public void registerObserver(GameObserver observer);
	public void removeObserver(GameObserver observer);
	public Board getBoard();
	public Marbles getMarbles();
}
