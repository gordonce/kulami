package kulami.game.board;

/**
 * Pos represents a position on a 10 by 10 Kulami board.
 * 
 * @author gordon
 * 
 */
public class Pos {
	private int row;
	private int col;

	private static Pos[][] positions = new Pos[10][10];

	static {
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				positions[row][col] = new Pos(row, col);
	}

	/**
	 * The constructor of Pos is private.
	 * 
	 * @param row
	 * @param col
	 */
	private Pos(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Get an immutable position with a row starting with row 0 at the top and a
	 * column starting with row 0 at the left side of a board.
	 * 
	 * @param row
	 *            Row between 0 and 9
	 * @param col
	 *            Column between 0 and 9
	 */
	public static Pos getPos(int row, int col) {
		assert row >= 0 && row < 10;
		assert col >= 0 && col < 10;
		return positions[row][col];
	}

	/**
	 * Returns the <code>Pos</code> corresponding to position <code>index</code>
	 * in a list of 100 elements.
	 * 
	 * @param index
	 *            0...99
	 * @return
	 */
	public static Pos getPos(int index) {
		assert index >= 0 && index < 100;
		return positions[index / 10][index % 10];
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

	/**
	 * Returns the position in a 100 element list corresponding to the
	 * <code>Pos</code> object.
	 * 
	 * @return 0...99
	 */
	public int getIdx() {
		return row * 10 + col;
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

	public static void main(String[] args) {
		System.out.println(getPos(11));
		System.out.println(getPos(29));
		System.out.println(getPos(99));

	}
}