/**
 * 
 */
package kulami.game;

import java.util.List;

import kulami.game.Field.Owner;

/**
 * @author gordon
 *
 */
public class Board {
	private int size;
	private Field.Owner owner;
	private List<Field> fields;
	
	/**
	 * @param size
	 * @param owner
	 */
	public Board(int size, Owner owner) {
		this.size = size;
		this.owner = owner;
		initializeFields();
	}
	
	public Owner getOwner() {
		return owner;
	}
	
	public int getSize() {
		return size;
	}

	private void initializeFields() {
		for (int i = 0; i < size; i++)
			fields.add(new Field(this, Owner.None));
	}
	
	/**
	 * Update the owner of the board by checking who owns the majority
	 * of fields.
	 */
	public void updateOwner() {
		int black = 0, red = 0;
		for (Field field: fields) {
			Owner fieldOwner = field.getOwner();
			if (fieldOwner.equals(Owner.Black))
				black++;
			else if (fieldOwner.equals(Owner.Red))
				red++;
		}
		if (black > red)
			owner = Owner.Black;
		else if (red > black)
			owner = Owner.Red;
		else
			owner = Owner.None;
	}
	
	
}
