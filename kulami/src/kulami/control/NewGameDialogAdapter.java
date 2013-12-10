/**
 * 
 */
package kulami.control;

/**
 * @author gordon
 *
 */
public class NewGameDialogAdapter {

	private GameController gameController;
	
	
	/**
	 * @param gameController
	 */
	public NewGameDialogAdapter(GameController gameController) {
		this.gameController = gameController;
	}

	public void connectClicked() {
		gameController.connectServer();
	}
	
	public void cancelClicked() {
		gameController.cancelNewGameDialog();
	}
	
	
}
