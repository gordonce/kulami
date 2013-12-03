/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;

import kulami.game.GameMap.Pos;
import kulami.gui.GameObserver;

/**
 * @author gordon
 *
 */
public class Game implements GameObservable {

	private GameMap gameMap;
	private Player player;
	private List<GameObserver> gameObservers;
	
	/**
	 * @param board
	 * @param player
	 */
	public Game(GameMap gameMap, Player player) {
		this.gameMap = gameMap;
		this.player = player;
		gameObservers = new ArrayList<>();
	}
	
	public void placeMarble(Pos pos) {
		gameMap.setOwner(pos, player.getCoulour());
		informObservers();
	}
	
	@Override
	public GameMap getGameMap() {
		return gameMap;
	}

	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#registerObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void registerObserver(GameObserver observer) {
		gameObservers.add(observer);
	}

	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#removeObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void removeObserver(GameObserver observer) {
		gameObservers.remove(observer);
	}
	
	private void informObservers() {
		for (GameObserver observer: gameObservers)
			observer.gameChanged(this);
	}
}
