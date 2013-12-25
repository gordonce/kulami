/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * @author gordon
 *
 */
public class MapEditor extends JFrame {

	private MapEditorAdapter mapEditorAdapter;
	private JPanel board;

	public MapEditor(MapEditorAdapter mapEditorAdapter) {
		this.mapEditorAdapter = mapEditorAdapter;
		initGUI();
		setVisible(true);
	}
	
	private void initGUI() {
		setLayout(new BorderLayout(5, 10));

		JMenuBar menu = initMenu();

		JPanel leftPanel = initLeftPanel();

		initBoard();

		setJMenuBar(menu);
		add(leftPanel, BorderLayout.WEST);
		add(board, BorderLayout.CENTER);

		pack();

		setTitle("Spielfeld bearbeiten - Kulami");
//		setLocationRelativeTo(null);

//		setResizable(false);

	}
	
	private JMenuBar initMenu() {
		JMenuBar menu = new JMenuBar();
		return menu;
	}
	
	private JPanel initLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.add(new JLabel("left panel"));
		return leftPanel;
	}
	
	private void initBoard() {
		board = new JPanel();
		board.add(new JLabel("board"));
	}
}
