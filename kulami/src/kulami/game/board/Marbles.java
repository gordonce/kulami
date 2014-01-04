/**
 * 
 */
package kulami.game.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Marbles represents the positions of all marbles on a board. Marbles can be
 * placed or removed.
 * 
 * @author gordon
 * 
 */
public class Marbles {

	private Owner[][] marbles;

	/**
	 * Construct a new Marbles object with 100 slots for marbles. All positions
	 * are initialized to None.
	 */
	public Marbles() {
		marbles = new Owner[10][10];
		setAllNone();
	}

	/**
	 * @param marbles
	 */
	public Marbles(Marbles marblesObj) {
		marbles = new Owner[10][10];
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				marbles[row][col] = marblesObj.marbles[row][col];
	}

	/**
	 * Get the owner of position pos.
	 * 
	 * @param pos
	 *            A position
	 * @return The Owner
	 */
	public Owner getMarble(Pos pos) {
		return marbles[pos.getRow()][pos.getCol()];
	}

	/**
	 * Set the Owner of position pos to owner.
	 * 
	 * @param pos
	 * @param owner
	 */
	public boolean setMarble(Pos pos, Owner owner) {
		if (marbles[pos.getRow()][pos.getCol()] != owner) {
			marbles[pos.getRow()][pos.getCol()] = owner;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set all positions to Owner None.
	 */
	public void setAllNone() {
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				marbles[row][col] = Owner.None;
	}

	/**
	 * Calculate the largest interconnecting area of a single colour.
	 * 
	 * @param owner
	 *            The Owner
	 * @return Number of marbles in area
	 */
	public int getLargestArea(Owner owner) {
		List<Pos> positions = getMarbles(owner);
		int[] id = getAreaIDs(positions);
		Map<Integer, Integer> areas = areaSizes(id);
		System.out.println("areas: " + areas);
		
		int maxsize = 0;
		for (int size: areas.values())
			if (size > maxsize)
				maxsize = size;
		System.out.println("areas.values:" + areas.values());
		return maxsize;
	}

	private List<Pos> getMarbles(Owner owner) {
		List<Pos> positions = new ArrayList<>();
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				if (marbles[row][col] == owner)
					positions.add(Pos.getPos(row, col));
		return positions;
	}

	private int[] getAreaIDs(List<Pos> positions) {
		int n = positions.size();
		int[] id = new int[n];
		for (int i = 0; i < n; i++)
			id[i] = i;
		for (int i = 0; i < n - 1; i++)
			for (int j = i + 1; j < n; j++) {
				Pos pos1 = positions.get(i);
				Pos pos2 = positions.get(j);
				if (neighbouring(pos1, pos2))
					id[j] = id[i];
			}
		return id;
	}

	private boolean neighbouring(Pos pos1, Pos pos2) {
		int row1 = pos1.getRow();
		int row2 = pos2.getRow();
		int col1 = pos1.getCol();
		int col2 = pos2.getCol();
		if ((Math.abs(row1 - row2) == 1) && col1 == col2)
			return true;
		else if ((Math.abs(col1 - col2) == 1) && row1 == row2)
			return true;
		else
			return false;
	}

	private Map<Integer, Integer> areaSizes(int[] id) {
		Map<Integer, Integer> areas = new HashMap<>();
		for (int i : id)
			if (areas.containsKey(i))
				areas.put(i, areas.get(i) + 1);
			else
				areas.put(i, 1);
		return areas;
	}


	
	/**
	 * Calculate the length of the longest longest chain with 5 or more marbles.
	 * 
	 * @param owner
	 *            The Owner
	 * @return Number of marbles in longest chain (>= 5) or zero
	 */
	public int getLongestChain(Owner owner) {
		// TODO Auto-generated method stub
		return 0;
	}

}
