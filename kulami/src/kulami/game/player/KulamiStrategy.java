/**
 * 
 */
package kulami.game.player;

import kulami.game.Game;
import kulami.game.board.Pos;


/**
 * @author gordon
 *
 */
public interface KulamiStrategy {
	public Pos choosePos(Game game);
}
