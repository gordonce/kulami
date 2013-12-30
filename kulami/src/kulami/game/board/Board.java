/**
 * 
 */
package kulami.game.board;

import java.util.HashMap;
import java.util.Map;

/**
 * A Board is a representation of the positions of up to 17 panels on a 10 by 10
 * map.
 * 
 * @author gordon
 * 
 */
public class Board {

	static final char LoCodeSix = 'b';
	static final char HiCodeSix = 'e';

	static final char LoCodeFour = 'f';
	static final char HiCodeFour = 'j';

	static final char LoCodeThree = 'k';
	static final char HiCodeThree = 'n';

	static final char LoCodeTwo = 'o';
	static final char HiCodeTwo = 'r';

	static final int NumSix = 4;
	static final int NumFour = 5;
	static final int NumThree = 4;
	static final int NumTwo = 4;

	private Map<Integer, Character> panelCodes = new HashMap<>(4);
	private Map<Character, Panel> panels = new HashMap<>(17);
	
	/**
	 * 
	 */
	public Board() {
		panelCodes.put(6, LoCodeSix);
		panelCodes.put(4, LoCodeFour);
		panelCodes.put(3, LoCodeThree);
		panelCodes.put(2, LoCodeTwo);
		
		for (char ch = LoCodeSix; ch <= HiCodeSix; ch++)
			panels.put(ch, new PanelSix(ch));
		
		for (char ch = LoCodeFour; ch <= HiCodeFour; ch++)
			panels.put(ch, new PanelFour(ch));
		
		for (char ch = LoCodeThree; ch <= HiCodeThree; ch++)
			panels.put(ch, new PanelThree(ch));
		
		for (char ch = LoCodeTwo; ch <= HiCodeTwo; ch++)
			panels.put(ch, new PanelTwo(ch));
	}
	
	public void putPanel(int size, Pos pos, Orientation orientation) {
		
	}
		
}
