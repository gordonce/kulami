/**
 * 
 */
package kulami.game;

/**
 * A Field is a field on the Kulami game map. A Field belongs to a Board and has
 * an Owner.
 * 
 * @author gordon
 * 
 */
public class Field {
	private Board board;
	private Owner owner;

	/**
	 * Create a new Field that belongs to a Board and has an Owner. A Field
	 * always has a Board and an Owner. The Board is immutable, but the Owner
	 * can be changed.
	 * 
	 * @param board
	 * @param owner
	 */
	public Field(Board board, Owner owner) {
		this.board = board;
		this.owner = owner;
	}

	/**
	 * Return the current Owner of the Field.
	 * 
	 * @return
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Return the Board that the Field belongs to.
	 * 
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Set the Owner of the Field. The Owner of the Board that the Field belongs
	 * to is automatically updated.
	 * 
	 * @param owner
	 */
	public void setOwner(Owner owner) {
		if (this.owner != owner) {
			this.owner = owner;
			board.updateOwner();
		}
	}
}
