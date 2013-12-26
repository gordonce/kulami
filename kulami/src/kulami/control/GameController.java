/**
 * 
 */
package kulami.control;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import kulami.connectivity.InProtocolObserver;
import kulami.connectivity.MessageSender;
import kulami.connectivity.ServerAdapter;
import kulami.connectivity.ServerProxy;
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
import kulami.gui.MapEditor;
import kulami.gui.MapEditorAdapter;
import kulami.gui.MessagePager;
import kulami.gui.NewGameDialog;
import kulami.gui.NewGameDialogAdapter;
import kulami.gui.Orientation;
import kulami.gui.PlayerDialog;
import kulami.gui.PlayerDialogAdapter;
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

	private PlayerDialog playerDialog;

	private NewGameDialog newGameDialog;

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
	private ChooseBoardDialog chooseBoardDialog;
	private GameDisplayAdapter gameDisplayAdapter;
	private char playerColour;

	private MapEditor mapEditor;

	private static final Logger logger = Logger
			.getLogger("kulami.control.GameController");

	/**
	 * The GameController constructor creates a Mainframe and displays it. It
	 * also requests a MessagePager from the Mainframe so that messages can be
	 * displayed at any time.
	 */
	public GameController() {
		initMainframe();
		messagePager = mainframe.getMessageDisplay();
		statusDisplayer = mainframe.getStatusDisplay();
		mainframe.setVisible(true);
	}

	/**
	 * @return
	 */
	private void initMainframe() {
		mainframe = new Mainframe(new MainframeAdapter() {

			@Override
			public void startGameClicked() {
				showNewGameDialog();
			}

			@Override
			public void previousMovesDeactivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void previousMovesActivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void possibleMovesDeactivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void possibleMovesActivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void newPlayerClicked() {
				showPlayerDialog();
			}

			@Override
			public void newGameMapClicked() {
				showMapEditor();
			}

			@Override
			public void messageEntered(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadGameMapClicked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void editGameMapClicked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void boardPossessionDeactivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void boardPossessionActivated() {
				// TODO Auto-generated method stub

			}

			@Override
			public void abortGameClicked() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 
	 */
	private void showMapEditor() {
		// TODO Auto-generated method stub
		mapEditor = new MapEditor(new MapEditorAdapter() {

			@Override
			public void newPanelSelected(int size, Orientation orientation) {
				// TODO Auto-generated method stub
				logger.fine(String.format("Inserting panel size %d (%s)", size, orientation));
			}

			@Override
			public void saveMap() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void closeMapEditor() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Display the New Player dialog which prompts the user to enter a user name
	 * and decide whether to play in human or in computer mode.
	 */
	private void showPlayerDialog() {
		playerDialog = new PlayerDialog(mainframe, new PlayerDialogAdapter() {

			@Override
			public void okPressed() {
				playerName = playerDialog.getName();
				playerHuman = playerDialog.getHuman();
				compPlayerLevel = playerDialog.getCompLevel();

				playerDialog.clearAndHide();

				statusDisplayer.setHeroName(playerName);
			}

			@Override
			public void cancelPressed() {
				playerDialog.clearAndHide();
			}

			@Override
			public void windowClosed() {
				playerDialog.clearAndHide();
			}

		});
		playerDialog.setVisible(true);
	}

	/**
	 * Display the New Game dialog which prompts the user for a Kulami server
	 * connection.
	 */
	private void showNewGameDialog() {
		newGameDialog = new NewGameDialog(mainframe,
				new NewGameDialogAdapter() {

					/**
					 * Get Kulami server connection data from the New Game
					 * dialog and try to connect to the server.
					 */
					@Override
					public void connectClicked() {
						String hostName = newGameDialog.getHost();
						int port = newGameDialog.getPort();

						serverProxy = new ServerProxy(hostName, port);
						serverAdapter = new ServerAdapter();

						registerInProtocolObserver();

						serverProxy.addObserver(serverAdapter);

						serverProxy.connectAndListen();

						messageSender = new MessageSender(serverProxy);

						// TODO display error message if connection fails
					}

					@Override
					public void cancelClicked() {
						newGameDialog.clearAndHide();
					}
				});
		newGameDialog.setVisible(true);
	}

	private void showChooseBoardDialog() {
		chooseBoardDialog = new ChooseBoardDialog(mainframe,
				new ChooseBoardDialogAdapter() {

					@Override
					public void okClicked() {
						String boardCode = chooseBoardDialog.getBoardCode();
						int level = chooseBoardDialog.getLevel();

						GameMap board = new GameMap(boardCode);
						board.clearOwners();

						messageSender.sendParameters(board.getMapCode(), level);

						chooseBoardDialog.clearAndHide();

						game = new Game(board, createPlayer(), level);
						startGameDisplay();
					}

					@Override
					public void cancelClicked() {
						chooseBoardDialog.clearAndHide();
						serverProxy.disconnect();
					}
				});
		chooseBoardDialog.setVisible(true);
	}

	private void registerInProtocolObserver() {
		serverAdapter.registerObserver(new InProtocolObserver() {

			/**
			 * Server sent "Kulami?" Connection was successfully established.
			 * Continue protocol by sending the user name.
			 */
			@Override
			public void kulamiQ() {
				newGameDialog.clearAndHide();
				messageSender.newClient(playerName);
			}

			/**
			 * Server sent a message. The message is displayed to the user.
			 */
			@Override
			public void message(String msg) {
				messagePager.display("Server: " + msg);
			}

			/**
			 * Server asked for game parameters. (We are player 1.)
			 */
			@Override
			public void spielparameterQ() {
				showChooseBoardDialog();
			}

			/**
			 * Server sent the game parameters. (We are player 2.) Create a
			 * GameMap, a Player, and a Game and start displaying the game.
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
			@Override
			public void spielparameter(String boardCode, int level,
					char colour, String opponentName) {
				GameMap gameMap = new GameMap(boardCode);
				gameMap.clearOwners();
				playerColour = colour;
				game = new Game(gameMap, createPlayer(), level);
				GameController.this.opponentName = opponentName;
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
			@Override
			public void name(String opponentName) {
				GameController.this.opponentName = opponentName;
				statusDisplayer.setVillainName(opponentName);
			}

			/**
			 * Server sent colour. (We are player 1.) The Player object and the
			 * Game object can now be created and the display adjusted.
			 * 
			 * @param colour
			 *            'b' for black or 'r' for red.
			 * 
			 */
			@Override
			public void farbe(char colour) {
				// TODO Auto-generated method stub
				statusDisplayer.setHeroColour(colour);
				statusDisplayer.setVillainColour(colour == 'b' ? 'r' : 'b');
				playerColour = colour;
			}

			/**
			 * Server sent signal to start the game. The argument is the colour
			 * of the player who begins.
			 * 
			 * @param startingPlayer
			 *            'b' for black or 'r' for red.
			 * 
			 */
			@Override
			public void spielstart(char colour) {
				// TODO Display an appropriate message.
				// TODO If this player begins, make move.
				statusDisplayer.setCurrentPlayer(colour);
			}

			/**
			 * Server complained about an illegal move. The String indicates the
			 * reason.
			 * 
			 * @param msg
			 *            Reason for illegal move.
			 * 
			 */
			@Override
			public void ungueltig(String msg) {
				messagePager.display("Server: illegal move (" + msg + ")");
				// TODO Make move.
			}

			/**
			 * Server sent new board in response to a legal move.
			 * 
			 * @param mapCode
			 * 
			 */
			@Override
			public void gueltig(String boardCode) {
				// TODO Verify the new board.
				// TODO display that the opponent is now making a move
				statusDisplayer.setCurrentPlayer(playerColour == 'r' ? 'b'
						: 'r');
			}

			/**
			 * Server sent opponents move.
			 * 
			 * @param mapCode
			 * 
			 */
			@Override
			public void zug(final String boardCode) {
				game.updateGame(boardCode);
				// TODO display new board
				// TODO Make move
				statusDisplayer.setCurrentPlayer(playerColour);
			}

			/**
			 * Server signaled that the game is over and sends the final points.
			 * 
			 * @param pointsBlack
			 * @param pointsRed
			 * 
			 */
			@Override
			public void spielende(int pointsRed, int pointsBlack) {
				// TODO Display points
				// TODO Prompt for rematch
			}

			/**
			 * Opponent sent a message via the server.
			 * 
			 * @param msg
			 * 
			 */
			@Override
			public void playerMessage(String msg) {
				messagePager.display(opponentName + ": " + msg);
			}

			@Override
			public void unknownMessage(String msg) {
				mainframe.displayWarning("Unbekannte Nachricht empfangen:\n"
						+ msg);
			}
		});
	}

	private void startGameDisplay() {
		logger.finer("Initializing game display for game: " + game);
		gameDisplay = mainframe.initGameDisplay(game, new GameDisplayAdapter() {

			@Override
			public void tileClicked(Pos pos) {
				logger.finer("User clicked on tile at pos " + pos);
				if (game.isLegalMove(pos)) {
					logger.fine("field is legal");
					game.placeMarble(pos);
					messageSender.makeMove(pos.getCol(), pos.getRow());
				} else {
					logger.fine("field is illegal");
				}
			}
		});
		// TODO make the game display show the empty board
		game.pushMap();
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

}
