package kulami.game.player;

import kulami.game.Game;
import kulami.game.board.Pos;

/**
 * A <code>CompPlayer</code> is a <code>Player</code> who makes moves
 * automatically.
 * <p>
 * For making moves, CompPlayer needs a <code>KulamiStrategy</code>.
 * 
 * @author gordon
 * 
 */
public class CompPlayer extends Player {

	private KulamiStrategy strategy;

	/**
	 * Constructs a new <code>CompPlayer</code> with the
	 * <code>MinimaxStrategy</code> parametrized with <code>level</code>.
	 * 
	 * @param playerName
	 * @param colour
	 *            'r' for red or 'b' for black.
	 * @param level
	 *            1...10
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
