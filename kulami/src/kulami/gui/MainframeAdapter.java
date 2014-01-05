/**
 * 
 */
package kulami.gui;

/**
 * Adapter interface for user input from the <code>Mainframe</code>
 * 
 * @author gordon
 * 
 */
public interface MainframeAdapter {

	/**
	 * User clicked start game
	 */
	public void startGameClicked();

	/**
	 * User clicked abort game
	 */
	public void abortGameClicked();

	/**
	 * User clicked new board
	 */
	public void newGameMapClicked();

	/**
	 * User clicked edit board
	 */
	public void editGameMapClicked();

	/**
	 * User clicked load board
	 */
	public void loadGameMapClicked();

	/**
	 * User activated display previous moves
	 */
	public void previousMovesActivated();

	/**
	 * User deactivated display previous moves
	 */
	public void previousMovesDeactivated();

	/**
	 * User activated display possible moves
	 */
	public void possibleMovesActivated();

	/**
	 * User deactivated display possible moves
	 */
	public void possibleMovesDeactivated();

	/**
	 * User activated display board possession
	 */
	public void boardPossessionActivated();

	/**
	 * User deactivated display board possession
	 */
	public void boardPossessionDeactivated();

	/**
	 * User entered a message in the chat area
	 * 
	 * @param message
	 *            the message
	 */
	public void messageEntered(String message);

	/**
	 * User clicked exit game
	 */
	public void exitClicked();
}
