/**
 * 
 */
package kulami.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gordon
 *
 */
public class Board {
	private int size;
	private char name;
	private Owner owner;
	private List<Field> fields;
	
	/**
	 * @param size
	 * @param owner
	 */
	public Board(int size, char name, Owner owner) {
		this.size = size;
		this.name = name;
		this.owner = owner;
		fields = new ArrayList<>();
	}
	
	public Owner getOwner() {
		return owner;
	}
	
	public char getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
	
	public void addField(Field field) {
		assert fields.size() < size;
		fields.add(field);
		updateOwner();
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
