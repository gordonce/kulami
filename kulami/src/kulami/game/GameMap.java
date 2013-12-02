/**
 * 
 */
package kulami.game;

import java.util.HashMap;
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
	private Matcher mapMatcher;

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

		initializeBoards();
		// TODO catch exception if mapCode is not properly formatted
		parseMapCode(mapCode);
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
	 * matrix.
	 * 
	 * @param row
	 * @param col
	 * @param owner
	 */
	public void setOwner(int row, int col, Owner owner) {
		fieldMatrix[row][col].setOwner(owner);
	}
	
	/**
	 * Given a map code, update the Owners of the Fields.
	 * 
	 * // TODO must throw if the positions of the panels has changed. 
	 *
	 * @param mapCode
	 */
//	public void updateGameMap(String mapCode) {
//		int row = 0;
//		int col = 0;
//		for int i < 
//	}

	private void parseMapCode(String mapCode) {
		fieldPattern = Pattern.compile("([a-r])([0-2])");
		mapMatcher = fieldPattern.matcher(mapCode);
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
	}
}
