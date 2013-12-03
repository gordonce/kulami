/**
 * 
 */
package kulami.gui;

import javax.swing.JPanel;

import kulami.game.GameMap;
import kulami.game.GameObservable;


/**
 * @author gordon
 *
 */
public class GameDisplay implements GameObserver {

	private MapPainter mapPainter;
	
	public GameDisplay(GameObservable game, JPanel board) {
		mapPainter = new MapPainter(board);
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
