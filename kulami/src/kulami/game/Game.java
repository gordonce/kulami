/**
 * 
 */
package kulami.game;

import kulami.game.GameMap.Pos;

/**
 * @author gordon
 *
 */
public class Game {

	private GameMap gameMap;
	private Player player;
	
	/**
	 * @param board
	 * @param player
	 */
	public Game(GameMap gameMap, Player player) {
		this.gameMap = gameMap;
		this.player = player;
	}
	
	public void placeMarble(Pos pos) {
		gameMap.setOwner(pos, player.getCoulour());
	}
	
	public GameMap getGameMap() {
		return gameMap;
	}
}
