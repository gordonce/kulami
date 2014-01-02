/**
 * 
 */
package kulami.game;

import java.util.List;

import kulami.control.DisplayFlags;
import kulami.game.board.Board;
import kulami.game.board.Marbles;
import kulami.game.board.Pos;
import kulami.gui.GameObserver;

/**
 * @author gordon
 *
 */
public interface GameObservable {

	public void registerObserver(GameObserver observer);
	public void removeObserver(GameObserver observer);
	public Board getBoard();
	public Marbles getMarbles();
	public Pos getLastMove();
	public List<Pos> getLegalMoves();
	
	public DisplayFlags getDisplayFlags();
	
}
