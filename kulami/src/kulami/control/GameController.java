/**
 * 
 */
package kulami.control;

import kulami.game.Game;
import kulami.game.Player;
import kulami.gui.GameDisplay;
import kulami.gui.Mainframe;
import kulami.gui.MessageDisplay;
import kulami.gui.NewGameDialog;
import kulami.gui.PlayerDialog;

/**
 * @author gordon
 * 
 */
public class GameController implements MessageObserver {

	private Mainframe mainframe;
	private MainframeAdapter mainframeAdapter;

	private PlayerDialog playerDialog;
	private PlayerDialogAdapter playerDialogAdapter;
	
	private NewGameDialog newGameDialog;
	private NewGameDialogAdapter newGameDialogAdapter;
	
	private GameDisplay gameDisplay;
	private MessageDisplay messageDisplay;
	
	private Game game;
	
	private Player player;
	
	private String playerName;
	private boolean playerHuman;
	private int compPlayerLevel;
	
	public GameController() {
		mainframeAdapter = new MainframeAdapter(this);
		mainframe = new Mainframe(mainframeAdapter);
		mainframe.setVisible(true);
	}

	public void showPlayerDialog() {
		System.out.println("show new player dialog");
		playerDialogAdapter = new PlayerDialogAdapter(this);
		playerDialog = new PlayerDialog(mainframe, playerDialogAdapter);
		playerDialog.setVisible(true);
	}

	/**
	 * @param name
	 * @param human
	 * @param level
	 */
	public void newPlayer(String name, boolean human, int level) {
	}

	public void showNewGameDialog() {
		newGameDialogAdapter = new NewGameDialogAdapter(this);
		newGameDialog = new NewGameDialog(newGameDialogAdapter);
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
