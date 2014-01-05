package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import kulami.control.DisplayFlags;
import kulami.game.board.Board;
import kulami.game.board.Orientation;

/**
 * The <code>MapEditor</code> is a frame in which a new board can be created.
 * <p>
 * User actions are delegated to a <code>MapEditorAdapter</code>.
 * 
 * @author gordon
 * 
 */
public class MapEditor extends JFrame {

	private MapEditorAdapter mapEditorAdapter;
	private MapPainter mapPainter;

	private static final Logger logger = Logger
			.getLogger("kulami.gui.MapEditor");

	/**
	 * Construct a new <code>MapEditor</code> and display it.
	 * 
	 * @param mapEditorAdapter
	 */
	public MapEditor(MapEditorAdapter mapEditorAdapter) {
		this.mapEditorAdapter = mapEditorAdapter;
		initGUI();
		setVisible(true);
	}

	/**
	 * Draw a <code>Board</code>.
	 * 
	 * @param board
	 */
	public void drawBoard(Board board) {
		mapPainter.drawBoard(board, new DisplayFlags());
		initTileListeners();
	}

	/**
	 * Save the <code>Board</code> into a file.
	 * 
	 * @param board
	 *            the <code>Board</code> to be saved
	 */
	public void saveMap(Board board) {
		String mapCode = board.getBoardCode();
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				writer.println(mapCode);
				logger.info(String.format(
						"Board\n%s \n in Datei %s geschrieben.",
						board.toString(), file.toString()));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, String.format(
						"Fehler beim Schreiben in Datei %s : %s",
						file.getName(), e.getMessage()), "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * close MapEditor
	 */
	public void clearAndHide() {
		setVisible(false);
	}

	/**
	 * Initialize the central GUI elements
	 */
	private void initGUI() {
		setLayout(new BorderLayout(5, 10));

		JMenuBar menu = initMenu();

		JPanel leftPanel = initLeftPanel();

		JPanel board = initBoard();
		mapPainter = new MapPainter(board);

		setJMenuBar(menu);
		add(leftPanel, BorderLayout.WEST);
		add(board, BorderLayout.CENTER);

		pack();

		setTitle("Spielfeld bearbeiten - Kulami");
		setLocationRelativeTo(null);

		// setResizable(false);

	}

	/**
	 * Initialize the main menu
	 * 
	 * @return the menu bar
	 */
	private JMenuBar initMenu() {
		JMenuBar menu = new JMenuBar();

		JMenu mapMenu = new JMenu("Spielfeld");
		mapMenu.setMnemonic(KeyEvent.VK_S);
		menu.add(mapMenu);

		JMenuItem saveItem = new JMenuItem("Spielfeld speichern");
		saveItem.setMnemonic(KeyEvent.VK_S);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Event.CTRL_MASK));
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapEditorAdapter.saveMap();
			}
		});
		mapMenu.add(saveItem);

		JMenuItem closeItem = new JMenuItem("Spielfeldeditor schließen");
		closeItem.setMnemonic(KeyEvent.VK_Q);
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				Event.CTRL_MASK));
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapEditorAdapter.closeMapEditor();
			}
		});
		mapMenu.add(closeItem);

		return menu;
	}

	/**
	 * Initialize the left panel with elements to choose a panel size and
	 * orientation.
	 * 
	 * @return the JPanel
	 */
	private JPanel initLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel
				.setPreferredSize(new Dimension(150, Mainframe.FIELDSIZE * 10));

		JPanel sizeSelectionPanel = new JPanel(new GridLayout(4, 1));
		sizeSelectionPanel.setBorder(new TitledBorder("Holzplattengröße"));
		sizeSelectionPanel.setPreferredSize(new Dimension(150,
				Mainframe.FIELDSIZE * 4));
		// panelSelectionPanel.add(new JLabel("<html><b>Holzplattengröße</b>"));
		final ButtonGroup sizeSelectionGroup = new ButtonGroup();
		JRadioButton sixButton = new JRadioButton("6 Felder", true);
		sixButton.setActionCommand("6");
		sizeSelectionGroup.add(sixButton);
		sizeSelectionPanel.add(sixButton);
		JRadioButton fourButton = new JRadioButton("4 Felder", false);
		fourButton.setActionCommand("4");
		sizeSelectionGroup.add(fourButton);
		sizeSelectionPanel.add(fourButton);
		JRadioButton threeButton = new JRadioButton("3 Felder", false);
		threeButton.setActionCommand("3");
		sizeSelectionGroup.add(threeButton);
		sizeSelectionPanel.add(threeButton);
		JRadioButton twoButton = new JRadioButton("2 Felder", false);
		twoButton.setActionCommand("2");
		sizeSelectionGroup.add(twoButton);
		sizeSelectionPanel.add(twoButton);

		JPanel orientationPanel = new JPanel(new GridLayout(2, 1));
		orientationPanel.setBorder(new TitledBorder("Ausrichtung"));
		orientationPanel.setPreferredSize(new Dimension(150,
				Mainframe.FIELDSIZE * 2));
		// orientationPanel.add(new JLabel("<html><b>Ausrichtung</b>"));
		final ButtonGroup orientationGroup = new ButtonGroup();
		JRadioButton horizontalButton = new JRadioButton("Horizontal", true);
		horizontalButton.setActionCommand("horizontal");
		orientationGroup.add(horizontalButton);
		orientationPanel.add(horizontalButton);
		JRadioButton verticalButton = new JRadioButton("Vertikal", false);
		verticalButton.setActionCommand("vertical");
		orientationGroup.add(verticalButton);
		orientationPanel.add(verticalButton);

		JButton addButton = new JButton("Hinzufügen");

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size = Integer.parseInt(sizeSelectionGroup.getSelection()
						.getActionCommand());
				Orientation orientation = orientationGroup.getSelection()
						.getActionCommand().equals("horizontal") ? Orientation.Horizontal
						: Orientation.Vertical;
				mapEditorAdapter.newPanelSelected(size, orientation);
			}
		});

		leftPanel.add(sizeSelectionPanel);
		leftPanel.add(orientationPanel);
		leftPanel.add(addButton);

		return leftPanel;
	}

	/**
	 * Initialize the panel where the board is displayed.
	 * 
	 * @return the JPanel
	 */
	private JPanel initBoard() {
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(10, 10, 0, 0));
		board.setPreferredSize(new Dimension(Mainframe.FIELDSIZE * 10,
				Mainframe.FIELDSIZE * 10));
		return board;
	}

	/**
	 * Initialize listeners for each <code>TileComponent</code>
	 */
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
				mapEditorAdapter.tileClicked(tile.getPos());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent
			 * )
			 */
			@Override
			public void mouseEntered(MouseEvent e) {
				Object source = e.getSource();
				TileComponent tile;
				if (source instanceof TileComponent)
					tile = (TileComponent) e.getSource();
				else
					return;
				mapEditorAdapter.tileEntered(tile.getPos());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent
			 * )
			 */
			@Override
			public void mouseExited(MouseEvent e) {
				Object source = e.getSource();
				TileComponent tile;
				if (source instanceof TileComponent)
					tile = (TileComponent) e.getSource();
				else
					return;
				mapEditorAdapter.tileExited(tile.getPos());
			}
		});
	}
}
