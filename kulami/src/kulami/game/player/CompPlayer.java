/**
 * 
 */
package kulami.game.player;

import kulami.game.Game;
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
	 * @param level
	 */
	public CompPlayer(String playerName, char colour, int level) {
		super(playerName, colour);
		strategy = new MinimaxStrategy(level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.player.Player#makeMove(kulami.game.board.GameMap)
	 */
	@Override
	public Pos makeMove(Game game) {
		return strategy.choosePos(game);

	}

}
