/**
 * 
 */
package kulami.gui;

/**
 * @author gordon
 *
 */
public interface MainframeAdapter {

	public void newPlayerClicked();
	
	public void startGameClicked();
	
	public void abortGameClicked();
	
	public void newGameMapClicked();
	
	public void editGameMapClicked();
	
	public void loadGameMapClicked();
	
	public void previousMovesActivated();
	
	public void previousMovesDeactivated();
	
	public void possibleMovesActivated();
	
	public void possibleMovesDeactivated();
	
	public void boardPossessionActivated();
	
	public void boardPossessionDeactivated();
	
	public void messageEntered(String message);
}
