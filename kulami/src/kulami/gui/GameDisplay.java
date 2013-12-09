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
	
	// TODO Test:
	public GameDisplay(JPanel board) {
		mapPainter = new MapPainter(board);
	}
	
	/* (non-Javadoc)
	 * @see kulami.gui.GameObserver#gameChanged()
	 */
	@Override
	public void gameChanged(GameObservable game) {
		GameMap gameMap = game.getGameMap();
		mapPainter.drawMarbles(gameMap);
	}

	/* (non-Javadoc)
	 * @see kulami.gui.GameObserver#boardChanged(kulami.game.GameObservable)
	 */
	@Override
	public void boardChanged(GameObservable game) {
		GameMap gameMap = game.getGameMap();
		mapPainter.drawMap(gameMap);
	}

}
