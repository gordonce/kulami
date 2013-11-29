/**
 * 
 */
package kulami.game;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GameMap represents a map of 17 boards together with its current
 * configuration. The shape of a 10x10-map is immutable. Only the owners of
 * fields can be changed.
 * 
 * @author gordon
 * 
 */
public class GameMap {

	private Map<Character, Board> boards;
	private Field[][] fieldMatrix = new Field[10][10];

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
	 * 100 pairs of characters, the first of which indicates the Board, and the
	 * second of which indicates the Owner.
	 * 
	 * @return
	 */
	public String getMapCode() {
		StringBuilder mapCode = new StringBuilder();
		for (Field[] row : fieldMatrix)
			for (Field field : row) {
				mapCode.append(field.getBoard().getName());
				mapCode.append(field.getOwner().getIdx());
			}
		return mapCode.toString();
	}

	private void parseMapCode(String mapCode) {
		Pattern fieldPattern = Pattern.compile("([a-r])([0-2])");
		Matcher mapMatcher = fieldPattern.matcher(mapCode);
		int row = 0;
		int col = 0;
		for (int i = 0; i < 100; i++) {
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
				Board board = boards.get(boardIndex);
				Field field = new Field(board, owner);
				board.addField(field);
				fieldMatrix[row][col] = field;
				col = (col + 1) % 10;
				if (col == 0)
					row++;
			} else {
				// TODO throw exception: mapCode does not contain 100 fields
			}
		}

	}

	/*
	 * The internal representation of boards is a list of 18 Boards (including
	 * one for fields without boards).
	 */
	private void initializeBoards() {
		boards = new HashMap<>();
		boards.put('a', new Board(36, 'a', Owner.None)); // for fields without
															// boards
		boards.put('b', new Board(6, 'b', Owner.None));
		boards.put('c', new Board(6, 'c', Owner.None));
		boards.put('d', new Board(6, 'd', Owner.None));
		boards.put('e', new Board(6, 'e', Owner.None));
		boards.put('f', new Board(4, 'f', Owner.None));
		boards.put('g', new Board(4, 'g', Owner.None));
		boards.put('h', new Board(4, 'h', Owner.None));
		boards.put('i', new Board(4, 'i', Owner.None));
		boards.put('j', new Board(4, 'j', Owner.None));
		boards.put('k', new Board(3, 'k', Owner.None));
		boards.put('l', new Board(3, 'l', Owner.None));
		boards.put('m', new Board(3, 'm', Owner.None));
		boards.put('n', new Board(3, 'n', Owner.None));
		boards.put('o', new Board(2, 'o', Owner.None));
		boards.put('p', new Board(2, 'p', Owner.None));
		boards.put('q', new Board(2, 'q', Owner.None));
		boards.put('r', new Board(2, 'r', Owner.None));

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
