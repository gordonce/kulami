/**
 * 
 */
package kulami.game;

import kulami.game.board.GameMap;
import kulami.game.board.Pos;


/**
 * @author gordon
 *
 */
public interface KulamiStrategy {
	public Pos choosePos(GameMap gameMap);
}
