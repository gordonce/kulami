/**
 * 
 */
package kulami.gui;

import kulami.game.Pos;

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
	
}
