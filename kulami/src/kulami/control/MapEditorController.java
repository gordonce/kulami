/**
 * 
 */
package kulami.control;

import java.util.logging.Logger;

import kulami.game.GameMap;
import kulami.gui.MapEditor;
import kulami.gui.MapEditorAdapter;
import kulami.gui.Orientation;

/**
 * @author gordon
 * 
 */
public class MapEditorController implements MapEditorAdapter {
	private GameController gameController;
	private MapEditor mapEditor;
	private GameMap gameMap;
	
	private static final Logger logger = Logger
			.getLogger("kulami.control.MapEditorController");

	public MapEditorController(GameController gameController) {
		this.gameController = gameController;
		mapEditor = new MapEditor(this);
		// TODO display empty map
	}

	@Override
	public void newPanelSelected(int size, Orientation orientation) {
		// TODO start insertion animation
		logger.fine(String.format("Inserting panel size %d (%s)", size,
				orientation));
	}

	@Override
	public void saveMap() {
		// TODO save map 

	}

	@Override
	public void closeMapEditor() {
		// TODO Auto-generated method stub

	}
}
