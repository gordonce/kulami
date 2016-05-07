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
 * This class implements the minimax strategy for choosing a position for the
 * next move.
 * 
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

	/**
	 * The strategy is parametrized with a level.
	 * 
	 * @param level
	 *            1..10
	 */
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
		// thisColour = game.getPlayer().getCoulour();
		savedMove = null;
		max(1, level, game.getGameMap().getCopy());
		return savedMove;
	}

	/**
	 * Choose the move that maximizes the value of the current player.
	 * <p>
	 * As a side effect sets the <code>savedMoved</code> in its first call.
	 * 
	 * @param player
	 *            1 or -1
	 * @param depth
	 *            0..10
	 * @param gameMap
	 *            the GameMap
	 * @return value
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
			System.out
					.println(String.format("Pos: %s Value: %d ", move, value));
			if (value > maxVal) {
				maxVal = value;
				if (depth == level)
					savedMove = move;
			}
		}
		return maxVal;
	}

	/**
	 * Choose the move that minimizes the value of the current player.
	 * 
	 * @param player
	 *            1 or -1
	 * @param depth
	 *            0..10
	 * @param gameMap
	 *            the GameMap
	 * @return value
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
	 * Copy the GameMap and perform the move.
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
	 * Returns the <code>Owner</code> corresponding to the minimax algorithms
	 * player encoding.
	 * 
	 * @param player
	 *            1 or -1
	 * @return the Owner
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
	 * Returns a list of legal moves.
	 * 
	 * @param player
	 * @param gameMap
	 * @return list of positions
	 */
	private List<Pos> generatePossibleMoves(int player, GameMap gameMap) {
		return gameMap.getLegalFields();
	}

	/**
	 * Evaluates the benefit for a player when performing a move by counting
	 * his/her points.
	 * 
	 * @param player
	 *            1 or -1
	 * @param gameMap
	 * @return number of points
	 */
	private int evaluate(int player, GameMap gameMap) {
		return gameMap.getPoints(getOwner(1), game.getLevel());
	}

	/**
	 * Check whether player has any marbles left.
	 * 
	 * @param player
	 *            1 or -1
	 * @return <code>true</code> if player has moves left
	 */
	private boolean hasMarbles(int player, GameMap gameMap) {
		return gameMap.remainingMarbles(getOwner(player)) > 0;
	}
}
