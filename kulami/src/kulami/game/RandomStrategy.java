/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.Random;

import kulami.game.GameMap.Pos;

/**
 * @author gordon
 *
 */
public class RandomStrategy implements KulamiStrategy {

	private Random randomGen;
	
	public RandomStrategy() {
		randomGen = new Random();
	}
	/* (non-Javadoc)
	 * @see kulami.game.KulamiStrategy#choosePos(kulami.game.Game, kulami.game.Player)
	 */
	@Override
		
	public Pos choosePos(Game game, Player player) {
		GameMap gameMap = game.getGameMap();
//		Owner nextPlayer = player.getCoulour();
		ArrayList<Pos> candidates = gameMap.getLegalFields();
		int randomIdx = randomGen.nextInt(candidates.size());
		return candidates.get(randomIdx);
	}

}
