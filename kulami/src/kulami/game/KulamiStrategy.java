/**
 * 
 */
package kulami.game;

import kulami.game.GameMap.Pos;

/**
 * @author gordon
 *
 */
public interface KulamiStrategy {
	public Pos choosePos(Game game, Player player);
}
