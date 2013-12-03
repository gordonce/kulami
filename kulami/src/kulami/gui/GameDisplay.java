/**
 * 
 */
package kulami.gui;

import kulami.game.GameMap;
import kulami.game.GameObservable;


/**
 * @author gordon
 *
 */
public class GameDisplay implements GameObserver {

	private MapPainter mapPainter;
	
	public GameDisplay(GameObservable game) {
		mapPainter = new MapPainter();
		game.registerObserver(this);
	}
	/* (non-Javadoc)
	 * @see kulami.gui.GameObserver#gameChanged()
	 */
	@Override
	public void gameChanged(GameObservable game) {
		GameMap gameMap = game.getGameMap();
		mapPainter.drawMap(gameMap);
	}

}
