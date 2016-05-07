/**
 * 
 */
package kulami.control;

/**
 * This class is a wrapper for display settings that the user can choose to turn
 * on or off during a game.
 * 
 * @author gordon
 * 
 */
public class DisplayFlags {
	private boolean previousMoves;
	private boolean possibleMoves;
	private boolean panelPossession;

	/**
	 * Default constructor which sets all flags to <code>false</code>.
	 */
	public DisplayFlags() {
		previousMoves = false;
		possibleMoves = false;
		panelPossession = false;
	}

	/**
	 * Construct a DisplayFlags object and initialize flags to chosen values.
	 * <p>
	 * Only the controller should call this constructor.
	 * 
	 * @param previousMoves
	 *            display previous moves?
	 * @param possibleMoves
	 *            display currently legal moves?
	 * @param panelPossession
	 *            display current owners of each panel?
	 */
	DisplayFlags(boolean previousMoves, boolean possibleMoves,
			boolean panelPossession) {
		this.previousMoves = previousMoves;
		this.possibleMoves = possibleMoves;
		this.panelPossession = panelPossession;
	}

	/**
	 * Returns the value of <code>previousMoves</code>
	 * 
	 * @return previousMoves
	 */
	public boolean isPreviousMoves() {
		return previousMoves;
	}

	/**
	 * Returns the value of <code>possibleMoves</code>
	 * 
	 * @return the possibleMoves
	 */
	public boolean isPossibleMoves() {
		return possibleMoves;
	}

	/**
	 * Returns the value of <code>panelPossession</code>
	 * 
	 * @return panelPossession
	 */
	public boolean isPanelPossession() {
		return panelPossession;
	}

	/**
	 * Sets <code>previousMoves</code>.
	 * <p>
	 * This method should only be called by a controller.
	 * 
	 * @param previousMoves
	 */
	void setPreviousMoves(boolean previousMoves) {
		this.previousMoves = previousMoves;
	}

	/**
	 * Sets <code>possilbeMoves</code>.
	 * <p>
	 * This method should only be called by a controller.
	 * 
	 * @param possibleMoves
	 */
	void setPossibleMoves(boolean possibleMoves) {
		this.possibleMoves = possibleMoves;
	}

	/**
	 * Sets <code>panelPossession</code>.
	 * <p>
	 * This method should only be called by a controller.
	 * 
	 * @param panelPossession
	 */
	void setPanelPossession(boolean panelPossession) {
		this.panelPossession = panelPossession;
	}

}
