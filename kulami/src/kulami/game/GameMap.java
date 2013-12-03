/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GameMap represents a map of 17 panels together with its current
 * configuration. The shape of a 10x10-map is immutable. Only the owners of
 * fields can be changed.
 * 
 * @author gordon
 * 
 */
public class GameMap {

	private Map<Character, Panel> panels;
	private Field[][] fieldMatrix = new Field[10][10];
	private Pattern fieldPattern;
	private List<Move> history;

	public static class Pos {
		private int row;
		private int col;

		public Pos(int row, int col) {
			assert row >= 0 && row < 10;
			assert col >= 0 && col < 10;
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "[" + row + "," + col + "]";
		}

	}

	class Move {
		Pos pos;
		Owner owner;

		Move(Pos pos, Owner owner) {
			this.pos = pos;
			this.owner = owner;
		}

		Pos getPos() {
			return pos;
		}

		Owner getOwner() {
			return owner;
		}

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
	 */
	public GameMap(String mapCode) {
		history = new ArrayList<>();
		fieldPattern = Pattern.compile("([a-r])([0-2])");
		initializeBoards();
		// TODO catch exception if mapCode is not properly formatted
		parseMapCode(mapCode);
	}

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
				Pos pos = new Pos(row, col);
				if (isLegal(pos, lastMove, nextToLastMove))
					legalMoves.add(pos);
			}
		return legalMoves;

	}

	private boolean isLegal(Pos pos, Pos lastMove, Pos nextToLastMove) {
		Field thisField = getField(pos);
		Panel thisPanel = thisField.getPanel();
		// Is there a panel on the field?
		if (thisPanel.getName() == 'a') {
			return false;
		}
		// Is the field empty?
		if (thisField.getOwner() != Owner.None)
			return false;
		// Same row or column but not on same panel as last move?
		if (lastMove != null) {
			Panel lastPanel = getField(lastMove).getPanel();
			if ((pos.getCol() != lastMove.getCol() && 
					pos.getRow() != lastMove.getRow()) || thisPanel == lastPanel)
				return false;
		}
		// Not on next to last panel?
		if (nextToLastMove != null) {
			Panel nextToLastPanel = getField(nextToLastMove).getPanel();
			if (thisPanel == nextToLastPanel)
				return false;
		}
		return true;
	}

	private Field getField(Pos pos) {
		return fieldMatrix[pos.getRow()][pos.getCol()];
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
		for (Field[] row : fieldMatrix)
			for (Field field : row) {
				mapCode.append(field.getPanel().getName());
				mapCode.append(field.getOwner().getIdx());
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
		if (fieldMatrix[pos.getRow()][pos.getCol()].setOwner(owner))
			history.add(new Move(pos, owner));
	}

	/**
	 * Given a map code, update the Owners of the Fields.
	 * 
	 * // TODO must throw if the positions of the panels has changed.
	 * 
	 * @param mapCode
	 */
	public void updateGameMap(String mapCode) {
		// TODO remove code duplication with parseMapCode
		/*
		 * TODO command pattern? create parse-method that takes an object //
		 * like a higher order function that tells it what to do with // each
		 * field
		 */
		Matcher mapMatcher = fieldPattern.matcher(mapCode);
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (mapMatcher.find()) {
					int ownerIndex = Integer.parseInt(mapMatcher.group(2));
					Owner owner;
					if (ownerIndex == 0)
						owner = Owner.None;
					else if (ownerIndex == 1)
						owner = Owner.Black;
					else
						owner = Owner.Red;
					setOwner(new Pos(row, col), owner);
				} else {
					// TODO throw exception: mapCode does not contain 100 fields
				}
			}
		}
	}

	/**
	 * Get the current points for Owner.
	 * 
	 * @param owner
	 * @return
	 */
	public int getPoints(Owner owner) {
		int points = 0;
		for (Panel panel : panels.values())
			if (panel.getOwner() == owner)
				points += panel.getSize();
		return points;
	}

	private void parseMapCode(String mapCode) {
		Matcher mapMatcher = fieldPattern.matcher(mapCode);
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (mapMatcher.find()) {
					char boardIndex = mapMatcher.group(1).charAt(0);
					int ownerIndex = Integer.parseInt(mapMatcher.group(2));
					Owner owner;
					if (ownerIndex == 0)
						owner = Owner.None;
					else if (ownerIndex == 1)
						owner = Owner.Black;
					else
						owner = Owner.Red;
					Panel panel = panels.get(boardIndex);
					Field field = new Field(panel, owner);
					panel.addField(field);
					fieldMatrix[row][col] = field;
				} else {
					// TODO throw exception: mapCode does not contain 100 fields
				}
			}
		}

	}

	/*
	 * The internal representation of panels is a list of 18 Boards (including
	 * one for fields without panels).
	 */
	private void initializeBoards() {
		panels = new HashMap<>();
		panels.put('a', new Panel(36, 'a', Owner.None)); // for fields without
															// panels
		panels.put('b', new Panel(6, 'b', Owner.None));
		panels.put('c', new Panel(6, 'c', Owner.None));
		panels.put('d', new Panel(6, 'd', Owner.None));
		panels.put('e', new Panel(6, 'e', Owner.None));
		panels.put('f', new Panel(4, 'f', Owner.None));
		panels.put('g', new Panel(4, 'g', Owner.None));
		panels.put('h', new Panel(4, 'h', Owner.None));
		panels.put('i', new Panel(4, 'i', Owner.None));
		panels.put('j', new Panel(4, 'j', Owner.None));
		panels.put('k', new Panel(3, 'k', Owner.None));
		panels.put('l', new Panel(3, 'l', Owner.None));
		panels.put('m', new Panel(3, 'm', Owner.None));
		panels.put('n', new Panel(3, 'n', Owner.None));
		panels.put('o', new Panel(2, 'o', Owner.None));
		panels.put('p', new Panel(2, 'p', Owner.None));
		panels.put('q', new Panel(2, 'q', Owner.None));
		panels.put('r', new Panel(2, 'r', Owner.None));

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
	 * Test case: create game map and retrieve textual representation.
	 * Set two new marbles.
	 * get legal fields
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
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
		gameMap.setOwner(new Pos(1, 3), Owner.Black);
		gameMap.setOwner(new Pos(5, 3), Owner.Red);
		System.out.println(gameMap);
		ArrayList<Pos> legalFields = gameMap.getLegalFields();
		for (Pos pos : legalFields)
			System.out.println(pos);
	}
}
