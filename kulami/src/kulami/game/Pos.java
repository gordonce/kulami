/**
 * 
 */
package kulami.game;

/**
 * Pos represents a position on a 10 by 10 Kulami board.
 * 
 * @author gordon
 * 
 */
public class Pos {
	private int row;
	private int col;

	/**
	 * Construct an immutable position with a row starting with row 1 at the top
	 * and a column starting with row 1 at the left side of a board.
	 * 
	 * @param row
	 *            Row between 1 and 10
	 * @param col
	 *            Column between 1 and 10
	 */
	public Pos(int row, int col) {
		assert row >= 0 && row < 10;
		assert col >= 0 && col < 10;
		this.row = row;
		this.col = col;
	}

	/**
	 * Return the row.
	 * 
	 * @return The row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Return the column.
	 * 
	 * @return The column
	 */
	public int getCol() {
		return col;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + row + "," + col + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Pos) {
			Pos that = (Pos) obj;
			return this.row == that.row && this.col == that.col;
		} else
			return false;
	}

}