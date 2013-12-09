/**
 * 
 */
package kulami.game;



/**
 * @author gordon
 *
 */
public class CompPlayer extends Player {

	private KulamiStrategy strategy;
	
	public CompPlayer() {
		super();
	}
	
	/**
	 * @param colour
	 * @param game
	 */
	public CompPlayer(Owner colour, Game game) {
		super(colour, game);
		strategy = new RandomStrategy();
	}
	
	public void makeMove() {
		placeMarble(strategy.choosePos(game, this));
	}

}
