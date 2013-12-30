/**
 * 
 */
package kulami.game.board;

/**
 * Marbles represents the positions of all marbles on a board. Marbles can be
 * placed or removed.
 * 
 * @author gordon
 * 
 */
public class Marbles {

	private Owner[][] marbles;

	/**
	 * Construct a new Marbles object with 100 slots for marbles. All positions
	 * are initialized to None.
	 */
	public Marbles() {
		marbles = new Owner[10][10];
		setAllNone();
	}

	/**
	 * Get the owner of position pos.
	 * 
	 * @param pos
	 *            A position
	 * @return The Owner
	 */
	public Owner getMarble(Pos pos) {
		return marbles[pos.getRow()][pos.getCol()];
	}

	/**
	 * Set the Owner of position pos to owner.
	 * 
	 * @param pos
	 * @param owner
	 */
	public void setMarble(Pos pos, Owner owner) {
		marbles[pos.getRow()][pos.getCol()] = owner;
	}

	/**
	 * Set all positions to Owner None.
	 */
	public void setAllNone() {
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				marbles[row][col] = Owner.None;
	}

}
