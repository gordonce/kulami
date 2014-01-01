/**
 * 
 */
package kulami.game.player;

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
	public CompPlayer(String playerName, char owner, int level) {
		super(playerName, owner);
		strategy = new RandomStrategy(level);
	}
	
	/* (non-Javadoc)
	 * @see kulami.game.player.Player#makeMove(kulami.game.board.GameMap)
	 */
	@Override
	public Pos makeMove(GameMap gameMap) {
		return strategy.choosePos(gameMap);

	}

}
