/**
 * 
 */
package kulami.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kulami.game.Move;
import kulami.gui.Orientation;

/**
 * GameMap represents a map of 17 panels together with its current
 * configuration. A GameMap is immutable so that it is easy to try different
 * moves without changing the original GameMap.
 * 
 * @author gordon
 * 
 */
public class GameMap {

	private Map<Character, Panel> panels;
	private Field[][] fields = new Field[10][10];
	private Pattern fieldPattern = Pattern.compile("([a-r])([0-2])");
	private List<Move> history;
	private Map<Integer, Character> panelCodes;

	private static final Logger logger = Logger
			.getLogger("kulami.game.GameMap");

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
		initPanelCodes();
		// TODO throw exception if mapCode is not properly formatted
		parseMapCode(mapCode);
	}

	private void initPanelCodes() {
		panelCodes = new HashMap<>();
		panelCodes.put(6, 'b');
		panelCodes.put(4, 'f');
		panelCodes.put(3, 'k');
		panelCodes.put(2, 'o');
		
	}

	private GameMap(Map<Character, Panel> panels, Field[][] fields,
			List<Move> history) {
		this.panels = panels;
		this.fields = fields;
		this.history = history;
	}

	/**
	 * A factory method to construct an empty GameMap.
	 * 
	 * @return
	 */
	public static GameMap getEmpyMap() {
		String emptyCode = new String(new char[100]).replace("\0", "a0");
		return new GameMap(emptyCode);
	}

	/**
	 * Set the owner of all fields to None and erase the history.
	 */
	public GameMap clearOwners() {
		Field[][] newFields = new Field[10][10];
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Field field = fields[row][col];
				Panel panel = field.getPanel();
				Pos pos = field.getPos();
				newFields[row][col] = new Field(panel, Owner.None, pos);
			}
		return new GameMap(panels, newFields, new ArrayList<Move>());
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
				Pos pos = Pos.getPos(row, col);
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
			if ((pos.getCol() != lastMove.getCol() && pos.getRow() != lastMove
					.getRow()) || thisPanel == lastPanel)
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

	public Field getField(Pos pos) {
		return fields[pos.getRow()][pos.getCol()];
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
		for (Field[] row : fields)
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
	public GameMap setOwner(Pos pos, Owner owner) {
		Field[][] newFields = new Field[10][10];
		List<Move> newHistory = history;
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Field field = fields[row][col];
				Panel panel = field.getPanel();
				if (row == pos.getRow() && col == pos.getCol()) {
					newFields[row][col] = new Field(panel, owner, pos);
					if (owner != field.getOwner())
						newHistory = append(history, new Move(pos, owner));
				} else
					newFields[row][col] = field;
			}
		return new GameMap(panels, newFields, newHistory);
	}

	private <T> ArrayList<T> append(List<T> list, T el) {
		ArrayList<T> newList = new ArrayList<T>();
		newList.addAll(list);
		newList.add(el);
		return newList;
	}

	/**
	 * Given a map code, update the Owners of the Fields.
	 * 
	 * // TODO must throw if the positions of the panels has changed.
	 * 
	 * @param mapCode
	 */
	public GameMap updateGameMap(String mapCode) {
		// TODO remove code duplication with parseMapCode
		/*
		 * TODO command pattern? create parse-method that takes an object //
		 * like a higher order function that tells it what to do with // each
		 * field
		 */
		GameMap newGameMap = this;
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
					if (fields[row][col].getOwner() != owner) {
						newGameMap = setOwner(Pos.getPos(row, col), owner);
						logger.fine(String.format(
								"Updated field at %s. New owner: %s", Pos.getPos(
										row, col), owner));
					}
				} else {
					// TODO throw exception: mapCode does not contain 100 fields
				}
			}
		}

		return newGameMap;
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

	/**
	 * @param size
	 *            Panel of size 6, 4, 3, or 2
	 * @param orientation
	 *            Horizontal or vertical
	 * @param pos
	 *            Position of the upper left corner
	 */
	public GameMap insertPanel(int size, Orientation orientation, Pos pos) {
		logger.finer(String.format(
				"Trying to insert panel of size %d at position %s", size, pos));
		// is a slot for the size left?

		int width = 0, height = 0;
		char loCode = '\0', hiCode = '\0';

		switch (size) {
		case 6:
			loCode = 'b';
			hiCode = 'e';
			if (orientation == Orientation.Horizontal) {
				width = 3;
				height = 2;
			} else {
				width = 2;
				height = 3;
			}
			break;

		case 4:
			loCode = 'f';
			hiCode = 'j';
			width = 2;
			height = 2;
			break;

		case 3:
			loCode = 'k';
			hiCode = 'n';
			if (orientation == Orientation.Horizontal) {
				width = 3;
				height = 1;
			} else {
				width = 1;
				height = 3;
			}
			break;

		case 2:
			loCode = 'o';
			hiCode = 'r';
			if (orientation == Orientation.Horizontal) {
				width = 2;
				height = 1;
			} else {
				width = 1;
				height = 2;
			}
			break;

		}

		List<Pos> positions = getFieldsForPanel(loCode, hiCode, pos, width,
				height);
		if (fieldsEmpty(positions)) {
			logger.finer(String.format("Inserting panel at %s", pos));

			return putPanelOnFields(panels.get(code), positions);
		} else {
			// TODO throw
			return null;
		}

	}

	private List<Pos> getFieldsForPanel(char loCode, char hiCode, Pos pos,
			int width, int height) {

		int cornerRow = pos.getRow();
		int cornerCol = pos.getCol();
		List<Pos> fields = null;

		for (char ch = loCode; ch <= hiCode; ch++)
			if (!panelPlaced(ch)) {
				code = ch;
				// is space left for the panel
				if (cornerCol <= 10 - width && cornerRow <= height) {
					fields = new ArrayList<>();
					for (int row = cornerRow; row < cornerRow + height; row++)
						for (int col = cornerCol; col < cornerCol + width; col++)
							fields.add(Pos.getPos(row, col));
				} else {
					// TODO throw
					return null;
				}
			}
		return fields;

	}

	private boolean fieldsEmpty(List<Pos> positions) {
		for (Pos pos : positions)
			if (getField(pos).getPanel().getName() != 'a')
				return false;
		return true;
	}

	private boolean panelPlaced(char name) {
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				if (getField(Pos.getPos(row, col)).getPanel().getName() == name)
					return true;
		return false;

	}

	private GameMap putPanelOnFields(Panel panel, List<Pos> positions) {
		Field[][] newFields = new Field[10][10];
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Pos pos = Pos.getPos(row, col);
				if (positions.contains(pos)) {
					Field field = fields[row][col];
					newFields[row][col] = new Field(panel, field.getOwner(),
							field.getPos());
				}
			}
		return new GameMap(panels, newFields, history);
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
					Field field = new Field(panel, owner, Pos.getPos(row, col));
					fields[row][col] = field;
				} else {
					// TODO throw exception: mapCode does not contain 100 fields
				}
			}
		}

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
		gameMap.setOwner(Pos.getPos(1, 3), Owner.Black);
		gameMap.setOwner(Pos.getPos(5, 3), Owner.Red);
		System.out.println(gameMap);
		ArrayList<Pos> legalFields = gameMap.getLegalFields();
		for (Pos pos : legalFields)
			System.out.println(pos);
	}
}
