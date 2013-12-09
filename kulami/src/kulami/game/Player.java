/**
 * 
 */
package kulami.game;

import kulami.game.GameMap.Pos;

/**
 * @author gordon
 * 
 */
public abstract class Player {

	private Owner colour;
	protected Game game;
	private int marbles;

	public Player() {
		marbles = 20;
	}

	/**
	 * @param colour
	 * @param marbles
	 */
	public Player(Owner colour, Game game) {
		this.colour = colour;
		this.game = game;
		marbles = 20;
	}

	public void setOwner(Owner owner) {
		this.colour = owner;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public Owner getCoulour() {
		return colour;
	}

	public int getMarbles() {
		return marbles;
	}

	public void placeMarble(Pos pos) {
		if (marbles > 0) {
			game.placeMarble(pos);
			marbles--;
		} else {
			// TODO throw an exception
		}
	}
}
