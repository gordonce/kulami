/**
 * 
 */
package kulami.gui;

import kulami.game.board.Orientation;
import kulami.game.board.Pos;

/**
 * @author gordon
 *
 */
public interface MapEditorAdapter {

	public void newPanelSelected(int size, Orientation orientation);
	
	public void saveMap();
	
	public void closeMapEditor();
	
	public void placePanel(Pos pos);

	/**
	 * @param pos
	 */
	public void tileClicked(Pos pos);

	/**
	 * @param pos
	 */
	public void tileEntered(Pos pos);

	/**
	 * @param pos
	 */
	public void tileExited(Pos pos);
	
}
