/**
 * 
 */
package kulami.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JPanel;

import kulami.game.GameObservable;
import kulami.game.board.Board;
import kulami.game.board.Marbles;

/**
 * @author gordon
 * 
 */
public class GameDisplay implements GameObserver {

	private MapPainter mapPainter;
	private GameDisplayAdapter gameDisplayAdapter;

	private static final Logger logger = Logger
			.getLogger("kulami.gui.GameDisplay");

	public GameDisplay(GameObservable game, JPanel board,
			GameDisplayAdapter gameDisplayAdapter) {
		this.gameDisplayAdapter = gameDisplayAdapter;
		mapPainter = new MapPainter(board);
		game.registerObserver(this);
	}

	// TODO Test:
	public GameDisplay(JPanel board) {
		mapPainter = new MapPainter(board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.GameObserver#gameChanged()
	 */
	@Override
	public void gameChanged(GameObservable game) {
		mapPainter.setLastMove(game.getLastMove());
		mapPainter.setPossibleMoves(game.getLegalMoves());
		mapPainter.setPanelOwners(game.getPanelOwners());
		Marbles marbles = game.getMarbles();
		mapPainter.drawMarbles(marbles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.GameObserver#boardChanged(kulami.game.GameObservable)
	 */
	@Override
	public void boardChanged(GameObservable game) {
		Board board = game.getBoard();
		logger.finer("Drawing map: " + board);
		mapPainter.drawBoard(board, game.getDisplayFlags());
		initTileListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.GameObserver#flagsChanged(kulami.control.DisplayFlags)
	 */
	@Override
	public void flagsChanged(GameObservable game) {
		mapPainter.flagsChanged();
	}

	private void initTileListeners() {
		mapPainter.registerTileListeners(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent
			 * )
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				Object source = e.getSource();
				TileComponent tile;
				if (source instanceof TileComponent)
					tile = (TileComponent) e.getSource();
				else
					return;
				gameDisplayAdapter.tileClicked(tile.getPos());

			}

		});
	}

}
