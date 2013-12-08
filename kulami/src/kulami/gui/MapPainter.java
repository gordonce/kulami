/**
 * 
 */
package kulami.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JPanel;

import kulami.game.GameMap;

/**
 * @author gordon
 *
 */
public class MapPainter {

	JPanel board;
	
	/**
	 * @param board
	 */
	public MapPainter(JPanel board) {
		this.board = board;
		for (int i = 0; i < 100; i++) {
			board.add(new TileComponent("" + i));
		}
	}

	/**
	 * @param gameMap
	 */
	public void drawMap(GameMap gameMap) {
		// TODO get a list of 100 fields and draw them in the board panel.
		
	}
	
	private class TileComponent extends JComponent {
		Image tileImage;
		String tileText;
		
		TileComponent() {
			
		}
		
		TileComponent(Image tileImage) {
			this.tileImage = tileImage;
		}
		
		TileComponent(String tileText) {
			this.tileText = tileText;
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			
			g2.drawString(tileText, 15, 15);
		}
		
		void setTile(Image tileImage) {
			
		}
		
		void setTileText(String tileText) {
			this.tileText = tileText;
		}
	}
}

