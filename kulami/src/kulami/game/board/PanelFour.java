package kulami.game.board;

/**
 * A <code>Panel</code> of size 4.
 * 
 * @author gordon
 * 
 */
public class PanelFour extends Panel {


	/**
	 * @param name
	 */
	public PanelFour(char name) {
		super(name);
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
		return new Pos[] { corner, Pos.getPos(row, col + 1),
				Pos.getPos(row + 1, col), Pos.getPos(row + 1, col + 1) };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.board.Panel#getSize()
	 */
	@Override
	public int getSize() {
		return 4;
	}

}
