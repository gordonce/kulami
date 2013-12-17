/**
 * 
 */
package kulami.control;

import java.util.logging.Logger;

import kulami.game.CompPlayer;
import kulami.game.Game;
import kulami.game.GameMap;
import kulami.game.HumanPlayer;
import kulami.game.Owner;
import kulami.game.Player;
import kulami.game.Pos;
import kulami.gui.ChooseBoardDialog;
import kulami.gui.ChooseBoardDialogAdapter;
import kulami.gui.GameDisplay;
import kulami.gui.GameDisplayAdapter;
import kulami.gui.Mainframe;
import kulami.gui.MessagePager;
import kulami.gui.NewGameDialog;
import kulami.gui.PlayerDialog;
import kulami.gui.StatusDisplayer;

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
	private MessageSender messageSender;

	private Game game;

	private String playerName;
	private boolean playerHuman;
	private int compPlayerLevel;
	private ServerAdapter serverAdapter;
	private MessagePager messagePager;
	private StatusDisplayer statusDisplayer;
	private GameDisplay gameDisplay;

	private String opponentName;
	private ChooseBoardDialogAdapter chooseBoardDialogAdapter;
	private ChooseBoardDialog chooseBoardDialog;
	private GameDisplayAdapter gameDisplayAdapter;
	private char playerColour;

	private static final Logger logger = Logger
			.getLogger("kulami.control.GameController");

	/**
	 * The GameController constructor creates a Mainframe and displays it. It
	 * also requests a MessagePager from the Mainframe so that messages can be
	 * displayed at any time.
	 */
	public GameController() {
		mainframeAdapter = new MainframeAdapter(this);
		mainframe = new Mainframe(mainframeAdapter);
		messagePager = mainframe.getMessageDisplay();
		statusDisplayer = mainframe.getStatusDisplay();
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

		statusDisplayer.setHeroName(playerName);
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

		messageSender = new MessageSender(serverProxy);

		// TODO display error message if connection fails
	}

	/* Methods to handle the Choose Board dialog */

	public void showChooseBoardDialog() {
		chooseBoardDialogAdapter = new ChooseBoardDialogAdapter(this);
		chooseBoardDialog = new ChooseBoardDialog(mainframe,
				chooseBoardDialogAdapter);
		chooseBoardDialog.setVisible(true);
	}

	/**
	 * User has clicked OK on the Choose Board dialog.
	 */
	public void loadGame() {
		String boardCode = chooseBoardDialog.getBoardCode();
		int level = chooseBoardDialog.getLevel();

		GameMap board = new GameMap(boardCode);
		board.clearOwners();

		messageSender.sendParameters(board.getMapCode(), level);

		chooseBoardDialog.clearAndHide();

		game = new Game(board, createPlayer(), level);
		startGameDisplay();
	}

	public void chooseBoardCancelled() {
		chooseBoardDialog.clearAndHide();
		serverProxy.disconnect();
	}

	/* Methods to handle server messages */

	/**
	 * Server sent "Kulami?" Connection was successfully established. Continue
	 * protocol by sending the user name.
	 */
	public void sendName() {
		newGameDialog.clearAndHide();
		// serverProxy.sendMessage(String.format("neuerClient(%s).",
		// playerName));
		messageSender.newClient(playerName);
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
	 * Server asked for game parameters. (We are player 1.)
	 * 
	 */
	public void serverWantsParameters() {
		// TODO Display dialog asking for board and level
		showChooseBoardDialog();
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
		gameMap.clearOwners();
		playerColour = colour;
		game = new Game(gameMap, createPlayer() , level);
		this.opponentName = opponentName;
		statusDisplayer.setVillainName(opponentName);
		statusDisplayer.setHeroColour(colour);
		statusDisplayer.setVillainColour(colour == 'b' ? 'r' : 'b');
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
	public void playerTwoConnected(String opponentName) {
		this.opponentName = opponentName;
		statusDisplayer.setVillainName(opponentName);
	}

	/**
	 * Server sent colour. (We are player 1.) The Player object and the Game
	 * object can now be created and the display adjusted.
	 * 
	 * @param colour
	 *            'b' for black or 'r' for red.
	 * 
	 */
	public void assignColour(char colour) {
		statusDisplayer.setHeroColour(colour);
		statusDisplayer.setVillainColour(colour == 'b' ? 'r' : 'b');
		playerColour = colour;
	}

	/**
	 * Server sent signal to start the game. The argument is the colour of the
	 * player who begins.
	 * 
	 * @param startingPlayer
	 *            'b' for black or 'r' for red.
	 * 
	 */
	public void startGame(char startingPlayer) {
		// TODO Display an appropriate message.
		// TODO If this player begins, make move.
		statusDisplayer.setCurrentPlayer(startingPlayer);

	}

	/**
	 * Server complained about an illegal move. The String indicates the reason.
	 * 
	 * @param msg
	 *            Reason for illegal move.
	 * 
	 */
	public void illegalMove(String msg) {
		messagePager.display("Server: illegal move (" + msg + ")");
		// TODO Make move.

	}

	/**
	 * Server sent new board in response to a legal move.
	 * 
	 * @param mapCode
	 * 
	 */
	public void legalMove(String mapCode) {
		// TODO Verify the new board.
		// TODO display that the opponent is now making a move
	}

	/**
	 * Server sent opponents move.
	 * 
	 * @param mapCode
	 * 
	 */
	public void opponentMoved(String mapCode) {
		// TODO display new board
		// TODO Make move

	}

	/**
	 * Server signaled that the game is over and sends the final points.
	 * 
	 * @param pointsBlack
	 * @param pointsRed
	 * 
	 */
	public void endGame(int pointsRed, int pointsBlack) {
		// TODO Display points
		// TODO Prompt for rematch

	}

	/**
	 * Opponent sent a message via the server.
	 * 
	 * @param msg
	 * 
	 */
	public void displayPlayerMessage(String message) {
		messagePager.display(opponentName + ": " + message);
	}

	/**
	 * @param message
	 * 
	 */
	public void unknownMessage(String message) {
		mainframe.displayWarning("Unbekannte Nachricht empfangen:\n" + message);
	}

	private void startGameDisplay() {
		logger.finer("Initializing game display for game: " + game);
		gameDisplayAdapter = new GameDisplayAdapter(this);
		gameDisplay = mainframe.initGameDisplay(game, gameDisplayAdapter);
		// TODO make the game display show the empty board
		game.pushMap();
		mainframe.repaint();
	}

	private Player createPlayer() {
		Owner owner;
		if (playerColour == 'r')
			owner = Owner.Red;
		else
			owner = Owner.Black;
		if (playerHuman)
			return new HumanPlayer(playerName, owner);
		else
			return new CompPlayer(playerName, owner);
	}

	/**
	 * 
	 */
	public void fieldClicked(Pos pos) {
		logger.finer("User clicked on tile at pos " + pos);
		game.placeMarble(pos);
		messageSender.makeMove(pos.getRow(), pos.getCol());
	}

}
