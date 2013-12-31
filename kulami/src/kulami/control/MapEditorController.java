/**
 * 
 */
package kulami.control;

import java.util.logging.Logger;

import kulami.game.board.Board;
import kulami.game.board.Board.FieldsNotEmptyException;
import kulami.game.board.Orientation;
import kulami.game.board.Panel.PanelNotPlacedException;
import kulami.game.board.Panel.PanelOutOfBoundsException;
import kulami.game.board.Pos;
import kulami.game.board.TooManyPanelsException;
import kulami.gui.MapEditor;
import kulami.gui.MapEditorAdapter;

/**
 * A MapEditor frame needs a MapEditorController object to handle user input
 * events. User input events are defined in MapEditorAdapter.
 * 
 * @author gordon
 * 
 */
public class MapEditorController implements MapEditorAdapter {
	private MapEditor mapEditor;
	private Board board;

	// Currently selected panel type
	private int size;
	private Orientation orientation;

	// Panel type selected but not inserted yet
	private boolean insertingPanel;

	private static final Logger logger = Logger
			.getLogger("kulami.control.MapEditorController");

	/**
	 * The constructor creates a new MapEditor and displays it.  
	 * 
	 * @param gameController
	 */
	public MapEditorController() {
		mapEditor = new MapEditor(this);
		insertingPanel = false;
		board = new Board();
		mapEditor.drawBoard(board);
		logger.finer("Displaying board: \n" + board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.MapEditorAdapter#newPanelSelected(int,
	 * kulami.gui.Orientation)
	 */
	@Override
	public void newPanelSelected(int size, Orientation orientation) {
		logger.fine(String.format("Inserting panel size %d (%s)", size,
				orientation));
		this.size = size;
		this.orientation = orientation;
		insertingPanel = true;
		// TODO start insertion animation
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.MapEditorAdapter#saveMap()
	 */
	@Override
	public void saveMap() {
		mapEditor.saveMap(board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.MapEditorAdapter#closeMapEditor()
	 */
	@Override
	public void closeMapEditor() {
		// TODO Auto-generated method stub
		mapEditor.clearAndHide();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.MapEditorAdapter#placePanel(kulami.game.Pos)
	 */
	@Override
	public void placePanel(Pos pos) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kulami.gui.MapEditorAdapter#tileClicked(kulami.game.Pos)
	 */
	@Override
	public void tileClicked(Pos pos) {
		logger.fine(String.format("Tile at %s clicked.", pos));
		if (!insertingPanel)
			return;
		try {
			board.putPanel(size, pos, orientation);
			mapEditor.drawBoard(board);
		} catch (PanelOutOfBoundsException | PanelNotPlacedException
				| FieldsNotEmptyException | TooManyPanelsException e) {
			// ignore
		}
	}

	/* (non-Javadoc)
	 * @see kulami.gui.MapEditorAdapter#tileEntered(kulami.game.Pos)
	 */
	@Override
	public void tileEntered(Pos pos) {
		// TODO Can the Panel be placed here?
		// Show preview
	}

	/* (non-Javadoc)
	 * @see kulami.gui.MapEditorAdapter#tileExited(kulami.game.Pos)
	 */
	@Override
	public void tileExited(Pos pos) {
		// TODO hide preview
	}
	



}
