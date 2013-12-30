/**
 * 
 */
package kulami.game.player;

import kulami.game.RandomStrategy;
import kulami.game.board.GameMap;
import kulami.game.board.Pos;




/**
 * @author gordon
 *
 */
public class CompPlayer extends Player {

	private KulamiStrategy strategy;
	
	public CompPlayer() {
		super();
	}
	
	/**
	 * @param playerName
	 * @param owner
	 */
	public CompPlayer(String playerName, char owner) {
		super(playerName, owner);
		strategy = new RandomStrategy();
	}
	
	public Pos getMove(GameMap gameMap) {
		return strategy.choosePos(gameMap);
	}

}
