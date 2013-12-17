/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;

import kulami.gui.GameObserver;

/**
 * @author gordon
 *
 */
public class Game implements GameObservable {

	private GameMap gameMap;
	private Player player;
	private int level;
	
	private List<GameObserver> gameObservers;
	
	/**
	 * @param board
	 * @param player
	 */
	public Game(GameMap gameMap, Player player, int level) {
		this.gameMap = gameMap;
		this.player = player;
		gameObservers = new ArrayList<>();
	}
	
	public void placeMarble(Pos pos) {
		gameMap.setOwner(pos, player.getCoulour());
		informObservers();
	}
	
	public void pushMap() {
		for (GameObserver observer: gameObservers)
			observer.boardChanged(this);
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

	@Override
	public String toString() {
		return "Game with map: \n" + gameMap;
	}
	
	
	
}
