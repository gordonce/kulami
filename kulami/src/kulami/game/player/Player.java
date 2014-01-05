package kulami.game.player;

import kulami.game.Game;
import kulami.game.board.Pos;

/**
 * This class represents the local player in a Kulami game.
 * 
 * @author gordon
 * 
 */
public abstract class Player {

	private String name;
	private char colour;

	public Player() {
	}

	/**
	 * Constructor that can be called by overriding classes.
	 * 
	 * @param name
	 * @param colour
	 */
	public Player(String name, char colour) {
		this.name = name;
		this.colour = colour;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the colour ('r' or 'b')
	 */
	public char getCoulour() {
		return colour;
	}

	/**
	 * Choose position for the next move.
	 * 
	 * @param game
	 *            a reference to the <code>Game</code>
	 * @return position
	 */
	abstract public Pos makeMove(Game game);

	@Override
	public String toString() {
		return String.format("%s (%s)", name, colour);
	}

}
