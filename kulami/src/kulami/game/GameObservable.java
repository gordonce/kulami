/**
 * 
 */
package kulami.game;

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
