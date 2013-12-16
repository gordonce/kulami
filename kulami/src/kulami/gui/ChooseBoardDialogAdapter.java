/**
 * 
 */
package kulami.gui;

import kulami.control.GameController;

/**
 * @author gordon
 *
 */
public class ChooseBoardDialogAdapter {

	private GameController gameController;

	/**
	 * @param gameController
	 */
	public ChooseBoardDialogAdapter(GameController gameController) {
		this.gameController = gameController;
	}
	
	public void okClicked() {
		gameController.loadGame();
	}
	
	public void cancelClicked() {
		gameController.chooseBoardCancelled();
	}
}
