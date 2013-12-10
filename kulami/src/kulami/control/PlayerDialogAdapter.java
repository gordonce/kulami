/**
 * 
 */
package kulami.control;

import kulami.gui.PlayerDialog;

/**
 * @author gordon
 * 
 */
public class PlayerDialogAdapter {

	private GameController gameController;

	/**
	 * @param gameController
	 */
	public PlayerDialogAdapter(GameController gameController) {
		this.gameController = gameController;
	}

	public void okPressed() {
		gameController.newPlayer();
	}

	public void cancelPressed() {
		gameController.cancelPlayerDialog();
	}

	public void windowClosed() {
	}
}
