/**
 * 
 */
package kulami.game;

/**
 * @author gordon
 *
 */
public class Field {
	private Board board;
	private Owner owner;
	
	enum Owner {None, Black, Red};
	
	public Field(Board board, Owner owner) {
		this.board = board;
		this.owner = owner;
	}
	
	public Owner getOwner() {
		return owner;
	}
	
	public void setOwner(Owner owner) {
		this.owner = owner;
		board.updateOwner();
	}
}
