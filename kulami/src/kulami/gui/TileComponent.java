/**
 * 
 */
package kulami.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import kulami.game.Pos;

class TileComponent extends JComponent implements MouseListener {
	private Image tileImage;
	private int marble;
	private boolean active;

	private Pos pos;

	static final int NONE = 0;
	static final int BLACK = 1;
	static final int RED = 2;

	public TileComponent(Image tileImage, Pos pos) {
		this.tileImage = tileImage;
		this.pos = pos;
		marble = 0;
		active = false;
		addMouseListener(this);
	}

	public void setMarble(int marble) {
		this.marble = marble;
	}

	public Pos getPos() {
		return pos;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
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
					AlphaComposite.SRC_OVER, (float) .6));
			g2.setColor(Color.GRAY);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TileComponent [tileImage=" + tileImage + ", marble=" + marble
				+ ", active=" + active + ", pos=" + pos + "]";
	}
}