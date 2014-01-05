/**
 * 
 */
package kulami.game.player;

import kulami.game.Game;
import kulami.game.board.Pos;

/**
 * A <code>HumanPlayer</code> makes his/her own moves.
 * 
 * @author gordon
 * 
 */
public class HumanPlayer extends Player {

	/**
	 * Constructs a new <code>HumanPlayer</code>.
	 * 
	 * @param name
	 * @param colour
	 *            'r' or 'b'
	 */
	public HumanPlayer(String name, char colour) {
		super(name, colour);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.player.Player#makeMove(kulami.game.board.GameMap)
	 */
	@Override
	public Pos makeMove(Game game) {
		// TODO Auto-generated method stub
		return null;
	}

}
