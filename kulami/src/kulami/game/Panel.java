/**
 * 
 */
package kulami.game;

import java.util.List;

/**
 * A Panel object represents a panel on a Kulami board. 
 * 
 * @author gordon
 * 
 */
class Panel {
	private int size;
	private char code;
	private List<Field> fields;

	/**
	 * @param size
	 * @param name
	 * @param fields
	 */
	public Panel(int size, char name, List<Field> fields) {
		this.size = size;
		this.code = name;
		this.fields = fields;
	}

	/**
	 * Calculate the owner of the board by checking who owns the majority of
	 * fields.
	 * 
	 * @return The Owner
	 */
	public Owner getOwner() {
		int black = 0, red = 0;
		for (Field field : fields) {
			Owner fieldOwner = field.getOwner();
			if (fieldOwner.equals(Owner.Black))
				black++;
			else if (fieldOwner.equals(Owner.Red))
				red++;
		}
		if (black > red)
			return Owner.Black;
		else if (red > black)
			return Owner.Red;
		else
			return Owner.None;
	}

	public char getName() {
		return code;
	}

	public int getSize() {
		return size;
	}
	
	public List<Field> getFields() {
		return fields;
	}

}
