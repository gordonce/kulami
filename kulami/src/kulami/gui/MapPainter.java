/**
 * 
 */
package kulami.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kulami.game.GameMap;

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
	
	private Image redMarble;
	private Image blackMarble;
	
	private List<MarbleComponent> marbleComponents;

	/**
	 * @param board
	 */
	public MapPainter(JPanel board) {
		this.board = board;
		loadImages();

//		board.repaint();
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
		middleV = toolkit.getImage(getClass().getResource(
				path + "middle_v.png"));
		middleH = toolkit.getImage(getClass().getResource(
				path + "middle_h.png"));
		redMarble = toolkit.getImage(getClass().getResource(
				path + "red_marble.png"));
		blackMarble = toolkit.getImage(getClass().getResource(
				path + "black_marble.png"));
	}

	/**
	 * @param gameMap
	 */
	public void drawMap(GameMap gameMap) {
		List<Image> tileImages = mapToImageList(gameMap);
		marbleComponents = new ArrayList<>(100);
		for (Image tile: tileImages) {
			board.add(new TileComponent(tile));
			MarbleComponent marbleComponent = new MarbleComponent(emptyTile);
			marbleComponents.add(marbleComponent);
		}
		board.repaint();
	}

	public void drawMarbles(GameMap gameMap) {
//		List<Image> marbles = mapToTileList(gameMap);
		marbleComponents.get(3).setMarble(blackMarble);
	}
	
//	private List<Image> mapToTileList(GameMap gameMap) {
//		Image[] imageArray = 
//	}
	
	private List<Image> mapToImageList(GameMap gameMap) {
		Image[] imageArray = new Image[100];
		Arrays.fill(imageArray, emptyTile);
		String mapCode = gameMap.getMapCode();
		// 6er
		for (char ch = 'b'; ch <= 'e'; ch++) {
			int first = mapCode.indexOf(ch) / 2;
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
		// 4er
		for (char ch = 'f'; ch <= 'j'; ch++) {
			int first = mapCode.indexOf(ch) / 2;
			imageArray[first] = upperLeft;
			imageArray[first + 1] = upperRight;
			imageArray[first + 10] = lowerLeft;
			imageArray[first + 11] = lowerRight;
		}
		// 3er
		for (char ch = 'k'; ch <= 'n'; ch++) {
			int first = mapCode.indexOf(ch) / 2;
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
		// 2er
		for (char ch = 'o'; ch <= 'r'; ch++) {
			int first = mapCode.indexOf(ch) / 2;
			boolean horizontal = (mapCode.charAt(first * 2 + 2) == ch);
			if (horizontal) {
				imageArray[first] = leftEnd;
				imageArray[first + 1] = rightEnd;
			} else {
				imageArray[first] = upperEnd;
				imageArray[first + 10] = lowerEnd;
			}
		}
		return Arrays.asList(imageArray);
	}

	private class TileComponent extends JComponent {
		Image tileImage;
		
		TileComponent(Image tileImage) {
			this.tileImage = tileImage;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			g2.drawImage(tileImage, 0, 0, this);
		}
	}
	
	private class MarbleComponent extends JComponent {
		Image marbleImage;
		
		MarbleComponent(Image marbleImage) {
			this.marbleImage = marbleImage;
		}
		
		void setMarble(Image marbleImage) {
			this.marbleImage = marbleImage;
			repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.drawImage(marbleImage, 0, 0, this);
		}
	}
	
	public static void main(String[] args) {
		GameMap gameMap = new GameMap("a0a0a0k0f0f0a0a0a0a0"
				+ "a0a0o0k0f0f0p0p0a0a0" + "a0a0o0k0b1b0b0g2g0a0"
				+ "a0c0c0c0b0b0b0g0g0a0" + "a0c0c0c0l0d0d0d0a0a0"
				+ "h0h0i0i0l2d1d0d0m0a0" + "h0h0i0i0l2q2j0j1m0a0"
				+ "a0a0e0e0e1q0j0j0m2a0" + "a0a0e0e0e0r0r0a0a0a0"
				+ "a0a0a0n0n1n0a0a0a0a0");
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(600, 600));
		panel.setLayout(new GridLayout(10, 10));
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MapPainter mapPainter = new MapPainter(panel);
		mapPainter.drawMap(gameMap);
		mapPainter.drawMarbles(gameMap);
		frame.setVisible(true);
	}
	

}
