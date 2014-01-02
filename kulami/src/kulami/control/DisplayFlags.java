/**
 * 
 */
package kulami.control;

/**
 * @author gordon
 *
 */
public class DisplayFlags {
	private boolean previousMoves;
	private boolean possibleMoves;
	private boolean panelPossession;
	
	/**
	 * @param previousMoves
	 * @param possibleMoves
	 * @param panelPossession
	 */
	public DisplayFlags(boolean previousMoves, boolean possibleMoves,
			boolean panelPossession) {
		this.previousMoves = previousMoves;
		this.possibleMoves = possibleMoves;
		this.panelPossession = panelPossession;
	}
	/**
	 * @return the previousMoves
	 */
	public boolean isPreviousMoves() {
		return previousMoves;
	}
	/**
	 * @return the possibleMoves
	 */
	public boolean isPossibleMoves() {
		return possibleMoves;
	}
	/**
	 * @return the panelPossession
	 */
	public boolean isPanelPossession() {
		return panelPossession;
	}
	
	/**
	 * @param previousMoves the previousMoves to set
	 */
	void setPreviousMoves(boolean previousMoves) {
		this.previousMoves = previousMoves;
	}
	/**
	 * @param possibleMoves the possibleMoves to set
	 */
	void setPossibleMoves(boolean possibleMoves) {
		this.possibleMoves = possibleMoves;
	}
	/**
	 * @param panelPossession the panelPossession to set
	 */
	void setPanelPossession(boolean panelPossession) {
		this.panelPossession = panelPossession;
	}
	
	
	
}
