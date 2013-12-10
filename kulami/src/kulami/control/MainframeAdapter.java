/**
 * 
 */
package kulami.control;

/**
 * @author gordon
 *
 */
public class MainframeAdapter {

	private GameController gameController;

	public MainframeAdapter(GameController gameController) {
		this.gameController = gameController;
	}
	
	public void newPlayerClicked() {
		gameController.showNewPlayerDialog();
	}
	
	public void startGameClicked() {
		
	}
	
	public void abortGameClicked() {
		
	}
	
	public void newGameMapClicked() {
		
	}
	
	public void editGameMapClicked() {
	
	}
	
	public void loadGameMapClicked() {
		
	}
	
	public void previousMovesActivated() {
		System.out.println("previous moves activated");
		
	}
	
	public void previousMovesDeactivated() {
		System.out.println("previous moves deactivated");
	}
	
	public void possibleMovesActivated() {
		
	}
	
	public void possibleMovesDeactivated() {
		
	}
	
	public void boardPossessionActivated() {
		
	}
	
	public void boardPossessionDeactivated() {
		
	}
	
	public void messageEntered(String message) {
		System.out.println("Message: " + message);
	}
}
