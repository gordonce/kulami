/**
 * 
 */
package kulami.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	List<TileComponent> tiles;

	/**
	 * @param board
	 */
	public MapPainter(JPanel board) {
		this.board = board;
		loadImages();

		// board.repaint();
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

	/**
	 * @param gameMap
	 */
	public void drawMap(GameMap gameMap) {
		List<Image> tileImages = mapToImageList(gameMap);
		tiles = new ArrayList<>(100);
		for (Image tile : tileImages) {
			TileComponent tileComp = new TileComponent(tile);
			board.add(tileComp);
			tiles.add(tileComp);
		}
		board.repaint();
	}

	public void drawMarbles(GameMap gameMap) {
		String mapCode = gameMap.getMapCode();
		for (int i = 0; i < 100; i++)
			tiles.get(i).setMarble(
					Integer.parseInt(mapCode
							.substring((i * 2 + 1), (i * 2 + 2))));
	}

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

	private class TileComponent extends JComponent implements MouseListener {
		Image tileImage;
		int marble;
		static final int NONE = 0;
		static final int BLACK = 1;
		static final int RED = 2;
		boolean active;

		TileComponent(Image tileImage) {
			this.tileImage = tileImage;
			marble = 0;
			active = false;
			addMouseListener(this);
		}

		void setMarble(int marble) {
			this.marble = marble;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(tileImage, 0, 0, this);
			if (marble == RED) {
				g2.setColor(Color.RED);
				g2.fillOval(10, 10, 40, 40);
			}
			if (marble == BLACK) {
				g2.setColor(Color.BLACK);
				g2.fillOval(10, 10, 40, 40);
			}
			if (marble == NONE && active) {
				g2.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, (float) .7));
				g2.setColor(Color.RED);
				g2.fillOval(10, 10, 40, 40);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			active = true;
			repaint();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			active = false;
			repaint();
		}
	}

	public static void main(String[] args) {
		GameMap gameMap = new GameMap("a0a0a0k0f0f0a0a0a0a0"
				+ "a0a0o0k0f0f0p0p0a0a0" + "c0c0o0k0b1b0b0g2g0a0"
				+ "c0c0a0a0b0b0b0g0g0a0" + "c0c0a0a0l0d0d0d0a0a0"
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
		frame.setResizable(false);
		MapPainter mapPainter = new MapPainter(panel);
		mapPainter.drawMap(gameMap);
		mapPainter.drawMarbles(gameMap);
		frame.setVisible(true);
	}

}
