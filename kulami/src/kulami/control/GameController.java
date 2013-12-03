/**
 * 
 */
package kulami.control;

import javax.swing.JFrame;

import kulami.gui.Mainframe;

/**
 * @author gordon
 * 
 */
public class GameController implements MessageObserver {

	private Mainframe mainframe;
	
	public GameController() {
		
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

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameController gameController = new GameController();
				JFrame mainframe = new Mainframe(gameController);
			}
		});
		
	}

}
