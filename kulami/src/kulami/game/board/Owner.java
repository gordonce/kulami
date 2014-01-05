package kulami.game.board;

/**
 * The owner of a field on the board.
 * 
 * @author gordon
 * 
 */
public enum Owner {
	None(0), Black(1), Red(2);

	int idx;

	Owner(int idx) {
		this.idx = idx;
	}
	
	/**
	 * Returns the index corresponding to the owner.
	 * 
	 * @return 0 for None, 1 for Black, 2 for Red.
	 */
	public int getIdx() {
		return idx;
	}
}
