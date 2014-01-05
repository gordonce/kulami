package kulami.game;


/**
 * Classes that want to be notified of game state changes have to implement this
 * interface.
 * 
 * @author gordon
 * 
 */
public interface GameObserver {
	/**
	 * A marble has been placed. Use {@link GameObservable#getMarbles()} to get
	 * the marbles.
	 * 
	 * @param game
	 */
	public void gameChanged(GameObservable game);

	/**
	 * The board has changed. Use {@link GameObservable#getBoard()} to get the
	 * board.
	 * 
	 * @param game
	 */
	public void boardChanged(GameObservable game);

	/**
	 * The display flags have changed. Use
	 * {@link GameObservable#getDisplayFlags()} to get the
	 * <code>DisplayFlags</code> object.
	 * 
	 * @param game
	 */
	public void flagsChanged(GameObservable game);
}
