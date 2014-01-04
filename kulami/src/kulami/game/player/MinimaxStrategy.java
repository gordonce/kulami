/**
 * 
 */
package kulami.game.player;

import java.util.List;

import kulami.game.Game;
import kulami.game.board.GameMap;
import kulami.game.board.Owner;
import kulami.game.board.Pos;

/**
 * @author gordon
 * 
 */
public class MinimaxStrategy implements KulamiStrategy {

	/**
	 * Negative infinity is set to the number of points for all panels plus 1
	 */
	private static final int POSITIVE_INFINITY = 65;
	/**
	 * Negative infinity is set to the number of points for all panels plus 1
	 * negated
	 */
	private static final int NEGATIVE_INFINITY = -65;
	private int level;
	private Pos savedMove;
	private Game game;
	private char thisColour;

	public MinimaxStrategy(int level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kulami.game.player.KulamiStrategy#choosePos(kulami.game.board.GameMap)
	 */
	@Override
	public Pos choosePos(Game game) {
		this.game = game;
		thisColour = game.getPlayer().getCoulour();
		savedMove = null;
		max(1, level, game.getGameMap().getCopy());
		return savedMove;
	}

	/**
	 * @param player
	 * @param depth
	 * @param gameMap
	 * @return
	 */
	private int max(int player, int depth, GameMap gameMap) {
		if (depth == 0 || !hasMarbles(player, gameMap)) {
			return evaluate(player, gameMap);
		}
		int maxVal = NEGATIVE_INFINITY;
		List<Pos> possibleMoves = generatePossibleMoves(player, gameMap);
		for (Pos move : possibleMoves) {
			GameMap nextGameMap = performMove(player, move, gameMap);
			int value = min(-player, depth - 1, nextGameMap);
			System.out.println(String.format("Pos: %s Value: %d ", move, value));
			if (value > maxVal) {
				maxVal = value;
				if (depth == level)
					savedMove = move;
			}
		}
		return maxVal;
	}

	/**
	 * @param player
	 * @param depth
	 * @param gameMap
	 * @return
	 */
	private int min(int player, int depth, GameMap gameMap) {
		if (depth == 0 || !hasMarbles(player, gameMap)) {
			return evaluate(player, gameMap);
		}
		int minVal = POSITIVE_INFINITY;
		List<Pos> possibleMoves = generatePossibleMoves(player, gameMap);
		for (Pos move : possibleMoves) {
			GameMap nextGameMap = performMove(player, move, gameMap);
			int value = max(-player, depth - 1, nextGameMap);
			if (value < minVal) {
				minVal = value;
			}
		}
		return minVal;
	}

	/**
	 * Copy the GameMap an perform the move.
	 * 
	 * @param move
	 *            position where to place the marble
	 * @param player
	 *            1 for this player, -1 for opponent
	 * @param gameMap
	 *            old GameMap
	 * @return new GameMap
	 */
	private GameMap performMove(int player, Pos move, GameMap gameMap) {
		GameMap newGameMap = gameMap.getCopy();
		newGameMap.setOwner(move, getOwner(player));
		return newGameMap;
	}

	/**
	 * @param player
	 * @return
	 */
	private Owner getOwner(int player) {
		if (player == 1)
			return game.getPlayer().getCoulour() == 'r' ? Owner.Red
					: Owner.Black;
		else
			return game.getPlayer().getCoulour() == 'r' ? Owner.Black
					: Owner.Red;
	}

	/**
	 * @param player
	 * @param gameMap
	 * @return
	 */
	private List<Pos> generatePossibleMoves(int player, GameMap gameMap) {
		return gameMap.getLegalFields();
	}

	/**
	 * @param gameMap
	 * @return
	 */
	private int evaluate(int player, GameMap gameMap) {
		return gameMap.getPoints(getOwner(1), game.getLevel());
	}

	/**
	 * @param player
	 * @return
	 */
	private boolean hasMarbles(int player, GameMap gameMap) {
		return gameMap.remainingMarbles(getOwner(player)) > 0;
	}
}
