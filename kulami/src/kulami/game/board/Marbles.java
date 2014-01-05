package kulami.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A <code>Marbles</code> represents the positions of all marbles on a
 * <code>Board</code>. Marbles can be placed or removed.
 * 
 * @author gordon
 * 
 */
public class Marbles {

	private Owner[][] marbles;

	/**
	 * Construct a new <code>Marbles</code> object with 100 slots for marbles.
	 * All positions are initialized to <code>Owner.None</code>.
	 */
	public Marbles() {
		marbles = new Owner[10][10];
		setAllNone();
	}

	/**
	 * Constructs a copy of an existing <code>Marbles</code> object.
	 * 
	 * @param marblesObj
	 *            an existing <code>Marbles</code> object
	 */
	public Marbles(Marbles marblesObj) {
		marbles = new Owner[10][10];
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				marbles[row][col] = marblesObj.marbles[row][col];
	}

	/**
	 * Get the owner of position <code>pos</code>.
	 * 
	 * @param pos
	 *            a position
	 * @return the <code>Owner</code>
	 */
	public Owner getMarble(Pos pos) {
		return marbles[pos.getRow()][pos.getCol()];
	}

	/**
	 * Set the owner of position <code>pos</code> to <code>owner</code>.
	 * 
	 * @param pos
	 *            a position
	 * @param owner
	 *            the <code>Owner</code>
	 * @return <code>true</code> if the owner changed, <code>false</code>
	 *         otherwise
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
	 * Set all positions to <code>Owner.None</code>.
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
	 *            the <code>Owner</code>
	 * @return number of marbles in area
	 */
	public int getLargestArea(Owner owner) {
		List<Pos> positions = marblesForOwner(owner);
		int[] id = getAreaIDs(positions);
		Map<Integer, Integer> areas = areaSizes(id);
		System.out.println("areas: " + areas);

		int maxsize = 0;
		for (int size : areas.values())
			if (size > maxsize)
				maxsize = size;
		System.out.println("areas.values:" + areas.values());
		return maxsize;
	}

	/**
	 * Returns a <code>List</code> of positions of marbles belonging to
	 * <code>owner</code>.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return list of positions
	 */
	private List<Pos> marblesForOwner(Owner owner) {
		List<Pos> positions = new ArrayList<>();
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				if (marbles[row][col] == owner)
					positions.add(Pos.getPos(row, col));
		return positions;
	}

	/**
	 * Produce an array of sizes of adjacent marbles of the same colour using
	 * the union-find algorithm.
	 * 
	 * @param positions
	 *            A list of positions of marbles of the same colour
	 * @return An array of sizes of areas of the same colour
	 */
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
					union(i, j, id);
			}
		return id;
	}

	/**
	 * Set all slots that have the same <code>id</code> as <code>p</code> to the
	 * id of <code>q</code>.
	 * 
	 * @param p
	 * @param q
	 * @param id
	 */
	private void union(int p, int q, int[] id) {
		int pID = id[p];
		int qID = id[q];

		if (pID != qID)
			for (int i = 0; i < id.length; i++)
				if (id[i] == pID)
					id[i] = qID;
	}

	/**
	 * Returns true if <code>pos1</code> and <code>pos2</code> are neighbouring
	 * positions.
	 * 
	 * @param pos1
	 *            first position
	 * @param pos2
	 *            second position
	 * @return <code>true</code> if the positions are adjacent,
	 *         <code>false</code> otherwise
	 */
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

	/**
	 * Returns a <code>Map</code> that contains the sizes of areas as values.
	 * 
	 * @param id
	 *            array of area ids created by the union-find algorithm
	 * @return a Map of area sizes as values
	 */
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
	 * Calculate the length of the chains with 5 or more marbles.
	 * 
	 * @param owner
	 *            the <code>Owner</code>
	 * @return number of marbles in long chains (>= 5) or zero
	 */
	public int getChainLength(Owner owner) {
		int bonus = 0;
		List<Pos> positions = new ArrayList<>();

		// rows
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}

		// columns
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 10; row++) {
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}

		// left to right diagonals
		// longest diagonal starts at (0,0) and has 10 fields
		// shortest diagonals start at (0,5) and (5,0) and have 5 fields
		for (int i = 0; i < 6; i++) {
			for (int row = 0; row < 10 - i; row++) {
				int col = row + i;
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}

		for (int i = 1; i < 6; i++) {
			for (int col = 0; col < 10 - i; col++) {
				int row = col + i;
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}

		// right to left diagonals
		for (int i = 0; i < 6; i++) {
			for (int row = 9; row > 0 + i; row--) {
				int col = i + (9 - row);
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}

		for (int i = 1; i < 6; i++) {
			for (int col = 0; col < 10 - i; col++) {
				int row = 10 - (i + col);
				positions.add(Pos.getPos(row, col));
			}
			bonus += chainBonus(positions, owner);
			positions.clear();
		}
		return bonus;
	}

	/**
	 * Returns the bonus points for a list of <code>positions</code> for level
	 * 2.
	 * 
	 * @param positions
	 *            list of positions
	 * @param owner
	 *            the <code>Owner</code>
	 * @return length of longest chain (>= 5) or zero
	 */
	private int chainBonus(List<Pos> positions, Owner owner) {
		int maxChain = 0;
		int chain = 0;
		for (Pos pos : positions) {
			if (getMarble(pos) == owner) {
				chain++;
				if (chain > maxChain)
					maxChain = chain;
			} else {
				if (chain > maxChain)
					maxChain = chain;
				chain = 0;
			}
		}
		return (maxChain >= 5) ? maxChain : 0;
	}
}
