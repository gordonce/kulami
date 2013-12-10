/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kulami.control.GameController;
import kulami.control.MainframeAdapter;
import kulami.control.ServerProxy;
import kulami.game.GameObservable;

/**
 * @author gordon
 * 
 */
public class Mainframe extends JFrame {

	private MainframeAdapter mainframeAdapter;

	// GUI elements
	private JTextArea messageTextArea;
	private JPanel board;
	private JTextField chatTextField;

	/**
	 * The Mainframe is the central GUI element. It depends on a GameController
	 * to delegate all functionality. It has a GameDisplay element that is
	 * responsible for displaying the current game.
	 * 
	 * @param gameController
	 */
	public Mainframe(MainframeAdapter mainframeAdapter) {
		this.mainframeAdapter = mainframeAdapter;
		initGUI();
	}

	public void initGameDisplay(GameObservable game) {
//		gameDisplay = new GameDisplay(game, board);
	}

	public void initMessageDisplay(ServerProxy serverProxy) {
//		messageDisplay = new MessageDisplay(serverAdapter, messageTextArea);
	}

	private void initGUI() {
		setLayout(new BorderLayout(5, 10));

		JMenuBar mainMenu = initMainMenu();

		JPanel leftPanel = initLeftPanel();

		board = initGameBoard();

		setJMenuBar(mainMenu);
		add(leftPanel, BorderLayout.WEST);
		add(board, BorderLayout.CENTER);

		pack();

		setTitle("Kulami");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image icon = toolkit.getImage(getClass().getResource(
				"/images/kulami_icon.png"));
		setIconImage(icon);

	}

	private JPanel initGameBoard() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(10, 10, 0, 0));
		boardPanel.setPreferredSize(new Dimension(600, 600));
		return boardPanel;
	}

	private JPanel initLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(200, 600));
		leftPanel.setLayout(new GridLayout(0, 1, 5, 5));

		JLabel heroStats = new JLabel("Hero");
		JLabel villainStats = new JLabel("Villain");

		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(3, 1, 5, 5));

		JCheckBox previousMovesCheckBox = new JCheckBox("letzte Züge");
		previousMovesCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					mainframeAdapter.previousMovesActivated();
				else
					mainframeAdapter.previousMovesDeactivated();
			}
		});

		JCheckBox possibleMovesCheckBox = new JCheckBox("mögliche Züge");
		possibleMovesCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					mainframeAdapter.possibleMovesActivated();
				else
					mainframeAdapter.possibleMovesDeactivated();
			}
		});

		JCheckBox boardPossessionCheckBox = new JCheckBox("Plattenbesitz");
		boardPossessionCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					mainframeAdapter.boardPossessionActivated();
				else
					mainframeAdapter.boardPossessionDeactivated();
			}
		});

		optionsPanel.add(previousMovesCheckBox);
		optionsPanel.add(possibleMovesCheckBox);
		optionsPanel.add(boardPossessionCheckBox);

		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout(5, 5));

		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		JScrollPane messageScrollPane = new JScrollPane(messageTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		chatTextField = new JTextField();
		chatTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.messageEntered(chatTextField.getText());
				chatTextField.setText("");
			}
		});
		messagePanel.add(messageScrollPane, BorderLayout.CENTER);
		messagePanel.add(chatTextField, BorderLayout.SOUTH);

		leftPanel.add(heroStats);
		leftPanel.add(villainStats);
		leftPanel.add(optionsPanel);
		leftPanel.add(messagePanel);
		return leftPanel;
	}

	private JMenuBar initMainMenu() {
		JMenuBar mainMenu = new JMenuBar();
		JMenu playerMenu = new JMenu("Spieler");

		JMenuItem addPlayer = new JMenuItem("Neuer Spieler");
		addPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.newPlayerClicked();
			}
		});
		playerMenu.add(addPlayer);

		JMenu gameMenu = new JMenu("Server");

		JMenuItem startGame = new JMenuItem("Spiel starten");
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.startGameClicked();
			}
		});
		gameMenu.add(startGame);

		JMenuItem abortGame = new JMenuItem("Spiel abbrechen");
		abortGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.abortGameClicked();
			}
		});
		gameMenu.add(abortGame);

		JMenu boardMenu = new JMenu("Spielfeld");

		JMenuItem newGameMap = new JMenuItem("Spielfeld erstellen");
		newGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.newGameMapClicked();
			}
		});
		boardMenu.add(newGameMap);

		JMenuItem editGameMap = new JMenuItem("Spielfeld bearbeiten");
		editGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.editGameMapClicked();
			}
		});
		boardMenu.add(editGameMap);

		JMenuItem loadGameMap = new JMenuItem("Spielfeld laden");
		loadGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.loadGameMapClicked();
			}
		});
		boardMenu.add(loadGameMap);

		mainMenu = new JMenuBar();
		mainMenu.add(playerMenu);
		mainMenu.add(gameMenu);
		mainMenu.add(boardMenu);

		return mainMenu;
	}

}
