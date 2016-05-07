package kulami.game;

import java.util.List;

import kulami.control.DisplayFlags;
import kulami.game.board.Board;
import kulami.game.board.Marbles;
import kulami.game.board.Owner;
import kulami.game.board.Pos;

/**
 * The <code>GameObservable</code> interface defines the methods that the
 * <code>Game</code> class must implement for <code>GameObserver</code>s.
 * 
 * @author gordon
 * 
 */
public interface GameObservable {

	/**
	 * Register a new <code>GameObserver</code>.
	 * 
	 * @param observer
	 */
	public void registerObserver(GameObserver observer);

	/**
	 * Remove a <code>GameObserver</code>.
	 * 
	 * @param observer
	 */
	public void removeObserver(GameObserver observer);

	/**
	 * Returns a reference to the <code>Board</code>.
	 * 
	 * @return the board
	 */
	public Board getBoard();

	/**
	 * Returns a reference to the <code>Marbles</code> object.
	 * 
	 * @return the marbles
	 */
	public Marbles getMarbles();

	/**
	 * Returns the position of the last move.
	 * 
	 * @return the position
	 */
	public Pos getLastMove();

	/**
	 * Returns a list of all currently legal moves.
	 * 
	 * @return the list of positions
	 */
	public List<Pos> getLegalMoves();

	/**
	 * Returns a <code>List</code> of length 100 containing the
	 * <code>Owner</code>s of the <code>Panel</code>s that each corresponding
	 * field currently belongs to.
	 * <p>
	 * Used to display panel possession.
	 * 
	 * @return list of Owners
	 */
	public List<Owner> getPanelOwners();

	/**
	 * Returns a reference to the <code>DisplayFlags</code> object.
	 * 
	 * @return reference to DisplayFlags object
	 */
	public DisplayFlags getDisplayFlags();

}
