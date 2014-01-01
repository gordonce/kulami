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
public abstract class Player {

	private String name;
	private char colour;
	private int marbles;

	public Player() {
		marbles = 20;
	}

	/**
	 * @param colour
	 * @param marbles
	 */
	public Player(String name, char colour) {
		this.name = name;
		this.colour = colour;
		marbles = 20;
	}

	public String getName() {
		return name;
	}
	
	public char getCoulour() {
		return colour;
	}

	public int getMarbles() {
		return marbles;
	}

	@Override
	public String toString() {
		return String.format("%s (%d marbles)", name, marbles);
	}

	/**
	 * @return
	 */
	abstract public Pos makeMove(Game game);
	
	

}
