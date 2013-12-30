/**
 * 
 */
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

import kulami.game.board.GameMap;
import kulami.game.board.Orientation;

/**
 * @author gordon
 * 
 */
public class MapEditor extends JFrame {

	private MapEditorAdapter mapEditorAdapter;
	private MapPainter mapPainter;
	
	private static final Logger logger = Logger
			.getLogger("kulami.gui.MapEditor");

	public MapEditor(MapEditorAdapter mapEditorAdapter) {
		this.mapEditorAdapter = mapEditorAdapter;
		initGUI();
		setVisible(true);
	}

	public void drawMap(GameMap gameMap) {
		mapPainter.drawMap(gameMap);
		initTileListeners();
	}

	/**
	 * @param gameMap
	 */
	public void saveMap(GameMap gameMap) {
		String mapCode = gameMap.getMapCode();
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				writer.println(mapCode);
				logger.info(String.format("Board\n%s \n in Datei %s geschrieben.", gameMap.toString(), file.toString()));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, String.format(
						"Fehler beim Schreiben in Datei %s : %s",
						file.getName(), e.getMessage()), "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * MapEditor schließen.
	 */
	public void clearAndHide() {
		setVisible(false);
	}

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

	private JPanel initBoard() {
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(10, 10, 0, 0));
		board.setPreferredSize(new Dimension(Mainframe.FIELDSIZE * 10,
				Mainframe.FIELDSIZE * 10));
		return board;
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
				mapEditorAdapter.tileClicked(tile.getPos());
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
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

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
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
