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
class Field {
	private Panel panel;
	private Owner owner;
	private Pos pos;

	/**
	 * Create a new Field that belongs to a Panel and has an Owner. A Field
	 * always has a Panel and an Owner.
	 * 
	 * @param panel
	 * @param owner
	 */
	public Field(Panel panel, Owner owner, Pos pos) {
		this.panel = panel;
		this.owner = owner;
		this.pos = pos;
	}

	/**
	 * Return the current Owner of the Field.
	 * 
	 * @return The Owner
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Return the Panel that the Field belongs to.
	 * 
	 * @return The Panel
	 */
	public Panel getPanel() {
		return panel;
	}
	
	/**
	 * Return the position of the Field on the board.
	 * 
	 * @return The position
	 */
	public Pos getPos() {
		return pos;
	}

}
