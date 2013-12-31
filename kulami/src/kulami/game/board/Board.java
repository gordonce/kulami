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

	private static final Map<Integer, Character> LoCodes = new HashMap<>(4);
	private static final Map<Integer, Character> HiCodes = new HashMap<>(4);
	private static final List<Integer> Sizes = new ArrayList<>(4);

	static {
		Sizes.add(6);
		Sizes.add(4);
		Sizes.add(3);
		Sizes.add(2);

		LoCodes.put(6, 'b');
		LoCodes.put(4, 'f');
		LoCodes.put(3, 'k');
		LoCodes.put(2, 'o');

		HiCodes.put(6, 'e');
		HiCodes.put(4, 'j');
		HiCodes.put(3, 'n');
		HiCodes.put(2, 'r');
	}

	// private Map<Integer, Character> panelCodes;
	private Map<Character, Boolean> codeTaken;
	private Map<Character, Panel> panels;
	private Panel[] fields = new Panel[100];

	/**
	 * 
	 */
	public Board() {
		// panelCodes = new HashMap<>(4);
		codeTaken = new HashMap<>(17);
		panels = new HashMap<>(17);
		for (int size : Sizes) {
			for (char code = LoCodes.get(size); code <= HiCodes.get(size); code++) {
				codeTaken.put(code, false);
				panels.put(code, Panel.getPanel(size, code));
			}
		}

	}

	/**
	 * Place a panel on the board with its size, upper left corner, and
	 * orientation specified. The method returns the assigned code if
	 * successful.
	 * 
	 * @param size
	 * @param corner
	 * @param orientation
	 * @return
	 * @throws PanelOutOfBoundsException
	 * @throws PanelNotPlacedException
	 * @throws FieldsNotEmptyException
	 * @throws TooManyPanelsException
	 */
	public char putPanel(int size, Pos corner, Orientation orientation)
			throws PanelOutOfBoundsException, PanelNotPlacedException,
			FieldsNotEmptyException, TooManyPanelsException {
		assert Sizes.contains(size);
		char code = getAvailableCode(size);
		putPanel(code, corner, orientation);
		return code;
	}

	private char getAvailableCode(int size) throws TooManyPanelsException {
		for (char code = LoCodes.get(size); code <= HiCodes.get(size); code++) {
			if (!codeTaken.get(code))
				return code;
		}
		throw new TooManyPanelsException();
	}

	/**
	 * Place panel on board given a code, the upper left corner, and
	 * orientation.
	 * 
	 * @param code
	 * @param corner
	 * @param orientation
	 * @throws PanelOutOfBoundsException 
	 * @throws FieldsNotEmptyException 
	 */
	public void putPanel(char code, Pos corner, Orientation orientation) throws PanelOutOfBoundsException, FieldsNotEmptyException {
		assert panels.containsKey(code);
		Panel panel = panels.get(code);
		Pos[] positions = panel.getPositions(corner, orientation);
		
		for (Pos pos : positions)
			if (fields[pos.getIdx()] != null)
				throw new FieldsNotEmptyException();
		
		panel.placePanel(corner, orientation);
		
		for (Pos pos : positions)
			fields[pos.getIdx()] = panel;
		
		codeTaken.put(code, true);
	}

	public void removePanel(char code) throws PanelNotPlacedException,
			PanelOutOfBoundsException {
		assert code >= LoCodes.get(6) && code <= HiCodes.get(2);
		Panel panel = panels.get(code);
		Pos[] positions = panel.getPositions();
		for (Pos pos : positions)
			fields[pos.getIdx()] = null;
		panel.removePanel();
		codeTaken.put(code, false);
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
	public Map<Character, Panel> getPanels() {
		return panels;
	}

	public static int getSize(char code) {
		for (int size : Sizes) {
			if (code >= LoCodes.get(size) && code <= HiCodes.get(size))
				return size;
		}
		return 0;
	}

	public class FieldsNotEmptyException extends Exception {
	}
}
