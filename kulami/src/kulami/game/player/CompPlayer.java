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

	/**
	 * @param playerName
	 * @param colour
	 *            'r' for red or 'b' for black.
	 */
	public CompPlayer(String playerName, char colour, int level) {
		super(playerName, colour);
		strategy = new RandomStrategy(level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.player.Player#makeMove(kulami.game.board.GameMap)
	 */
	@Override
	public Pos makeMove(GameMap gameMap) {
		return strategy.choosePos(gameMap);

	}

}
