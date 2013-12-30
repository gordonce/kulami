/**
 * 
 */
package kulami.game;

import kulami.game.board.GameMap;
import kulami.gui.GameObserver;

/**
 * @author gordon
 *
 */
public interface GameObservable {

	public void registerObserver(GameObserver observer);
	public void removeObserver(GameObserver observer);
	public GameMap getGameMap();
}
