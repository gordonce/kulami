package kulami.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import kulami.control.DisplayFlags;
import kulami.game.board.Owner;
import kulami.game.board.Pos;

/**
 * A <code>TileComponent</code> is a <code>JComponent</code> that displays one
 * of 100 fields of a Kulami board.
 * 
 * @author gordon
 * 
 */
class TileComponent extends JComponent implements MouseListener {
	private Image tileImage;
	private int marble;
	private boolean active;
	private boolean lastMove;
	private boolean nextToLastMove;
	private DisplayFlags displayFlags;

	// private static final Logger logger = Logger
	// .getLogger("kulami.gui.MapPainter");

	private Pos pos;
	private boolean possibleMove;
	private Owner panelOwner;

	private static Color redTransparent = new Color(255, 0, 0, 128);
	private static Color blackTransparent = new Color(0, 0, 0, 128);

	static final int NONE = 0;
	static final int BLACK = 1;
	static final int RED = 2;

	/**
	 * Constructs a new <code>TileComponent</code> and initializes a
	 * <code>MouseListener</code>
	 * 
	 * @param tileImage
	 *            the <code>Image</code> to be displayed
	 * @param pos
	 *            the tile's position
	 * @param displayFlags
	 *            a reference to a <code>DisplayFlags</code> object
	 */
	public TileComponent(Image tileImage, Pos pos, DisplayFlags displayFlags) {
		this.tileImage = tileImage;
		this.pos = pos;
		this.displayFlags = displayFlags;
		marble = 0;
		active = false;
		lastMove = false;
		nextToLastMove = false;
		possibleMove = false;
		panelOwner = Owner.None;
		addMouseListener(this);
	}

	/**
	 * Draw a marble on this tile.
	 * 
	 * @param marble
	 *            {@value #BLACK}, {@value #RED}, or {@value #NONE}
	 */
	public void setMarble(int marble) {
		this.marble = marble;
	}

	/**
	 * @param lastMove
	 *            set to true if this tile displays the last move
	 */
	public void setLastMove(boolean lastMove) {
		this.lastMove = lastMove;
	}

	/**
	 * @param nextToLastMove
	 *            set to true if this tile displays the next to last move
	 */
	public void setNextToLastMove(boolean nextToLastMove) {
		this.nextToLastMove = nextToLastMove;
	}

	/**
	 * Set the owner of the panel that this tile belongs to.
	 * 
	 * @param owner
	 */
	public void setPanelOwner(Owner owner) {
		this.panelOwner = owner;
	}

	/**
	 * @param possibleMove
	 *            set to true if this tile displays a legal move
	 */
	public void setPossibleMove(boolean possibleMove) {
		this.possibleMove = possibleMove;
	}

	/**
	 * @return this tile's position
	 */
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
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) .6));
			g2.setColor(Color.GRAY);
			g2.fillOval(10, 10, 40, 40);
		}
		if (displayFlags.isPreviousMoves()) {
			if (lastMove) {
				g2.setColor(Color.DARK_GRAY);
				g2.setStroke(new BasicStroke(3));
				g2.draw(new Ellipse2D.Float(6, 6, 48, 48));
			}
			if (nextToLastMove) {
				g2.setColor(Color.LIGHT_GRAY);
				g2.setStroke(new BasicStroke(3));
				g2.draw(new Ellipse2D.Float(6, 6, 48, 48));
			}
		}
		if (displayFlags.isPossibleMoves()) {
			if (possibleMove) {
				g2.setColor(Color.GREEN);
				g2.setStroke(new BasicStroke(3));
				g2.draw(new Rectangle2D.Float(6, 6, 48, 48));
			}
		}
		if (displayFlags.isPanelPossession()) {
			if (panelOwner == Owner.Black) {
				g2.setColor(blackTransparent);
				g2.fillRect(0, 0, 60, 60);
			} else if (panelOwner == Owner.Red) {
				g2.setColor(redTransparent);
				g2.fillRect(0, 0, 60, 60);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
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
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		active = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		active = false;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TileComponent [tileImage=" + tileImage + ", marble=" + marble
				+ ", active=" + active + ", pos=" + pos + "]";
	}

}