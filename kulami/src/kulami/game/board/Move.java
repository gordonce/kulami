/**
 * 
 */
package kulami.game.board;


public class Move {
	private Pos pos;
	private Owner owner;

	public Move(Pos pos, Owner owner) {
		this.pos = pos;
		this.owner = owner;
	}

	public Pos getPos() {
		return pos;
	}

	public Owner getOwner() {
		return owner;
	}

}