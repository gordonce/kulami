/**
 * 
 */
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

	private String opponentName;
	private ChooseBoardDialog chooseBoardDialog;
	private char playerColour;


	private DisplayFlags displayFlags;

	private static final Logger logger = Logger
			.getLogger("kulami.control.GameController");

	/**
	 * The GameController constructor creates a Mainframe and displays it. It
	 * also requests a MessagePager from the Mainframe so that messages can be
	 * displayed at any time.
	 */
	public GameController() {
		initMainframe();
		displayFlags = new DisplayFlags(false, false, false);
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
	 * 
	 */
	private void showMapEditor() {
		new MapEditorController();
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
				initStatusDisplay();
				messagePager.display("Spiel beginnt");
				statusDisplayer.setCurrentPlayer(colour);
				if (!playerHuman && colour == playerColour) {
					makeMove();
				}
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
				messagePager.display("Server: ungülitger Zug (" + msg + ")");
				if (!playerHuman) {
					makeMove();
				}
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
				statusDisplayer.setCurrentPlayer(playerColour == 'r' ? 'b'
						: 'r');
				statusDisplayer.setHeroMarbles(game.remainingMarbles(playerColour));
			}

			/**
			 * Server sent opponent's move.
			 * 
			 * @param mapCode
			 * 
			 */
			@Override
			public void zug(final String boardCode) {
				try {
					game.updateGame(boardCode);
					statusDisplayer.setCurrentPlayer(playerColour);
					statusDisplayer.setVillainMarbles(game.remainingMarbles(playerColour == 'r' ? 'b' : 'r'));
					if (!playerHuman)
						makeMove();
				} catch (IllegalBoardCode e) {
					mainframe
							.displayWarning("Ungültiges Spielfeld empfangen. Verbindung wird beendet.");
					messageSender.quitGame();
				}
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

	private void startGameDisplay() {
		mainframe.initGameDisplay(game, new GameDisplayAdapter() {

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
		game.pushMap();
		mainframe.enableOptions(true);
	}
	
	private void initStatusDisplay() {
		statusDisplayer.setVillainName(opponentName);
		statusDisplayer.setHeroColour(playerColour);
		statusDisplayer.setVillainColour(playerColour == 'b' ? 'r' : 'b');
		statusDisplayer.setHeroMarbles(28);
		statusDisplayer.setVillainMarbles(28);
	}

	private Player createPlayer() {
		if (playerHuman)
			return new HumanPlayer(playerName, playerColour);
		else
			return new CompPlayer(playerName, playerColour, compPlayerLevel);
	}

	private void makeMove() {
		game.makeMove(new CompPlayerAdapter() {

			@Override
			public void madeMove(Pos pos) {
				messageSender.makeMove(pos.getCol(), pos.getRow());
			}
		});
	}

}
