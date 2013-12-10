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
	private MainframeAdapter mainframeAdapter;

	public GameController() {

		mainframeAdapter = new MainframeAdapter(this);
		mainframe = new Mainframe(mainframeAdapter);
		mainframe.setVisible(true);
	}

	public void showNewPlayerDialog() {
		System.out.println("show new player dialog");
	}

	/**
	 * @param name
	 * @param human
	 * @param level
	 */
	public void newPlayer(String name, boolean human, int level) {
	}

	public void showNewGameDialog() {
		// mainframe.showNewGameDialog();
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

}
