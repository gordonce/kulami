package kulami.control;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import kulami.connectivity.InProtocolObserver;
import kulami.connectivity.MessageSender;
import kulami.connectivity.ServerAdapter;
import kulami.connectivity.ServerProxy;
import kulami.game.CompPlayerAdapter;
import kulami.game.Game;
import kulami.game.board.IllegalBoardCode;
import kulami.game.board.Pos;
import kulami.game.player.CompPlayer;
import kulami.game.player.HumanPlayer;
import kulami.game.player.Player;
import kulami.gui.ChooseBoardDialog;
import kulami.gui.ChooseBoardDialogAdapter;
import kulami.gui.GameDisplayAdapter;
import kulami.gui.Mainframe;
import kulami.gui.MainframeAdapter;
import kulami.gui.MessagePager;
import kulami.gui.NewGameDialog;
import kulami.gui.NewGameDialogAdapter;
import kulami.gui.PlayerDialog;
import kulami.gui.PlayerDialogAdapter;
import kulami.gui.StatusDisplayer;

/**
 * The <code>GameController</code> is the layer between the GUI and the game
 * state.
 * <p>
 * The GUI delegates all user input to the controller and lets the controller
 * decide what to do. The controller also receives Kulami server messages. The
 * controller decides which actions to take and manipulates the game model.
 * <p>
 * The GameController's only public element is its constructor. Messages are
 * received in anonymous classes that implement adapter interfaces.
 * 
 * @author gordon
 * 
 */
public class GameController {

	// GUI elements
	private Mainframe mainframe;

	private PlayerDialog playerDialog;
	private NewGameDialog newGameDialog;
	private ChooseBoardDialog chooseBoardDialog;

	private MessagePager messagePager;
	private StatusDisplayer statusDisplayer;

	private DisplayFlags displayFlags;

	// Connectivity elements
	private ServerProxy serverProxy;
	private ServerAdapter serverAdapter;
	private MessageSender messageSender;

	// Game elements
	private Game game;
	private String playerName;
	private boolean playerHuman;
	private char playerColour;
	private int compPlayerLevel;
	private String opponentName;

	private boolean gameRunning;

	private static final Logger logger = Logger
			.getLogger("kulami.control.GameController");

	/**
	 * Constructs a <code>GameController</code>, initializes a
	 * <code>Mainframe</code> object and displays it.
	 * <p>
	 * The constructor also requests a <code>MessagePager</code> and a
	 * <code>StatusDisplayer</code> from the <code>Mainframe</code> to display
	 * information to the user
	 */
	public GameController() {
		initMainframe();

		displayFlags = new DisplayFlags(false, false, false);
		gameRunning = false;

		messagePager = mainframe.getMessageDisplay();
		statusDisplayer = mainframe.getStatusDisplay();
		mainframe.setVisible(true);
	}

	/**
	 * Initializes the <code>Mainframe</code>.
	 * <p>
	 * An anonymous inner class that implements the interface
	 * <code>MainframeAdapter</code> handles user input at the mainframe.
	 */
	private void initMainframe() {
		mainframe = new Mainframe(new MainframeAdapter() {

			@Override
			public void startGameClicked() {
				showPlayerDialog();
			}

			@Override
			public void previousMovesDeactivated() {
				displayFlags.setPreviousMoves(false);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void previousMovesActivated() {
				displayFlags.setPreviousMoves(true);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void possibleMovesDeactivated() {
				displayFlags.setPossibleMoves(false);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void possibleMovesActivated() {
				displayFlags.setPossibleMoves(true);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void newGameMapClicked() {
				showMapEditor();
			}

			@Override
			public void messageEntered(String message) {
				messagePager.display(String.format("%s: %s", playerName, message));
				messageSender.sendMessage(message);
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
				displayFlags.setPanelPossession(false);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void boardPossessionActivated() {
				displayFlags.setPanelPossession(true);
				game.flagsChanged(displayFlags);
			}

			@Override
			public void abortGameClicked() {
				if (messageSender != null) {
					boolean really = mainframe.yesNoQuestion(
							"Spiel wirklich abbrechen?", "Spiel abbrechen");
					if (really)
						messageSender.quitGame();
				}
			}

			@Override
			public void exitClicked() {
				boolean reallyExit = mainframe.yesNoQuestion("Kulami beenden?",
						"Kulami");
				if (reallyExit)
					System.exit(0);
			}
		});
	}

	/**
	 * Shows the <code>MapEditor</code> by creating a new
	 * <code>MapEditorController</code>.
	 */
	private void showMapEditor() {
		new MapEditorController();
	}

	/**
	 * Displays the <code>PlayerDialog</code> which prompts the user to enter a
	 * user name and decide whether to play in human or in computer mode.
	 * <p>
	 * An anonymous inner class that implements the
	 * <code>PlayerDialogAdapter</code> interface handles user input from the
	 * <code>PlayerDialog</code>.
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

				showNewGameDialog();
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
	 * Displays the <code>NewGameDialog</code> which prompts the user for a
	 * Kulami server connection.
	 * <p>
	 * An anonymous inner class that implements the
	 * <code>NewGameDialogAdapter</code> interface handles user input from the
	 * dialog.
	 * <p>
	 * The <code>PlayerDialog</code> should have been displayed before.
	 */
	private void showNewGameDialog() {
		newGameDialog = new NewGameDialog(mainframe,
				new NewGameDialogAdapter() {

					@Override
					public void connectClicked() {
						String hostName = newGameDialog.getHost();
						int port = newGameDialog.getPort();

						serverProxy = new ServerProxy(hostName, port);
						serverAdapter = new ServerAdapter();

						registerInProtocolObserver();

						serverProxy.addObserver(serverAdapter);

						try {
							serverProxy.connectAndListen();
							messageSender = new MessageSender(serverProxy);
						} catch (UnknownHostException e) {
							newGameDialog
									.displayWarning(
											"Unbekannter Server. Bitte Verbindungsdaten überprüfen.",
											"Fehler beim Verbinden");
							serverProxy.disconnect();
						} catch (IOException e) {
							newGameDialog.displayWarning(
									"Fehler beim Verbinden mit dem Server: "
											+ e.getMessage(),
									"Fehler beim Verbinden");
							serverProxy.disconnect();
						}
					}

					@Override
					public void cancelClicked() {
						newGameDialog.clearAndHide();
					}
				});
		newGameDialog.setVisible(true);
	}

	/**
	 * Displays the <code>ChooseBoardDialog</code> which prompts the user to
	 * choose a board from a file and a level for counting of points.
	 * <p>
	 * An anonymous inner class that implements the
	 * <code>ChoosBoardDialogAdapter</code> interface handles user input from
	 * the dialog.
	 * <p>
	 * This method should be called when the user connected to the server first
	 * and the server requested game parameters.
	 */
	private void showChooseBoardDialog() {
		chooseBoardDialog = new ChooseBoardDialog(mainframe,
				new ChooseBoardDialogAdapter() {

					@Override
					public void okClicked() {
						String boardCode = chooseBoardDialog.getBoardCode();
						int level = chooseBoardDialog.getLevel();

						// GameMap board = new GameMap(boardCode);
						// board.clearOwners();

						chooseBoardDialog.clearAndHide();

						try {
							game = new Game(boardCode, createPlayer(), level,
									displayFlags);
							messageSender.sendParameters(game.getBoardCode(),
									level);
							startGameDisplay();
						} catch (IllegalBoardCode e) {
							mainframe.displayWarning("Die Datei enthält kein"
									+ "gültiges Spielfeld");
							showChooseBoardDialog();
						}
					}

					@Override
					public void cancelClicked() {
						chooseBoardDialog.clearAndHide();
						messageSender.quitGame();
						serverProxy.disconnect();
					}
				});
		chooseBoardDialog.setVisible(true);
	}

	/**
	 * Registers an anonymous inner class that implements the
	 * <code>InProtocolObserver</code> interface with the
	 * <code>ServerAdapter</code>.
	 * <p>
	 * The anonymous inner class handles all incoming server messages and
	 * initiates appropriate action.
	 */
	private void registerInProtocolObserver() {
		serverAdapter.registerObserver(new InProtocolObserver() {

			@Override
			public void kulamiQ() {
				newGameDialog.clearAndHide();
				messageSender.newClient(playerName);
			}

			@Override
			public void message(String msg) {
				messagePager.display("Server: " + msg);
			}

			@Override
			public void spielparameterQ() {
				showChooseBoardDialog();
			}

			@Override
			public void spielparameter(String boardCode, int level,
					char colour, String opponentName) {
				playerColour = colour;
				try {
					game = new Game(boardCode, createPlayer(), level,
							displayFlags);
					GameController.this.opponentName = opponentName;

					startGameDisplay();
					initStatusDisplay();
				} catch (IllegalBoardCode e) {
					mainframe.displayWarning("Ungültiges Spielfeld erhalten.");
				}
			}

			@Override
			public void name(String opponentName) {
				GameController.this.opponentName = opponentName;
				statusDisplayer.setVillainName(opponentName);
			}

			@Override
			public void farbe(char colour) {
				playerColour = colour;
				statusDisplayer.setHeroColour(colour);
				statusDisplayer.setVillainColour(colour == 'b' ? 'r' : 'b');
				messagePager.display("Warte auf Spieler 2...");
			}

			@Override
			public void spielstart(char colour) {
				initStatusDisplay();
				messagePager.display("Spiel beginnt");
				gameRunning = true;
				statusDisplayer.setCurrentPlayer(colour);
				if (!playerHuman && colour == playerColour) {
					makeMove();
				}
			}

			@Override
			public void ungueltig(String msg) {
				messagePager.display("Server: ungülitger Zug (" + msg + ")");
				if (!playerHuman) {
					makeMove();
				}
			}

			@Override
			public void gueltig(String boardCode) {
				// TODO Verify the new board.
				statusDisplayer.setCurrentPlayer(playerColour == 'r' ? 'b'
						: 'r');
				updateStatusDisplay();

			}

			@Override
			public void zug(final String boardCode) {
				try {
					game.updateGame(boardCode);
					statusDisplayer.setCurrentPlayer(playerColour);
					updateStatusDisplay();
					if (!playerHuman)
						makeMove();
				} catch (IllegalBoardCode e) {
					mainframe
							.displayWarning("Ungültiges Spielfeld empfangen. Verbindung wird beendet.");
					messageSender.quitGame();
				}
			}

			@Override
			public void spielende(int pointsRed, int pointsBlack) {
				gameRunning = false;
				boolean newGame;
				if (playerColour == 'r')
					newGame = mainframe.displayResults(pointsRed, pointsBlack);
				else
					newGame = mainframe.displayResults(pointsBlack, pointsRed);
				if (newGame)
					messageSender.newGame();
				else
					messageSender.quitGame();
			}

			@Override
			public void playerMessage(String msg) {
				messagePager.display(opponentName + ": " + msg);
			}

			@Override
			public void unknownMessage(String msg) {
				mainframe.displayWarning("Unbekannte Nachricht empfangen:\n"
						+ msg + "\nVerbindung wird beendet.");
				messageSender.quitGame();
			}

			@Override
			public void connectionError() {
				mainframe
						.displayWarning("Es ist ein Fehler bei der Verbindung aufgetreten. Das Spiel wird beendet.");
			}
		});
	}

	/**
	 * Tells the mainframe to initialize the
	 * <code>GameDisplay> with the current game.
	 * <p>
	 * An anonymous inner class that implements the <code>GameDisplayAdapter</code>
	 * interface handles user clicks on the board display.
	 * <p>
	 * Before calling this method, a <code>Game</code> object has to be assigned
	 * to the game field.
	 */
	private void startGameDisplay() {
		mainframe.initGameDisplay(game, new GameDisplayAdapter() {

			@Override
			public void tileClicked(Pos pos) {
				if (gameRunning) {
					logger.finer("User clicked on tile at pos " + pos);
					if (game.isLegalMove(pos)) {
						logger.fine("field is legal");
						game.placeMarble(pos);
						messageSender.makeMove(pos.getCol(), pos.getRow());
					} else {
						logger.fine("field is illegal");
					}
				}
			}
		});
		game.pushMap();
		mainframe.enableOptions(true);
	}

	/**
	 * Sets the status display to its initial values.
	 */
	private void initStatusDisplay() {
		statusDisplayer.setVillainName(opponentName);
		statusDisplayer.setHeroColour(playerColour);
		statusDisplayer.setVillainColour(playerColour == 'b' ? 'r' : 'b');
		statusDisplayer.setHeroMarbles(28);
		statusDisplayer.setVillainMarbles(28);
		statusDisplayer.setHeroPoints(0);
		statusDisplayer.setVillainPoints(0);
	}

	/**
	 * Updates the number of remaining marbles and current points in the status
	 * display.
	 */
	private void updateStatusDisplay() {
		char villainColour = playerColour == 'r' ? 'b' : 'r';
		statusDisplayer.setHeroMarbles(game.remainingMarbles(playerColour));
		statusDisplayer.setVillainMarbles(game.remainingMarbles(villainColour));
		statusDisplayer.setHeroPoints(game.getPoints(playerColour));
		statusDisplayer.setVillainPoints(game.getPoints(villainColour));
	}

	/**
	 * Creates a new <code>Player</code> object.
	 * <p>
	 * The fields <code>playerHuman</code>, <code>playerName</code>,
	 * <code>playerColour</code>, and <code>compPlayerLevel</code> have to be
	 * initialized before calling this method.
	 * 
	 * @return a <code>HumanPlayer</code> or a <code>CompPlayer</code>
	 */
	private Player createPlayer() {
		if (playerHuman)
			return new HumanPlayer(playerName, playerColour);
		else
			return new CompPlayer(playerName, playerColour, compPlayerLevel);
	}

	/**
	 * Tell a <code>CompPlayer</code> to make a move.
	 * <p>
	 * An anonymous inner class that implements the
	 * <code>CompPlayerAdapter</code> interface is used to receive the chosen
	 * move.
	 */
	private void makeMove() {
		game.makeMove(new CompPlayerAdapter() {

			@Override
			public void madeMove(Pos pos) {
				messageSender.makeMove(pos.getCol(), pos.getRow());
			}
		});
	}

}
