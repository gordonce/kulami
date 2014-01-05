/**
 * 
 */
package kulami.game.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import kulami.game.board.Panel.PanelNotPlacedException;
import kulami.game.board.Panel.PanelOutOfBoundsException;

/**
 * GameMap represents a board of 17 panels together with its current
 * configuration of marbles.
 * 
 * @author gordon
 * 
 */
public class GameMap {

	private Board board;
	private Marbles marbles;

	private Pos lastMove;
	private Pos nextToLastMove;

	private int redMarbles;
	private int blackMarbles;

	private static final Logger logger = Logger
			.getLogger("kulami.game.board.GameMap");

	/**
	 * Private constructor that encapsulates general initialization tasks.
	 * <p>
	 * Creates a <code>Marbles</code> object and sets the marbles counter for
	 * each player to 28.
	 */
	private GameMap() {
		marbles = new Marbles();
		redMarbles = 28;
		blackMarbles = 28;
	}

	/**
	 * Constructs an empty GameMap given a <code>Board</code>.
	 * 
	 * @param board
	 */
	public GameMap(Board board) {
		this();
		this.board = board;
	}

	/**
	 * Construct a <code>GameMap</code> given a 200-character board code.
	 * 
	 * @param BoardCode
	 * @throws IllegalBoardCode
	 */
	public GameMap(String boardCode) throws IllegalBoardCode {
		this();
		board = new Board();
		BoardParser.getBoard(boardCode, board);
	}

	/**
	 * Returns a copy of the <code>GameMap</code>.
	 * <p>
	 * The returned object can be used to manipulate the game without altering
	 * the real game state.
	 * 
	 * @return the new <code>GameMap</code>
	 */
	public GameMap getCopy() {
		GameMap gameMap = new GameMap(board);
		gameMap.marbles = new Marbles(marbles);
		gameMap.lastMove = lastMove;
		gameMap.nextToLastMove = nextToLastMove;
		gameMap.blackMarbles = blackMarbles;
		gameMap.redMarbles = redMarbles;
		return gameMap;
	}

	/**
	 * Set the owner of all fields to None, erase the history and reset the
	 * numbers of marbles.
	 */
	public void clearOwners() {
		marbles.setAllNone();
		lastMove = null;
		nextToLastMove = null;
		blackMarbles = 28;
		redMarbles = 28;
	}

	/**
	 * Get all the positions where it is legal to place a marble in the next
	 * move.
	 * 
	 * @return a list of positions.
	 */
	public ArrayList<Pos> getLegalFields() {
		ArrayList<Pos> legalMoves = new ArrayList<>();
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Pos pos = Pos.getPos(row, col);
				if (isLegal(pos))
					legalMoves.add(pos);
			}
		return legalMoves;

	}

	/**
	 * Returns <code>true</code> if it is legal to place a marble at position
	 * <code>pos</code>.
	 * 
	 * @param pos
	 * @return <code>true</code> if <code>pos</code> is legal
	 */
	private boolean isLegal(Pos pos) {
		Panel thisPanel = board.getPanel(pos);
		// Is there a panel on the field?
		if (thisPanel == null) {
			return false;
		}
		// Is the field empty?
		if (marbles.getMarble(pos) != Owner.None)
			return false;
		// Same row or column but not on same panel as last move?
		if (lastMove != null) {
			Panel lastPanel = board.getPanel(lastMove);
			if ((pos.getCol() != lastMove.getCol() && pos.getRow() != lastMove
					.getRow()) || thisPanel == lastPanel)
				return false;
		}
		// Not on next to last panel?
		if (nextToLastMove != null) {
			Panel nextToLastPanel = board.getPanel(nextToLastMove);
			if (thisPanel == nextToLastPanel)
				return false;
		}
		return true;
	}

	/**
	 * Get a textual representation of the <code>GameMap</code>.
	 * <p>
	 * The map code is a String of 100 pairs of characters, the first of which
	 * indicates the <code>Panel</code>, and the second of which indicates the
	 * <code>Owner</code>.
	 * 
	 * @return
	 */
	public String getMapCode() {
		StringBuilder mapCode = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			Pos pos = Pos.getPos(i);
			Panel panel = board.getPanel(pos);
			char panelCode;
			if (panel == null)
				panelCode = 'a';
			else
				panelCode = panel.getName();
			mapCode.append(panelCode);

			int ownerIndex = marbles.getMarble(pos).getIdx();
			mapCode.append(ownerIndex);
		}
		return mapCode.toString();
	}

	/**
	 * Set the <code>Owner</code> of a field.
	 * <p>
	 * If the Owner changes, save the move in a history and decrease the number
	 * of available marbles.
	 * 
	 * @param row
	 * @param col
	 * @param owner
	 */
	public void setOwner(Pos pos, Owner owner) {
		boolean changed = marbles.setMarble(pos, owner);
		if (changed) {
			logger.fine("lastMove: " + lastMove);
			logger.fine("nextToLastMove: " + nextToLastMove);
			logger.info(String.format("Set owner of pos %s to %s.", pos, owner));
			nextToLastMove = lastMove;
			lastMove = pos;
			if (owner == Owner.Black)
				blackMarbles--;
			else
				redMarbles--;
		}
	}

	/**
	 * Given a board code, update the <code>Owners</code> of the fields.
	 * 
	 * @param boardCode
	 *            200-character board code
	 * @throws IllegalBoardCode
	 */
	public void updateGameMap(String boardCode) throws IllegalBoardCode {
		BoardParser.getMarbles(boardCode, this);
		logger.fine("lastMove: " + lastMove);
		logger.fine("nextToLastMove: " + nextToLastMove);
	}

	/**
	 * Get the current points for <code>owner</code>.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @param level
	 *            0, 1, or 2
	 * @return number of points
	 */
	public int getPoints(Owner owner, int level) {
		int points = panelPoints(owner);

		switch (level) {
		case 1:
			points += areaPoints(owner);
			break;
		case 2:
			points += areaPoints(owner) + chainPoints(owner);
		default:
			break;
		}
		return points;
	}

	public int getPoints(char playerColour, int level) {
		Owner owner = (playerColour == 'b') ? Owner.Black : Owner.Red;
		return getPoints(owner, level);
	}

	/**
	 * Calculate the number of points for owner based on panel possession.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return number of points
	 */
	private int panelPoints(Owner owner) {
		int points = 0;
		Map<Character, Panel> panels = board.getPanels();
		for (char name : panels.keySet()) {
			Panel panel = panels.get(name);
			try {
				if (panel.getOwner(marbles) == owner)
					points += panel.getSize();
			} catch (PanelNotPlacedException | PanelOutOfBoundsException e) {
				logger.warning("could not get points for panel " + name);
				e.printStackTrace();
			}
		}
		return points;
	}

	/**
	 * Calculate bonus points for level 1 and 2 based on largest interconnecting
	 * area.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return number of points
	 */
	private int areaPoints(Owner owner) {
		return marbles.getLargestArea(owner);

	}

	/**
	 * Calculate bonus points for level 2 based on longest chain.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return number of points
	 */
	private int chainPoints(Owner owner) {
		return marbles.getChainLength(owner);
	}

	/**
	 * Returns the number of remaining marbles for <code>owner</code>.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return number of marbles
	 */
	public int remainingMarbles(Owner owner) {
		if (owner == Owner.Black)
			return blackMarbles;
		else
			return redMarbles;
	}

	/**
	 * Returns a reference to the <code>Board</code> object
	 * 
	 * @return the <code>Board</code>
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Returns a reference to the <code>Marbles</code> object
	 * 
	 * @return the <code>Marbles</code>
	 */
	public Marbles getMarbles() {
		return marbles;
	}

	/**
	 * Returns the position of the last move.
	 * 
	 * @return position
	 */
	public Pos getLastMove() {
		return lastMove;
	}

	/**
	 * Returns a <code>List</code> of length 100 that contains the
	 * <code>Owner</code> of the <code>Panel</code>s on the corresponding
	 * fields.
	 * <p>
	 * This list can be used to visualize panel possession.
	 * 
	 * @return list of panel owners
	 */
	public List<Owner> getPanelOwners() {
		Panel[] fields = board.getFields();
		List<Owner> owners = new ArrayList<>(100);
		for (Panel field : fields)
			try {
				if (field == null)
					owners.add(Owner.None);
				else
					owners.add(field.getOwner(marbles));
			} catch (PanelNotPlacedException | PanelOutOfBoundsException e) {
				e.printStackTrace();
			}
		return owners;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append(getMapCode().substring(i * 20, i * 20 + 20));
			sb.append('\n');
		}
		return sb.toString();
	}

	/*
	 * Test case: create game map and retrieve textual representation. Set two
	 * new marbles. get legal fields
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IllegalBoardCode {
		// String mapCode1 = "a0a0a0k0f0f0a0a0a0a0"
		// + "a0a0p0k0f0f0o0o0a0a0" + "a0a0p0k0b1b0b0g2g0a0"
		// + "a0c0c0c0b0b0b0g0g0a0" + "a0c0c0c0l0d0d0d0a0a0"
		// + "h0h0i0i0l2d1d0d0m0a0" + "h0h0i0i0l2q2j0j1m0a0"
		// + "a0a0e0e0e1q0j0j0m2a0" + "a0a0e0e0e0r0r0a0a0a0"
		// + "a0a0a0n1n1n1a0a0a0a0";
		String mapCode2 = "a0a0a0f2f1o1o0a0a0a0" + "a0b0b1f2f1l1l1l2a0a0"
				+ "a0b0b1g1g1h2h2e1e2a0" + "k2b2b1g1g1h2h2e2e1a0"
				+ "k0c0c1c1m2m1m2e2e0a0" + "k2c1c1c2d2d2d1p1a0a0"
				+ "a0n1n2n2d2d2d1p0a0a0" + "a0q2q2i1i2j2j1r2r1a0"
				+ "a0a0a0i1i0j0j2a0a0a0" + "a0a0a0a0a0a0a0a0a0a0";
		GameMap gameMap = new GameMap(mapCode2);
		gameMap.updateGameMap(mapCode2);

		System.out.println(gameMap);
		System.out.printf("Rot: %d Punkte\n", gameMap.getPoints(Owner.Red, 3));
		System.out.printf("Schwarz: %d Punkte\n",
				gameMap.getPoints(Owner.Black, 3));
		// gameMap.setOwner(Pos.getPos(1, 3), Owner.Black);
		// gameMap.setOwner(Pos.getPos(5, 3), Owner.Red);
		// System.out.println(gameMap);
		// ArrayList<Pos> legalFields = gameMap.getLegalFields();
		// System.out.println("legal fields:");
		// for (Pos pos : legalFields)
		// System.out.println(pos);
	}
}
