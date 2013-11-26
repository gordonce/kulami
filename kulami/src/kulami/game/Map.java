/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Map represents a map of 17 boards together with its current configuration.
 * The shape of a 10x10-map is immutable. Only the owners of fields can be
 * changed.
 * 
 * @author gordon
 * 
 */
public class Map {

	private List<Board> boards;
	private Field[][] fieldMatrix = new Field[10][10];

	/**
	 * Construct a map given a 200-character representation of a map.
	 * 
	 * A Map object represents a 10x10-map in which each field is encoded by a
	 * pair of two characters.
	 * 
	 * 
	 * @param mapCode
	 */
	public Map(String mapCode) {

		// TODO validate mapCode
		// TODO save suitable representation of map
		initializeBoards();
		
	}
	
	public void setOwner(int xPos, int yPos, Field.Owner owner) {
		
	}

	/**
	 * The internal representation of boards is a list of 17 Boards. boards[0-3]
	 * 6 fields boards[4-8] 4 fields boards[9-12] 3 fields boards[13-16] 2
	 * fields
	 */
	private void initializeBoards() {
		boards = new ArrayList<>(17);
		for (int i = 0; i < 4; i++)
			// b-e
			boards.add(new Board(6, Field.Owner.None));
		for (int i = 0; i < 5; i++)
			// f-j
			boards.add(new Board(4, Field.Owner.None));
		for (int i = 0; i < 4; i++)
			// k-n
			boards.add(new Board(3, Field.Owner.None));
		for (int i = 0; i < 4; i++)
			// o-r
			boards.add(new Board(2, Field.Owner.None));
	}

}
