/**
 * 
 */
package kulami.gui;

import kulami.game.board.Orientation;
import kulami.game.board.Pos;

/**
 * Adapter interface for user input from the <code>MapEditor</code>
 * 
 * @author gordon
 * 
 */
public interface MapEditorAdapter {

	/**
	 * User selected new panel.
	 * 
	 * @param size
	 *            selected size
	 * @param orientation
	 *            selected <code>Orientation</code>
	 */
	public void newPanelSelected(int size, Orientation orientation);

	/**
	 * User clicked save
	 */
	public void saveMap();

	/**
	 * User clicked close
	 */
	public void closeMapEditor();

	/**
	 * User clicked tile at position <code>pos</code>
	 * 
	 * @param pos the position
	 */
	public void tileClicked(Pos pos); 
	
	/**
	 * User entered tile at position <code>pos</code>
	 * 
	 * @param pos the position
	 */
	public void tileEntered(Pos pos);

	/**
	 * User exited tile at position <code>pos</code>
	 * 
	 * @param pos the position
	 */
	public void tileExited(Pos pos);

}
