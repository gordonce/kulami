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
public class GameController {

	private Mainframe mainframe;
	private MainframeAdapter mainframeAdapter;

	private PlayerDialog playerDialog;
	private PlayerDialogAdapter playerDialogAdapter;
	
	private NewGameDialog newGameDialog;
	private NewGameDialogAdapter newGameDialogAdapter;
	
	private GameDisplay gameDisplay;
	private MessageDisplay messageDisplay;
	
	private ServerProxy serverProxy;
	
	private Game game;
	private Player player;
	
	private String playerName;
	private boolean playerHuman;
	private int compPlayerLevel;
	private ServerAdapter serverAdapter;
	
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
		String hostName = newGameDialog.getHost();
		int port = newGameDialog.getPort();
		
		serverProxy = new ServerProxy(hostName, port);
		serverAdapter = new ServerAdapter(this);
		
		serverProxy.addObserver(serverAdapter);
		
		serverProxy.connectAndListen();
		// TODO display error message if connection fails
	}

	/**
	 * 
	 */
	public void cancelNewGameDialog() {
		newGameDialog.clearAndHide();
	}
	
	public void serverMessageReceived(String message) {
		System.out.println("Received: " + message);
	}

	/**
	 * Server sent "Kulami?"
	 * Connection was successfully established.
	 */
	public void sendName() {
		newGameDialog.clearAndHide();
		serverProxy.sendMessage(String.format("neuerClient(%s).", playerName));
	}

	/**
	 * 
	 */
	public void displayMessage() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void sendParameters() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void receiveParameters() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void playerTwoConnected() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void assignColour() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void startGame() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void illegalMove() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void legalMove() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void opponentMoved() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void endGame() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void displayPlayerMessage() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void unknownMessage() {
		// TODO Auto-generated method stub
		
	}


}
