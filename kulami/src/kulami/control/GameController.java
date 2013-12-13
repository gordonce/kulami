/**
 * 
 */
package kulami.control;

import kulami.game.Game;
import kulami.game.Player;
import kulami.gui.Mainframe;
import kulami.gui.MessagePager;
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
	
	private ServerProxy serverProxy;
	
	private Game game;
	private Player player;
	
	private String playerName;
	private boolean playerHuman;
	private int compPlayerLevel;
	private ServerAdapter serverAdapter;
	private MessagePager messagePager;
	
	public GameController() {
		mainframeAdapter = new MainframeAdapter(this);
		mainframe = new Mainframe(mainframeAdapter);
		messagePager = mainframe.getMessageDisplay();
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

	/* Methods to handle server messages */
	
	/**
	 * Server sent "Kulami?"
	 * Connection was successfully established.
	 */
	public void sendName() {
		newGameDialog.clearAndHide();
		serverProxy.sendMessage(String.format("neuerClient(%s).", playerName));
	}

	/**
	 * @param message 
	 * 
	 */
	public void displayMessage(String message) {
		messagePager.display("Server: " + message);
	}

	/**
	 * 
	 */
	public void sendParameters() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param opponentName 
	 * @param colour 
	 * @param level 
	 * @param mapCode 
	 * 
	 */
	public void receiveParameters(String mapCode, int level, char colour, String opponentName) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param name 
	 * 
	 */
	public void playerTwoConnected(String name) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param colour
	 * 
	 */
	public void assignColour(char colour) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param startingPlayer
	 * 
	 */
	public void startGame(char startingPlayer) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param msg 
	 * 
	 */
	public void illegalMove(String msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param mapCode 
	 * 
	 */
	public void legalMove(String mapCode) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param mapCode 
	 * 
	 */
	public void opponentMoved(String mapCode) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param pointsBlack 
	 * @param pointsRed 
	 * 
	 */
	public void endGame(int pointsRed, int pointsBlack) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param msg 
	 * 
	 */
	public void displayPlayerMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param message 
	 * 
	 */
	public void unknownMessage(String message) {
		mainframe.displayWarning("Unbekannte Nachricht empfangen:\n" + message);
	}


}
