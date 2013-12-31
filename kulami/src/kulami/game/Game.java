/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
	private int level;
	
	private List<GameObserver> gameObservers;
	
	private static final Logger logger = Logger.getLogger("kulami.game.Game");
	
	private Game(Player player, int level) {
		this.player = player;
		this.level = level;
		gameObservers = new ArrayList<>();
	}
	/**
	 * @param board
	 * @param player
	 */
	public Game(Board board, Player player, int level) {
		this(player, level);
		gameMap = new GameMap(board);
	}
	
	public Game(String boardCode, Player player, int level) throws IllegalBoardCode {
		this(player, level);
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
		return gameMap.getPoints(playerColour, level);
	}
	
	public void pushMap() {
		for (GameObserver observer: gameObservers)
			observer.boardChanged(this);
	}
	
	public boolean isLegalMove(Pos pos) {
		List<Pos> legalFields = gameMap.getLegalFields();
		logger.finest("legal fields:");
		for (Pos p: legalFields)
			logger.finest(p.toString());
		return legalFields.contains(pos);
	}
	
	public String getBoardCode() {
		return gameMap.getMapCode();
	}
	
	public Marbles copyMarbles() {
		return gameMap.copyMarbles();
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
	
	
	
}
