/**
 * 
 */
package kulami.game.board;

import java.util.logging.Logger;

/**
 * A <code>Panel</code> object represents a panel on a Kulami board.
 * <p>
 * The abstract class <code>Panel</code> is subclassed by panels of a particular
 * size.
 * 
 * @author gordon
 * 
 */
public abstract class Panel {

	private char code;
	private boolean isPlaced;
	private Orientation orientation;
	private Pos corner;

	private static final Logger logger = Logger
			.getLogger("kulami.game.board.Panel");

	/**
	 * Construct a new Panel with a character code. The panel is not placed on
	 * the board yet.
	 * 
	 * @param code
	 *            'b'...'r'
	 */
	public Panel(char code) {
		this.code = code;
		isPlaced = false;
	}

	/**
	 * Factory method that returns a new <code>Panel</code> object based on its
	 * size and code.
	 * 
	 * @param size
	 *            2, 3, 4, or 6
	 * @param code
	 *            'b'...'r'
	 * @return a new <code>Panel</code> object
	 */
	public static Panel getPanel(int size, char code) {
		switch (size) {
		case 6:
			return new PanelSix(code);
		case 4:
			return new PanelFour(code);
		case 3:
			return new PanelThree(code);
		case 2:
			return new PanelTwo(code);
		default:
			return null;
		}
	}

	/**
	 * Returns the panel's code.
	 * 
	 * @return 'b'...'r'
	 */
	public char getName() {
		return code;
	}

	/**
	 * @return <code>true</code> if the panel is placed on a board yet,
	 *         <code>false</code> otherwise
	 */
	public boolean isPlaced() {
		return isPlaced;
	}

	/**
	 * @return the <code>Orientation</code>
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * Returns the size of the panel.
	 * 
	 * @return 2, 3, 4, or 6
	 */
	abstract public int getSize();

	/**
	 * Place the panel on a board with its upper left corner at Pos corner and
	 * with Orientation orientation.
	 * <p>
	 * If any positions would be outside the board, an exception is thrown. The
	 * method does not check, whether any other panels are at the same
	 * positions.
	 * 
	 * @param corner
	 *            the position of the upper left corner.
	 * @param orientation
	 *            the <code>Orientation</code>
	 * @throws PanelOutOfBoundsException
	 */
	public void placePanel(Pos corner, Orientation orientation)
			throws PanelOutOfBoundsException {
		if (canBePlaced(corner, orientation)) {
			this.corner = corner;
			this.orientation = orientation;
			isPlaced = true;
			logger.fine(String.format("Placed panel at pos: %s", corner));
		} else {
			throw new PanelOutOfBoundsException();
		}
	}

	/**
	 * Remove the panel from its board.
	 */
	public void removePanel() {
		if (isPlaced) {
			corner = null;
			orientation = null;
			isPlaced = false;
		}

	}

	/**
	 * Get the positions of the fields of a panel that is placed on a board.
	 * 
	 * @return An array of positions.
	 * @throws PanelNotPlacedException
	 * @throws PanelOutOfBoundsException
	 */
	public Pos[] getPositions() throws PanelNotPlacedException,
			PanelOutOfBoundsException {
		if (isPlaced)
			return getPositions(corner, orientation);
		else
			throw new PanelNotPlacedException();
	}

	/**
	 * Get the positions of a panel's fields for a given upper left corner and
	 * an orientation.
	 * 
	 * @param corner
	 * @param orientation
	 * @return An array of positions
	 */
	public abstract Pos[] getPositions(Pos corner, Orientation orientation)
			throws PanelOutOfBoundsException;

	/**
	 * Calculate the owner of the board by checking who owns the majority of
	 * fields.
	 * 
	 * @return the <code>Owner</code>
	 */
	public Owner getOwner(Marbles marbles) {
		Pos[] positions;
		try {
			positions = getPositions();
		} catch (PanelNotPlacedException | PanelOutOfBoundsException e) {
			return Owner.None;
		}
		int black = 0, red = 0;
		for (Pos pos : positions) {
			Owner fieldOwner = marbles.getMarble(pos);
			if (fieldOwner.equals(Owner.Black))
				black++;
			else if (fieldOwner.equals(Owner.Red))
				red++;
		}
		if (black > red)
			return Owner.Black;
		else if (red > black)
			return Owner.Red;
		else
			return Owner.None;
	}

	/**
	 * Returns <code>true</code> if this <code>Panel</code> could be place with
	 * its upper left corner at <code>corner</code> and with
	 * <code>Orientation</code> <code>orientation</code>.
	 * 
	 * @param corner
	 *            upper left corner
	 * @param orientation
	 *            horizontal or vertical
	 * @return true if panel can be placed there
	 */
	private boolean canBePlaced(Pos corner, Orientation orientation) {
		try {
			getPositions(corner, orientation);
			return true;
		} catch (PanelOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Information about a Panel that has not been placed yet was requested.
	 * @author gordon
	 * 
	 */
	public static class PanelNotPlacedException extends Exception {
	}

	/**
	 * There is not enough space for the Panel at the specified position.
	 * 
	 * @author gordon
	 * 
	 */
	public static class PanelOutOfBoundsException extends Exception {
	}

	public static void main(String[] args) {
		Panel six = new PanelSix('b');
		System.out.println(six.getSize());
	}
}
