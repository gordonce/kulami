/**
 * 
 */
package kulami.gui;

import kulami.control.GameController;
import kulami.game.Pos;

/**
 * @author gordon
 *
 */
public class GameDisplayAdapter {

	private GameController controller;

	/**
	 * @param controller
	 */
	public GameDisplayAdapter(GameController controller) {
		this.controller = controller;
	}
	
	public void tileClicked(Pos pos) {
		controller.fieldClicked(pos);
	}
}
