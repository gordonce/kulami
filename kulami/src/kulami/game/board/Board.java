/**
 * 
 */
package kulami.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kulami.game.board.Panel.PanelNotPlacedException;
import kulami.game.board.Panel.PanelOutOfBoundsException;

/**
 * A Board is a representation of the positions of up to 17 panels on a 10 by 10
 * map.
 * 
 * @author gordon
 * 
 */
public class Board {

	private static final Map<Integer, Character> loCodes = new HashMap<>(4);
	private static final Map<Integer, Character> hiCodes = new HashMap<>(4);
	private static final List<Integer> Sizes = new ArrayList<>(4);

	static {
		Sizes.add(6);
		Sizes.add(4);
		Sizes.add(3);
		Sizes.add(2);

		loCodes.put(6, 'b');
		loCodes.put(4, 'f');
		loCodes.put(3, 'k');
		loCodes.put(2, 'o');

		hiCodes.put(6, 'e');
		hiCodes.put(4, 'j');
		hiCodes.put(3, 'n');
		hiCodes.put(2, 'r');
	}

	private Map<Integer, Character> panelCodes = new HashMap<>(4);
	private Map<Character, Panel> panels = new HashMap<>(17);
	private Panel[] fields = new Panel[100];

	/**
	 * 
	 */
	public Board() {
		for (int size : Sizes) {
			panelCodes.put(size, loCodes.get(size));

			for (char code = loCodes.get(size); code <= hiCodes.get(size); code++)
				panels.put(code, Panel.getPanel(size, code));
		}

	}

	public char putPanel(int size, Pos corner, Orientation orientation)
			throws PanelOutOfBoundsException, PanelNotPlacedException,
			FieldsNotEmptyException {
		assert Sizes.contains(size);
		char code = panelCodes.get(size);
		if (code <= hiCodes.get(size)) {
			Panel panel = panels.get(code);
			Pos[] positions = panel.getPositions(corner, orientation);
			for (Pos pos : positions)
				if (fields[pos.getIdx()] != null)
					throw new FieldsNotEmptyException();
			panel.placePanel(corner, orientation);
			panelCodes.put(size, ++code);
			for (Pos pos : positions)
				fields[pos.getIdx()] = panel;
		}
		return code;
	}

	public void removePanel(char code) throws PanelNotPlacedException,
			PanelOutOfBoundsException {
		assert code >= loCodes.get(6) && code <= hiCodes.get(2);
		Panel panel = panels.get(code);
		Pos[] positions = panel.getPositions();
		for (Pos pos : positions)
			fields[pos.getIdx()] = null;
		panel.removePanel();
	}

	/**
	 * Get the panel at a particular position.
	 * 
	 * @param pos
	 *            a position
	 * @return null if there is no panel at pos
	 */
	public Panel getPanel(Pos pos) {
		return fields[pos.getIdx()];
	}

	/**
	 * Get an iterator over all panels on the board.
	 * 
	 * @return an iterator
	 */
	public 	Map<Character, Panel> getPanels() {
		return panels;
	}

	public static int getSize(char code) {
		for (int size : Sizes) {
			if (code >= loCodes.get(size) && code <= hiCodes.get(size))
				return size;
		}
		return 0;
	}

	public class FieldsNotEmptyException extends Exception {
	}
}
