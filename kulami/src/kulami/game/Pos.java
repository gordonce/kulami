/**
 * 
 */
package kulami.game;

public class Pos {
	private int row;
	private int col;

	public Pos(int row, int col) {
		assert row >= 0 && row < 10;
		assert col >= 0 && col < 10;
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

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

}