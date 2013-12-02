/**
 * 
 */
package kulami.game;

/**
 * A Field is a field on the Kulami game map. A Field belongs to a Panel and has
 * an Owner.
 * 
 * @author gordon
 * 
 */
public class Field {
	private Panel panel;
	private Owner owner;

	/**
	 * Create a new Field that belongs to a Panel and has an Owner. A Field
	 * always has a Panel and an Owner. The Panel is immutable, but the Owner
	 * can be changed.
	 * 
	 * @param panel
	 * @param owner
	 */
	public Field(Panel panel, Owner owner) {
		this.panel = panel;
		this.owner = owner;
	}

	/**
	 * Return the current Owner of the Field.
	 * 
	 * @return
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Return the Panel that the Field belongs to.
	 * 
	 * @return
	 */
	public Panel getBoard() {
		return panel;
	}

	/**
	 * Set the Owner of the Field. The Owner of the Panel that the Field belongs
	 * to is automatically updated.
	 * 
	 * @param owner
	 */
	public void setOwner(Owner owner) {
		if (this.owner != owner) {
			this.owner = owner;
			panel.updateOwner();
		}
	}
}
