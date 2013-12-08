/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import kulami.control.ServerAdapter;
import kulami.game.GameObservable;

/**
 * @author gordon
 * 
 */
public class Mainframe extends JFrame {


	private GameController gameController;
	private GameDisplay gameDisplay;
	private MessageDisplay messageDisplay;
	private NewGameDialog newGameDialog;
	private PlayerDialog playerDialog;
	
	// GUI elements
	private JTextArea messageTextArea;
	private JPanel board;

	/**
	 * The Mainframe is the central GUI element. It depends on a GameController
	 * to delegate all functionality. It has a GameDisplay element that is
	 * responsible for displaying the current game.
	 * 
	 * @param gameController
	 */
	public Mainframe(GameController gameController) {
		this.gameController = gameController;
		initGUI();
		// TODO Test:
		gameDisplay = new GameDisplay(board);
		setVisible(true);
	}

	public void initGameDisplay(GameObservable game) {
		gameDisplay = new GameDisplay(game, board);
	}
	
	public void initMessageDisplay(ServerAdapter serverAdapter) {
		messageDisplay = new MessageDisplay(serverAdapter, messageTextArea);
	}
	
	public void showNewGameDialog() {
		newGameDialog = new NewGameDialog();
	}
	
	public void showNewPlayerDialog() {
		playerDialog = new PlayerDialog(this);
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

	}

	private JPanel initGameBoard() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(10, 10, 5, 5));
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
		JCheckBox possibleMovesCheckBox = new JCheckBox("mögliche Züge");
		JCheckBox boardPossessionCheckBox = new JCheckBox("Plattenbesitz");

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
		JTextField chatTextField = new JTextField();

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
				gameController.newPlayer();
			}
		});
		playerMenu.add(addPlayer);

		JMenu gameMenu = new JMenu("Server");
		
		JMenuItem startGame = new JMenuItem("Spiel starten");
		startGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gameController.newGame();
			}
		});
		gameMenu.add(startGame);
		
		gameMenu.add(new JMenuItem("Spiel abbrechen"));
		
		JMenu boardMenu = new JMenu("Spielfeld");
		
		boardMenu.add(new JMenuItem("Spielfeld erstellen"));
		boardMenu.add(new JMenuItem("Spielfeld bearbeiten"));
		boardMenu.add(new JMenuItem("Spielfeld laden"));

		mainMenu = new JMenuBar();
		mainMenu.add(playerMenu);
		mainMenu.add(gameMenu);
		mainMenu.add(boardMenu);
		
		return mainMenu;
	}

}
