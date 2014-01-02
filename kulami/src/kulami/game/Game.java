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
 * @author gordon
 *
 */
public class Game implements GameObservable {

	private GameMap gameMap;
	private Player player;
	private int gameLevel;
	
	private List<GameObserver> gameObservers;
	private DisplayFlags displayFlags;
	
	private static final Logger logger = Logger.getLogger("kulami.game.Game");
	
	private Game(Player player, int level, DisplayFlags displayFlags) {
		this.player = player;
		this.gameLevel = level;
		this.displayFlags = displayFlags;
		gameObservers = new ArrayList<>();
	}
	/**
	 * @param board
	 * @param player
	 */
	public Game(Board board, Player player, int level, DisplayFlags displayFlags) {
		this(player, level, displayFlags);
		gameMap = new GameMap(board);
	}
	
	public Game(String boardCode, Player player, int level, DisplayFlags displayFlags) throws IllegalBoardCode {
		this(player, level, displayFlags);
		gameMap = new GameMap(boardCode);
	}
	
	public void placeMarble(Pos pos) {
		logger.fine(String.format("%s placed marble at %s.", player, pos));
		gameMap.setOwner(pos, player.getCoulour() == 'r' ? Owner.Red : Owner.Black);
		informObservers();
	}
	
	/**
	 * Given a map code update the marbles.
	 * 
	 * @param mapCode
	 * @throws IllegalBoardCode 
	 */
	public void updateGame(String boardCode) throws IllegalBoardCode {
		gameMap.updateGameMap(boardCode);
		informObservers();
	}
	
	public int getPoints(char playerColour) {
		return gameMap.getPoints(playerColour, gameLevel);
	}
	
	public void pushMap() {
		for (GameObserver observer: gameObservers)
			observer.boardChanged(this);
	}
	
	public boolean isLegalMove(Pos pos) {
		List<Pos> legalFields = gameMap.getLegalFields();
		return legalFields.contains(pos);
	}
	
	public String getBoardCode() {
		return gameMap.getMapCode();
	}
	
	public Marbles copyMarbles() {
		return gameMap.copyMarbles();
	}
	
	public GameMap getGameMap() {
		return gameMap;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getLevel() {
		return gameLevel;
	}
	
	@Override
	public Board getBoard() {
		return gameMap.getBoard();
	}

	@Override
	public Marbles getMarbles() {
		return gameMap.getMarbles();
	}
	
	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#getLegalMoves()
	 */
	@Override
	public List<Pos> getLegalMoves() {
		return gameMap.getLegalFields();
	}

	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#getPanelOwners()
	 */
	@Override
	public List<Owner> getPanelOwners() {
		return gameMap.getPanelOwners();
	}

	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#registerObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void registerObserver(GameObserver observer) {
		gameObservers.add(observer);
	}

	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#removeObserver(kulami.gui.GameObserver)
	 */
	@Override
	public void removeObserver(GameObserver observer) {
		gameObservers.remove(observer);
	}
	
	private void informObservers() {
		for (GameObserver observer: gameObservers)
			observer.gameChanged(this);
	}

	@Override
	public String toString() {
		return "Game with map: \n" + gameMap;
	}
	
	/**
	 * 
	 */
	public void makeMove(CompPlayerAdapter adapter) {
		Pos pos = player.makeMove(this);
		placeMarble(pos);
		adapter.madeMove(pos);
	}
	/**
	 * @param displayFlags
	 */
	public void flagsChanged(DisplayFlags displayFlags) {
		this.displayFlags = displayFlags;
		for (GameObserver observer: gameObservers)
			observer.flagsChanged(this);
		
	}
	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#getDisplayFlags()
	 */
	@Override
	public DisplayFlags getDisplayFlags() {
		return displayFlags;
	}
	/* (non-Javadoc)
	 * @see kulami.game.GameObservable#getLastMove()
	 */
	@Override
	public Pos getLastMove() {
		return gameMap.getLastMove();
	}
	
	
	
}
