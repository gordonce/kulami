/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.Random;

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
	 * @see kulami.game.KulamiStrategy#choosePos(kulami.game.GameMap, kulami.game.Player)
	 */
	@Override
	public Pos choosePos(GameMap gameMap) {
		ArrayList<Pos> candidates = gameMap.getLegalFields();
		int randomIdx = randomGen.nextInt(candidates.size());
		return candidates.get(randomIdx);
	}

}
