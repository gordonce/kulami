/**
 * 
 */
package kulami.gui;

import kulami.game.GameObservable;

/**
 * @author gordon
 *
 */
public interface GameObserver {
	public void gameChanged(GameObservable game);
	public void boardChanged(GameObservable game);
	public void flagsChanged(GameObservable game);
}
