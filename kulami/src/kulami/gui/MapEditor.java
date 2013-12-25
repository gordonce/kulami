/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

/**
 * @author gordon
 * 
 */
public class MapEditor extends JFrame {

	private MapEditorAdapter mapEditorAdapter;
	private JPanel board;

	private int sixFieldsLeft = 4;
	private int fourFieldsLeft = 5;
	private int threeFieldsLeft = 4;
	private int twoFieldsLeft = 4;

	private Orientation orientation = Orientation.Horizontal;

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

		leftPanel.setPreferredSize(new Dimension(Mainframe.FIELDSIZE * 6,
				Mainframe.FIELDSIZE * 10));

		JPanel fieldsPanel = new JPanel(new GridLayout(4, 1, 5, 5));

		leftPanel.add(fieldsPanel);
		
		JPanel sixFields = new JPanel();
		sixFields.setLayout(new GridLayout(6, 6, 0, 0));
		sixFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (sixFieldsLeft > 0)
					mapEditorAdapter.newPanelSelected(6, orientation);
			}

		});
		
		JPanel fourFields = new JPanel();
		fourFields.setLayout(new GridLayout(4, 4, 0, 0));
		fourFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fourFieldsLeft > 0)
					mapEditorAdapter.newPanelSelected(4, orientation);
			}
		});
		
		JPanel threeFields = new JPanel();
		threeFields.setLayout(new GridLayout(3, 3, 0, 0));
		threeFields.setBorder(new LineBorder(Color.BLACK, 3));
		threeFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (threeFieldsLeft > 0)
					mapEditorAdapter.newPanelSelected(3, orientation);
			}
		});

		JPanel twoFields = new JPanel();
		twoFields.setLayout(new GridLayout(2, 2, 0, 0));
		twoFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (twoFieldsLeft > 0)
					mapEditorAdapter.newPanelSelected(2, orientation);
			}
		});

		fieldsPanel.add(sixFields);
		fieldsPanel.add(fourFields);
		fieldsPanel.add(threeFields);
		fieldsPanel.add(twoFields);

		return leftPanel;
	}

	private void initBoard() {
		board = new JPanel();
		board.setLayout(new GridLayout(10, 10, 0, 0));
		board.setPreferredSize(new Dimension(Mainframe.FIELDSIZE * 10,
				Mainframe.FIELDSIZE * 10));
	}
	
}
