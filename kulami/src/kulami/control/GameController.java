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
		playerDialogAdapter = new PlayerDialogAdapter(this);
		playerDialog = new PlayerDialog(mainframe, playerDialogAdapter);
		playerDialog.setVisible(true);
	}

	/**
	 */
	public void newPlayer() {
		playerName = playerDialog.getName();
		playerHuman = playerDialog.getHuman();
		compPlayerLevel= playerDialog.getCompLevel();
		
		playerDialog.clearAndHide();
	}

	public void showNewGameDialog() {
		newGameDialogAdapter = new NewGameDialogAdapter(this);
		newGameDialog = new NewGameDialog(mainframe, newGameDialogAdapter);
		newGameDialog.setVisible(true);
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
	 * 
	 */
	public void cancelPlayerDialog() {
		playerDialog.clearAndHide();
	}

	/**
	 * 
	 */
	public void connectServer() {
		
	}

	/**
	 * 
	 */
	public void cancelNewGameDialog() {
		newGameDialog.clearAndHide();
	}


}
