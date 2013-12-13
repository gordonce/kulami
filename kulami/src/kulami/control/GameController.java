/**
 * 
 */
package kulami.control;

import kulami.game.CompPlayer;
import kulami.game.Game;
import kulami.game.GameMap;
import kulami.game.HumanPlayer;
import kulami.game.Owner;
import kulami.game.Player;
import kulami.gui.GameDisplay;
import kulami.gui.Mainframe;
import kulami.gui.MessagePager;
import kulami.gui.NewGameDialog;
import kulami.gui.PlayerDialog;

/**
 * The GameController is the layer between the GUI and the game logic. The GUI
 * delegates all user input to the controller and lets the controller decide
 * what to do. The controller also receives Kulami server messages. The
 * controller decides which actions to take and manipulates the game model.
 * 
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
	private GameDisplay gameDisplay;

	/**
	 * The GameController constructor creates a Mainframe and displays it. It
	 * also requests a MessagePager from the Mainframe so that messages can be
	 * displayed at any time.
	 */
	public GameController() {
		mainframeAdapter = new MainframeAdapter(this);
		mainframe = new Mainframe(mainframeAdapter);
		messagePager = mainframe.getMessageDisplay();
		mainframe.setVisible(true);
	}

	/**
	 * Display the New Player dialog which prompts the user to enter a user name
	 * and decide whether to play in human or in computer mode.
	 */
	public void showPlayerDialog() {
		playerDialogAdapter = new PlayerDialogAdapter(this);
		playerDialog = new PlayerDialog(mainframe, playerDialogAdapter);
		playerDialog.setVisible(true);
	}

	/**
	 * Get player data from the New Player dialog and save them for a later
	 * game.
	 */
	public void newPlayer() {
		playerName = playerDialog.getName();
		playerHuman = playerDialog.getHuman();
		compPlayerLevel = playerDialog.getCompLevel();

		playerDialog.clearAndHide();
	}

	/**
	 * Close the New Player dialog without taking any action.
	 */
	public void cancelPlayerDialog() {
		playerDialog.clearAndHide();
	}

	/**
	 * Display the New Game dialog which prompts the user for a Kulami server
	 * connection.
	 */
	public void showNewGameDialog() {
		newGameDialogAdapter = new NewGameDialogAdapter(this);
		newGameDialog = new NewGameDialog(mainframe, newGameDialogAdapter);
		newGameDialog.setVisible(true);
	}

	/**
	 * Close the New Game dialog without taking any action.
	 */
	public void cancelNewGameDialog() {
		newGameDialog.clearAndHide();
	}

	/**
	 * Get Kulami server connection data from the New Game dialog and try to
	 * connect to the server.
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

	/* Methods to handle server messages */

	/**
	 * Server sent "Kulami?" Connection was successfully established. Continue
	 * protocol by sending the user name.
	 */
	public void sendName() {
		newGameDialog.clearAndHide();
		serverProxy.sendMessage(String.format("neuerClient(%s).", playerName));
	}

	/**
	 * Server sent a message. The message is displayed to the user.
	 * 
	 * @param message
	 *            The message.
	 * 
	 */
	public void displayMessage(String message) {
		messagePager.display("Server: " + message);
	}

	/**
	 * 
	 */
	public void sendParameters() {
		// TODO Display dialog asking for board and level
		// TODO send board and level to server
	}

	/**
	 * Server sent the game parameters. (We are player 2.) Create a GameMap, a
	 * Player, and a Game and start displaying the game.
	 * 
	 * @param opponentName
	 *            The name of the opponent.
	 * @param colour
	 *            The player's colour ('b' for black or 'r' for red).
	 * @param level
	 *            The game level (1, 2, or 3)
	 * @param mapCode
	 *            The 200-character map code.
	 * 
	 */
	public void receiveParameters(String mapCode, int level, char colour,
			String opponentName) {
		GameMap gameMap = new GameMap(mapCode);
		Owner owner;
		if (colour == 'r')
			owner = Owner.Red;
		else
			owner = Owner.Black;
		if (playerHuman)
			player = new HumanPlayer(playerName, owner);
		else
			player = new CompPlayer(playerName, owner);
		game = new Game(gameMap, player);
		// TODO set opponent name display
		startGameDisplay();

	}

	/**
	 * Server sent name of player 2. (We are player 1). Set the display
	 * accordingly.
	 * 
	 * @param name
	 *            The name of the opponent.
	 * 
	 */
	public void playerTwoConnected(String name) {
		// TODO Tell mainframe to display the name.

	}

	/**
	 * Server sent colour. (We are player 1.) The Player object and the Game
	 * object can now be created and the display adjusted.
	 * 
	 * @param colour
	 * 
	 */
	public void assignColour(char colour) {
		// TODO implement method. Same as ReceiveParameters()
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

	private void startGameDisplay() {
		gameDisplay = mainframe.initGameDisplay(game);
		// TODO make the game display show the empty board
	}

}
