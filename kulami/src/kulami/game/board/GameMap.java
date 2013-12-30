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
 * GameMap represents a map of 17 panels together with its current
 * configuration. A GameMap is immutable so that it is easy to try different
 * moves without changing the original GameMap.
 * 
 * @author gordon
 * 
 */
public class GameMap {

	private List<Move> history;
	private Board board;
	private Marbles marbles;

	private static final Logger logger = Logger
			.getLogger("kulami.game.GameMap");

	/**
	 * Construct an empty GameMap
	 */
	public GameMap() {
		history = new ArrayList<>();
		board = new Board();
		marbles = new Marbles();
	}

	/**
	 * Construct a map given a 200-character representation of a map.
	 * 
	 * A GameMap object represents a 10x10-map in which each field is encoded by
	 * a pair of two characters.
	 * 
	 * The constructor initializes the Boards and creates a matrix of 10 by 10
	 * Fields .
	 * 
	 * TODO needs to throw an exception if mapCode is not correctly formatted
	 * 
	 * @param mapCode
	 * @throws IllegalBoardCode
	 */
	public GameMap(String boardCode) throws IllegalBoardCode {
		this();
		BoardParser.getBoard(boardCode, board);
		BoardParser.getMarbles(boardCode, marbles);
	}

	/**
	 * Set the owner of all fields to None and erase the history.
	 */
	public void clearOwners() {
		marbles.setAllNone();
		history.clear();
	}

	/**
	 * Get all the positions where it is legal to place a marble in the next
	 * move.
	 * 
	 * @return a list of positions.
	 */
	public ArrayList<Pos> getLegalFields() {
		int size = history.size();
		Pos lastMove = null;
		Pos nextToLastMove = null;
		ArrayList<Pos> legalMoves = new ArrayList<>();
		if (size > 0)
			lastMove = history.get(size - 1).getPos();
		if (size > 1)
			nextToLastMove = history.get(size - 2).getPos();
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Pos pos = Pos.getPos(row, col);
				if (isLegal(pos, lastMove, nextToLastMove))
					legalMoves.add(pos);
			}
		return legalMoves;

	}

	private boolean isLegal(Pos pos, Pos lastMove, Pos nextToLastMove) {
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
			Panel nextToLastPanel = board.getPanel(pos);
			if (thisPanel == nextToLastPanel)
				return false;
		}
		return true;
	}

	/**
	 * Get a textual representation of the GameMap. The map code is a String of
	 * 100 pairs of characters, the first of which indicates the Panel, and the
	 * second of which indicates the Owner.
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
	 * Set the Owner of a Field in a particular position of the 10x10 game map
	 * matrix. If the Owner changes, save the move in a history.
	 * 
	 * @param row
	 * @param col
	 * @param owner
	 */
	public void setOwner(Pos pos, Owner owner) {
		boolean changed =marbles.setMarble(pos, owner);
		if (changed)
			history.add(new Move(pos, owner));
	}

	/**
	 * Given a map code, update the Owners of the Fields.
	 * 
	 * // TODO must throw if the positions of the panels has changed.
	 * 
	 * @param mapCode
	 * @throws IllegalBoardCode 
	 */
	public void updateGameMap(String boardCode) throws IllegalBoardCode {
		BoardParser.getMarbles(boardCode, marbles);
	}

	/**
	 * Get the current points for Owner.
	 * 
	 * @param owner
	 * @return
	 */
	public int getPoints(Owner owner) {
		int points = 0;
		Map<Character, Panel> panels = board.getPanels();
		for (char name: panels.keySet()) {
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
		GameMap gameMap = new GameMap("a0a0a0k0f0f0a0a0a0a0"
				+ "a0a0o0k0f0f0p0p0a0a0" + "a0a0o0k0b1b0b0g2g0a0"
				+ "a0c0c0c0b0b0b0g0g0a0" + "a0c0c0c0l0d0d0d0a0a0"
				+ "h0h0i0i0l2d1d0d0m0a0" + "h0h0i0i0l2q2j0j1m0a0"
				+ "a0a0e0e0e1q0j0j0m2a0" + "a0a0e0e0e0r0r0a0a0a0"
				+ "a0a0a0n0n1n0a0a0a0a0");
		System.out.println(gameMap);
		System.out.printf("Rot: %d Punkte\n", gameMap.getPoints(Owner.Red));
		System.out.printf("Schwarz: %d Punkte\n",
				gameMap.getPoints(Owner.Black));
		gameMap.setOwner(Pos.getPos(1, 3), Owner.Black);
		gameMap.setOwner(Pos.getPos(5, 3), Owner.Red);
		System.out.println(gameMap);
		ArrayList<Pos> legalFields = gameMap.getLegalFields();
		for (Pos pos : legalFields)
			System.out.println(pos);
	}
}
