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
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import kulami.game.board.GameMap;
import kulami.game.board.Pos;

/**
 * @author gordon
 * 
 */
public class MapPainter {

	private JPanel board;

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

	private static final Logger logger = Logger
			.getLogger("kulami.gui.MapPainter");

	/**
	 * @param board
	 */
	public MapPainter(JPanel board) {
		this.board = board;
		loadImages();

		// board.repaint();
	}

	/**
	 * @param gameMap
	 */
	public void drawMap(GameMap gameMap) {
		board.removeAll();
		List<Image> tileImages = mapToImageList(gameMap);
		tiles = new ArrayList<>(100);
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++) {
				Image tileImg = tileImages.get(row * 10 + col);
				TileComponent tileComp = new TileComponent(tileImg, Pos.getPos(
						row, col));
				board.add(tileComp);
				tiles.add(tileComp);
			}
		// board.repaint();
		board.revalidate();
	}

	/**
	 * @param gameMap
	 */
	public void drawMarbles(GameMap gameMap) {
		String mapCode = gameMap.getMapCode();
		for (int i = 0; i < 100; i++)
			tiles.get(i).setMarble(
					Integer.parseInt(mapCode
							.substring((i * 2 + 1), (i * 2 + 2))));
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				board.repaint();
			}
		});
	}

	public void registerTileListeners(MouseListener tileListener) {
		for (TileComponent tile : tiles)
			tile.addMouseListener(tileListener);
	}

	private List<Image> mapToImageList(GameMap gameMap) {
		Image[] imageArray = new Image[100];
		Arrays.fill(imageArray, emptyTile);
		String mapCode = gameMap.getMapCode();
		// 6er
		for (char ch = 'b'; ch <= 'e'; ch++) {
			int first = mapCode.indexOf(ch);
			if (first >= 0) {
				first = first / 2;
				logger.finest(String.format("6er: %s at %d", ch, first));
				boolean horizontal = (mapCode.charAt(first * 2 + 4) == ch);
				imageArray[first] = upperLeft;
				if (horizontal) {
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
			}
		}
		// 4er
		for (char ch = 'f'; ch <= 'j'; ch++) {
			int first = mapCode.indexOf(ch);
			if (first >= 0) {
				first = first / 2;
				logger.finest(String.format("4er: %s at %d", ch, first));
				imageArray[first] = upperLeft;
				imageArray[first + 1] = upperRight;
				imageArray[first + 10] = lowerLeft;
				imageArray[first + 11] = lowerRight;
			}
		}
		// 3er
		for (char ch = 'k'; ch <= 'n'; ch++) {
			int first = mapCode.indexOf(ch);
			if (first >= 0) {
				first = first / 2;
				logger.finest(String.format("3er: %s at %d", ch, first));
				boolean horizontal = (mapCode.charAt(first * 2 + 2) == ch);
				if (horizontal) {
					imageArray[first] = leftEnd;
					imageArray[first + 1] = middleH;
					imageArray[first + 2] = rightEnd;
				} else {
					imageArray[first] = upperEnd;
					imageArray[first + 10] = middleV;
					imageArray[first + 20] = lowerEnd;
				}
			}
		}
		// 2er
		for (char ch = 'o'; ch <= 'r'; ch++) {
			int first = mapCode.indexOf(ch);
			if (first >= 0) {
				first = first / 2;
				logger.finest(String.format("2er: %s at %d", ch, first));
				boolean horizontal = (mapCode.charAt(first * 2 + 2) == ch);
				if (horizontal) {
					imageArray[first] = leftEnd;
					imageArray[first + 1] = rightEnd;
				} else {
					imageArray[first] = upperEnd;
					imageArray[first + 10] = lowerEnd;
				}
			}
		}
		return Arrays.asList(imageArray);
	}

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
