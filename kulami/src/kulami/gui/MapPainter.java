/**
 * 
 */
package kulami.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JPanel;

import kulami.control.DisplayFlags;
import kulami.game.board.Board;
import kulami.game.board.Marbles;
import kulami.game.board.Orientation;
import kulami.game.board.Owner;
import kulami.game.board.Panel;
import kulami.game.board.Panel.PanelNotPlacedException;
import kulami.game.board.Panel.PanelOutOfBoundsException;
import kulami.game.board.Pos;

/**
 * The <code>MapPainter</code> is responsible for drawing a <code>GameMap</code>
 * into a <code>JPanel</code>.
 * 
 * @author gordon
 * 
 */
public class MapPainter {

	private JPanel boardPanel;

	private Image emptyTile;
	private Image leftEnd;
	private Image rightEnd;
	private Image upperEnd;
	private Image lowerEnd;
	private Image leftSide;
	private Image rightSide;
	private Image upperSide;
	private Image lowerSide;
	private Image upperLeft;
	private Image upperRight;
	private Image lowerLeft;
	private Image lowerRight;
	private Image middleH;
	private Image middleV;

	private List<TileComponent> tiles;

	private TileComponent lastMoveTile;
	private TileComponent nextToLastMoveTile;

	private static final Logger logger = Logger
			.getLogger("kulami.gui.MapPainter");

	/**
	 * Constructs a new <code>MapPainter</code>
	 * 
	 * @param boardPanel
	 *            the <code>JPanel</code> to paint the board in
	 */
	public MapPainter(JPanel boardPanel) {
		this.boardPanel = boardPanel;
		loadImages();
	}

	/**
	 * Draws the board and gives each tile a reference to
	 * <code>displayFlags</code>.
	 * 
	 * @param board
	 *            the <code>Board</code> to draw
	 * @param displayFlags
	 */
	public void drawBoard(Board board, DisplayFlags displayFlags) {
		boardPanel.removeAll();
		List<Image> tileImages = mapToImageList(board);
		tiles = new ArrayList<>(100);
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Image tileImg = tileImages.get(row * 10 + col);
				TileComponent tileComp = new TileComponent(tileImg, Pos.getPos(
						row, col), displayFlags);
				boardPanel.add(tileComp);
				tiles.add(tileComp);
			}
		boardPanel.revalidate();
		boardPanel.repaint();
	}

	/**
	 * Draws marbles on top of the board
	 * 
	 * @param marbles
	 *            a reference to a <code>Marbles</code> object
	 */
	public void drawMarbles(Marbles marbles) {
		for (int i = 0; i < 100; i++) {
			int colour = marbles.getMarble(Pos.getPos(i)).getIdx();
			tiles.get(i).setMarble(colour);
		}
		boardPanel.repaint();
	}

	/**
	 * Remember the last move for the last moves display option
	 * 
	 * @param pos
	 *            position of the last move
	 */
	public void setLastMove(Pos pos) {
		// Reset next to last tile
		if (nextToLastMoveTile != null) {
			nextToLastMoveTile.setNextToLastMove(false);
		}
		// last tile becomes next to last tile
		if (lastMoveTile != null) {
			nextToLastMoveTile = lastMoveTile;
			nextToLastMoveTile.setLastMove(false);
			nextToLastMoveTile.setNextToLastMove(true);
		}
		// set last tile
		lastMoveTile = tiles.get(pos.getIdx());
		lastMoveTile.setLastMove(true);
		logger.fine("lastMoveTile set to: " + lastMoveTile);
	}

	/**
	 * Sets possible moves for the possible moves display option
	 * 
	 * @param positions
	 *            positions of possible moves
	 */
	public void setPossibleMoves(List<Pos> positions) {
		clearPossibleMoves();
		for (Pos pos : positions)
			tiles.get(pos.getIdx()).setPossibleMove(true);
	}

	/**
	 * Clears all possible moves
	 */
	public void clearPossibleMoves() {
		for (TileComponent tile : tiles)
			tile.setPossibleMove(false);
	}

	/**
	 * Sets the panel owner for each tile for the panel possession display
	 * option
	 * 
	 * @param owners
	 *            the panel owners
	 */
	public void setPanelOwners(List<Owner> owners) {
		for (int i = 0; i < 100; i++) {
			Owner owner = owners.get(i);
			tiles.get(i).setPanelOwner(owner);
		}

	}

	/**
	 * Register a <code>MouseListener</code> for each <code>TileComponent</code>
	 * .
	 * 
	 * @param tileListener
	 */
	public void registerTileListeners(MouseListener tileListener) {
		for (TileComponent tile : tiles)
			tile.addMouseListener(tileListener);
	}

	/**
	 * When display option flags changed, repaint
	 */
	public void flagsChanged() {
		boardPanel.repaint();
	}

	/**
	 * Convert a <code>Board</code> to a list of 100 <code>Image</code>s that
	 * can be used to display the board.
	 * 
	 * @param board
	 *            the board
	 * @return list of images
	 */
	private List<Image> mapToImageList(Board board) {
		Image[] imageArray = new Image[100];
		Arrays.fill(imageArray, emptyTile);
		Map<Character, Panel> panelMap = board.getPanels();
		for (Panel panel : panelMap.values()) {
			int size = panel.getSize();
			Orientation orientation = panel.getOrientation();
			Pos[] positions;
			try {
				positions = panel.getPositions();
				int first = positions[0].getIdx();
				if (size == 6) {
					imageArray[first] = upperLeft;
					if (orientation == Orientation.Horizontal) {
						imageArray[first + 1] = upperSide;
						imageArray[first + 2] = upperRight;
						imageArray[first + 10] = lowerLeft;
						imageArray[first + 11] = lowerSide;
						imageArray[first + 12] = lowerRight;
					} else {
						imageArray[first + 1] = upperRight;
						imageArray[first + 10] = leftSide;
						imageArray[first + 11] = rightSide;
						imageArray[first + 20] = lowerLeft;
						imageArray[first + 21] = lowerRight;
					}

				} else if (size == 4) {
					imageArray[first] = upperLeft;
					imageArray[first + 1] = upperRight;
					imageArray[first + 10] = lowerLeft;
					imageArray[first + 11] = lowerRight;
				} else if (size == 3) {
					if (orientation == Orientation.Horizontal) {
						imageArray[first] = leftEnd;
						imageArray[first + 1] = middleH;
						imageArray[first + 2] = rightEnd;
					} else {
						imageArray[first] = upperEnd;
						imageArray[first + 10] = middleV;
						imageArray[first + 20] = lowerEnd;
					}

				} else {
					if (orientation == Orientation.Horizontal) {
						imageArray[first] = leftEnd;
						imageArray[first + 1] = rightEnd;
					} else {
						imageArray[first] = upperEnd;
						imageArray[first + 10] = lowerEnd;
					}
				}
			} catch (PanelNotPlacedException | PanelOutOfBoundsException e) {
				// ignore unplaced panels
			}
		}
		return Arrays.asList(imageArray);
	}

	/**
	 * Load the tile images from image files.
	 */
	private void loadImages() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		String path = "/images/";
		emptyTile = toolkit.getImage(getClass().getResource(
				path + "empty_tile.png"));
		leftEnd = toolkit.getImage(getClass().getResource(
				path + "left_end_tile.png"));
		rightEnd = toolkit.getImage(getClass().getResource(
				path + "right_end_tile.png"));
		upperEnd = toolkit.getImage(getClass().getResource(
				path + "upper_end_tile.png"));
		lowerEnd = toolkit.getImage(getClass().getResource(
				path + "lower_end_tile.png"));
		leftSide = toolkit.getImage(getClass().getResource(
				path + "left_side_tile.png"));
		rightSide = toolkit.getImage(getClass().getResource(
				path + "right_side_tile.png"));
		upperSide = toolkit.getImage(getClass().getResource(
				path + "upper_side_tile.png"));
		lowerSide = toolkit.getImage(getClass().getResource(
				path + "lower_side_tile.png"));
		upperLeft = toolkit.getImage(getClass().getResource(
				path + "upper_left_tile.png"));
		upperRight = toolkit.getImage(getClass().getResource(
				path + "upper_right_tile.png"));
		lowerLeft = toolkit.getImage(getClass().getResource(
				path + "lower_left_tile.png"));
		lowerRight = toolkit.getImage(getClass().getResource(
				path + "lower_right_tile.png"));
		middleV = toolkit.getImage(getClass()
				.getResource(path + "middle_v.png"));
		middleH = toolkit.getImage(getClass()
				.getResource(path + "middle_h.png"));
	}

}
