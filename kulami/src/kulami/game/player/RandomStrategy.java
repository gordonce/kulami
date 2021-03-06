package kulami.game.player;

import java.util.ArrayList;
import java.util.Random;

import kulami.game.Game;
import kulami.game.board.GameMap;
import kulami.game.board.Pos;

/**
 * This implementation of <code>KulamiStrategy</code> chooses a random position
 * out of all possible positions.
 * 
 * @author gordon
 * 
 */
public class RandomStrategy implements KulamiStrategy {

	private Random randomGen;

	/**
	 * The constructor ignores the level.
	 * 
	 * @param level
	 */
	public RandomStrategy(int level) {
		randomGen = new Random();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.KulamiStrategy#choosePos(kulami.game.GameMap,
	 * kulami.game.Player)
	 */
	@Override
	public Pos choosePos(Game game) {
		GameMap gameMap = game.getGameMap();
		ArrayList<Pos> candidates = gameMap.getLegalFields();
		System.out.println(candidates);
		int randomIdx = randomGen.nextInt(candidates.size());
		return candidates.get(randomIdx);
	}

}
