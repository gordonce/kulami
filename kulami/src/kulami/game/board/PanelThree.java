/**
 * 
 */
package kulami.game.board;

/**
 * @author gordon
 * 
 */
public class PanelThree extends Panel {

	/**
	 * @param name
	 */
	public PanelThree(char name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.board.Panel#getPositions(kulami.game.board.Pos,
	 * kulami.game.board.Orientation)
	 */
	@Override
	public Pos[] getPositions(Pos corner, Orientation orientation) {
		int row = corner.getRow();
		int col = corner.getCol();
		if (orientation == Orientation.Horizontal)
			return new Pos[] { corner, Pos.getPos(row, col + 1),
					Pos.getPos(row, col + 2) };
		else
			return new Pos[] { corner, Pos.getPos(row + 1, col),
					Pos.getPos(row + 2, col) };
	}

	/* (non-Javadoc)
	 * @see kulami.game.board.Panel#getSize()
	 */
	@Override
	public int getSize() {
		return 3;
	}

}
