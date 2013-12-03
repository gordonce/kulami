/**
 * 
 */
package kulami.control;

import kulami.gui.Mainframe;

/**
 * @author gordon
 * 
 */
public class GameController implements MessageObserver {

	private Mainframe mainframe;

	public GameController() {

		mainframe = new Mainframe(this);
		
	}

	public void newPlayer() {
		mainframe.showNewPlayerDialog();
	}
	
	public void newGame() {
		mainframe.showNewGameDialog();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.control.MessageObserver#inform(java.lang.String)
	 */
	@Override
	public void inform(String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameController gameController = new GameController();
	}

}
