/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import kulami.control.DisplayFlags;
import kulami.game.board.Board;
import kulami.game.board.GameMap;
import kulami.game.board.IllegalBoardCode;
import kulami.game.board.Marbles;
import kulami.game.board.Owner;
import kulami.game.board.Pos;
import kulami.game.player.Player;
import kulami.gui.GameObserver;

/**
 * The class <code>Game</code> represents the current state of a Kulami game.
 * <p>
 * To construct a <code>Game</code>, a <code>Player</code>, a game level, a
 * <code>DisplayFlags</code> object, and a <code>Board</code> have to be
 * provided.
 * <p>
 * Objects can track the state of a <code>Game</code> by registering as a
 * <code>GameObserver</code>.
 * 
 * @author gordon
 * 
 */
public class Game implements GameObservable {

	private GameMap gameMap;
	private Player player;
	private int gameLevel;
	private DisplayFlags displayFlags;

	private List<GameObserver> gameObservers;

	private static final Logger logger = Logger.getLogger("kulami.game.Game");

	/**
	 * Private constructor that encapsulates common tasks of all public
	 * constructors.
	 * 
	 * @param player
	 * @param level
	 * @param displayFlags
	 */
	private Game(Player player, int level, DisplayFlags displayFlags) {
		this.player = player;
		this.gameLevel = level;
		this.displayFlags = displayFlags;
		gameObservers = new ArrayList<>();
	}

	/**
	 * Constructs a new <code>Game</code> using an existing <code>Board</code>
	 * object.
	 * 
	 * @param board
	 *            the game board
	 * @param player
	 *            the local player
	 * @param level
	 *            game level (0, 1, or 2)
	 * @param displayFlags
	 *            <code>DisplayFlags</code> object reference to be passed on
	 */
	public Game(Board board, Player player, int level, DisplayFlags displayFlags) {
		this(player, level, displayFlags);
		gameMap = new GameMap(board);
	}

	/**
	 * Constructs a new <code>Game</code> using a 200-character board code.
	 * <p>
	 * The board code consists of 100 pairs of letters identifying panels and
	 * numbers identifying owners.
	 * 
	 * @param boardCode
	 *            a 200-character board code
	 * @param player
	 *            the local player
	 * @param level
	 *            game level (0, 1, or 2)
	 * @param displayFlags
	 *            <code>DisplayFlags</code> object reference to be passed on
	 * @throws IllegalBoardCode
	 */
	public Game(String boardCode, Player player, int level,
			DisplayFlags displayFlags) throws IllegalBoardCode {
		this(player, level, displayFlags);
		gameMap = new GameMap(boardCode);
	}

	/**
	 * Places a marble at position <code>pos</code> for the local player and
	 * inform <code>GameObservers</code>.
	 * 
	 * @param pos
	 *            position for the marble
	 */
	public void placeMarble(Pos pos) {
		logger.fine(String.format("%s placed marble at %s.", player, pos));
		gameMap.setOwner(pos, player.getCoulour() == 'r' ? Owner.Red
				: Owner.Black);
		informObservers();
	}

	/**
	 * Given a board code update the marbles.
	 * 
	 * @param boardCode
	 * @throws IllegalBoardCode
	 */
	public void updateGame(String boardCode) throws IllegalBoardCode {
		gameMap.updateGameMap(boardCode);
		informObservers();
	}

	/**
	 * Calculate the current points for the player with colour
	 * <code>playerColour</code>.
	 * 
	 * @param playerColour
	 *            'r' or 'b'
	 * @return current points
	 */
	public int getPoints(char playerColour) {
		return gameMap.getPoints(playerColour, gameLevel);
	}

	/**
	 * Force the game to notify <code>GameObserver</code>s of a changed board.
	 * <p>
	 * This method is useful when first initializing a <code>Game</code>.
	 */
	public void pushMap() {
		for (GameObserver observer : gameObservers)
			observer.boardChanged(this);
	}

	/**
	 * Returns <code>true</code> if position <code>pos</code> is a legal
	 * position for placing the next marble.
	 * 
	 * @param pos
	 *            a position
	 * @return <code>true</code> if position is legal, <code>false</code>
	 *         otherwise
	 */
	public boolean isLegalMove(Pos pos) {
		List<Pos> legalFields = gameMap.getLegalFields();
		return legalFields.contains(pos);
	}

	/**
	 * Returns the 200-character board code.
	 * 
	 * @return the board code
	 */
	public String getBoardCode() {
		return gameMap.getMapCode();
	}

	/**
	 * Returns a reference to the <code>GameMap</code>.
	 * 
	 * @return the <code>GameMap</code>
	 */
	public GameMap getGameMap() {
		return gameMap;
	}

	/**
	 * Returns a reference to the <code>Player</code>.
	 * 
	 * @return the <code>Player</code>
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the game level.
	 * 
	 * @return the game level (0, 1, or 2)
	 */
	public int getLevel() {
		return gameLevel;
	}

	/**
	 * Returns the number of marbles remaining for player with colour
	 * <code>playerColour</code>.
	 * 
	 * @param playerColour
	 *            'r' or 'b'
	 * @return number of remaining marbles
	 */
	public int remainingMarbles(char playerColour) {
		if (playerColour == 'r')
			return gameMap.remainingMarbles(Owner.Red);
		else
			return gameMap.remainingMarbles(Owner.Black);
	}

	/**
	 * Tells the <code>Player</code> to choose a move.
	 * <p>
	 * This method should only be called for <code>CompPlayer</code>s.
	 * 
	 * @param adapter
	 *            <code>CompPlayerAdapter</code> to be called when move was
	 *            chosen
	 */
	public void makeMove(CompPlayerAdapter adapter) {
		Pos pos = player.makeMove(this);
		placeMarble(pos);
		adapter.madeMove(pos);
	}

	/**
	 * Informs the registered <code>GameObserver</code>s that the
	 * <code>DisplayFlags</code> have changed.
	 * 
	 * @param displayFlags
	 */
	public void flagsChanged(DisplayFlags displayFlags) {
		this.displayFlags = displayFlags;
		for (GameObserver observer : gameObservers)
			observer.flagsChanged(this);
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getBoard()
	 */
	@Override
	public Board getBoard() {
		return gameMap.getBoard();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getMarbles()
	 */
	@Override
	public Marbles getMarbles() {
		return gameMap.getMarbles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getLegalMoves()
	 */
	@Override
	public List<Pos> getLegalMoves() {
		return gameMap.getLegalFields();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getPanelOwners()
	 */
	@Override
	public List<Owner> getPanelOwners() {
		return gameMap.getPanelOwners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#registerObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void registerObserver(GameObserver observer) {
		gameObservers.add(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#removeObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void removeObserver(GameObserver observer) {
		gameObservers.remove(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getDisplayFlags()
	 */
	@Override
	public DisplayFlags getDisplayFlags() {
		return displayFlags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.game.GameObservable#getLastMove()
	 */
	@Override
	public Pos getLastMove() {
		return gameMap.getLastMove();
	}

	/**
	 * Inform registered <code>GameObserver</code>s of changes in the game
	 * (usually because a marble has been placed).
	 */
	private void informObservers() {
		for (GameObserver observer : gameObservers)
			observer.gameChanged(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Game with map: \n" + gameMap;
	}

}
