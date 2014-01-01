/**
 * 
 */
package kulami.game.player;

import java.util.ArrayList;
import java.util.Random;

import kulami.game.board.GameMap;
import kulami.game.board.Pos;

/**
 * @author gordon
 *
 */
public class RandomStrategy implements KulamiStrategy {

	private Random randomGen;
	
	public RandomStrategy(int level) {
		randomGen = new Random();
	}

	/* (non-Javadoc)
	 * @see kulami.game.KulamiStrategy#choosePos(kulami.game.GameMap, kulami.game.Player)
	 */
	@Override
	public Pos choosePos(GameMap gameMap) {
		ArrayList<Pos> candidates = gameMap.getLegalFields();
		System.out.println(candidates);
		int randomIdx = randomGen.nextInt(candidates.size());
		return candidates.get(randomIdx);
	}

}
