/**
 * 
 */
package kulami.game;


/**
 * @author gordon
 * 
 */
public abstract class Player {

	private String name;
	private Owner colour;
	private int marbles;

	public Player() {
		marbles = 20;
	}

	/**
	 * @param colour
	 * @param marbles
	 */
	public Player(String name, Owner colour) {
		this.name = name;
		this.colour = colour;
		marbles = 20;
	}

	public String getName() {
		return name;
	}
	
	public Owner getCoulour() {
		return colour;
	}

	public int getMarbles() {
		return marbles;
	}

	@Override
	public String toString() {
		return String.format("%s (%d marbles)", name, marbles);
	}
	
	

}
