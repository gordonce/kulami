package kulami.game.board;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kulami.game.board.Board.FieldsNotEmptyException;
import kulami.game.board.Panel.PanelOutOfBoundsException;

/**
 * BoardParser is a class that contains static methods to parse the textual
 * representation of a Kulami board used by the Kulami server.
 * 
 * @author gordon
 * 
 */
class BoardParser {

	private static final String fieldRegex;
	private static final String boardRegex;
	private static final Pattern boardPattern;

	static {
		fieldRegex = "([a-r])([0-2])";
		boardRegex = String.format("(%s){100}", fieldRegex);
		boardPattern = Pattern.compile(boardRegex);
	}

	/**
	 * <code>BoardParser</code> is not meant to be instantiated.
	 */
	private BoardParser() {
	}

	/**
	 * This method parses the <code>boardCode</code> and configures the
	 * <code>Board</code> given as its second parameter.
	 * 
	 * @param boardCode
	 *            a 200-character board code
	 * @param board
	 *            <code>Board</code> object to be configured
	 * @throws IllegalBoardCode
	 */
	static void getBoard(String boardCode, Board board) throws IllegalBoardCode {
		if (!isCorrect(boardCode))
			throw new IllegalBoardCode();
		char[] codes = boardCode.replaceAll("\\d", "").toCharArray();
		Set<Character> seenCodes = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			char code = codes[i];
			if (code != 'a' && seenCodes.add(code)) {
				int size = Board.getSize(code);
				Orientation orientation;
				switch (size) {
				case 6:
				case 3:
					orientation = (codes[i + 2] == code) ? Orientation.Horizontal
							: Orientation.Vertical;
					break;
				case 2:
					orientation = (codes[i + 1] == code) ? Orientation.Horizontal
							: Orientation.Vertical;
					break;
				default:
					orientation = Orientation.Horizontal;
					break;
				}
				try {
					board.putPanel(code, Pos.getPos(i), orientation);
				} catch (PanelOutOfBoundsException | FieldsNotEmptyException e) {
					throw new IllegalBoardCode();
				}
			}
		}
	}

	/**
	 * This method parses the <code>boardCode</code> and places the marbles
	 * accordingly on the given <code>GameMap</code>.
	 * 
	 * @param boardCode
	 *            200-character board code
	 * @param gameMap
	 *            <code>GameMap</code> object on which marbles will be placed.
	 * @throws IllegalBoardCode
	 */
	static void getMarbles(String boardCode, GameMap gameMap)
			throws IllegalBoardCode {
		if (!isCorrect(boardCode))
			throw new IllegalBoardCode();
		char[] codes = boardCode.replaceAll("\\D", "").toCharArray();
		Owner owner;
		for (int i = 0; i < 100; i++) {
			char code = codes[i];
			switch (code) {
			case '1':
				owner = Owner.Black;
				break;
			case '2':
				owner = Owner.Red;
				break;
			default:
				owner = Owner.None;
				break;
			}
			Pos pos = Pos.getPos(i);
			gameMap.setOwner(pos, owner);
		}
	}

	/**
	 * Checks whether the argument is likely to hold a legal board code.
	 * 
	 * @param boardCode
	 *            200-character board code
	 * @return true if <code>boardCode</code> is a 200-character board code
	 */
	private static boolean isCorrect(String boardCode) {
		Matcher matcher = boardPattern.matcher(boardCode);
		return matcher.matches();
	}
}
