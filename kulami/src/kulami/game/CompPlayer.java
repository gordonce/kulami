/**
 * 
 */
package kulami.game;

import kulami.game.GameMap.Pos;



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
	public CompPlayer(String playerName, Owner owner) {
		super(playerName, owner);
		strategy = new RandomStrategy();
	}
	
	public Pos getMove(GameMap gameMap) {
		return strategy.choosePos(gameMap);
	}

}
