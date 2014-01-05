/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import kulami.game.GameObservable;

/**
 * The <code>Mainframe</code> is the central GUI element.
 * <p>
 * It depends on a GameController to take care of all functionality. The
 * Mainframe does not have a direct reference to the controller, but forwards
 * all user actions to a <code>MainframeAdapter</code>.
 * <p>
 * Displaying the current game is delegated to a <code>GameDisplay</code> and
 * displaying messages is delegated to a <code>MessageDisplay</code>.
 * 
 * @author gordon
 * 
 */
public class Mainframe extends JFrame {

	public static final int FIELDSIZE = 60;

	private MainframeAdapter mainframeAdapter;

	// GUI elements
	private JTextArea messageTextArea;
	private JPanel board;
	private JTextField chatTextField;

	private JPanel heroPanel;
	private JPanel villainPanel;

	private JPanel optionsPanel;

	private JCheckBox previousMovesCheckBox;
	private JCheckBox possibleMovesCheckBox;
	private JCheckBox boardPossessionCheckBox;

	/**
	 * Constructs a new Mainframe with a MainframeAdapter provided by a
	 * controller and initialize all GUI elements.
	 * 
	 * @param gameController
	 */
	public Mainframe(MainframeAdapter mainframeAdapter) {
		this.mainframeAdapter = mainframeAdapter;
		initGUI();
	}

	/**
	 * Returns a <code>MessagePager</code> that can be used to display messages
	 * to the user.
	 * <p>
	 * The <code>MessagePager</code> is created by the Mainframe because only
	 * the Mainframe knows where it wants messages to be displayed. The
	 * controller takes care of sending messages to the pager.
	 * 
	 * @return A <code>MessagePager</code> that can be used to display messages.
	 */
	public MessagePager getMessageDisplay() {
		return new MessageDisplay(messageTextArea);
	}

	/**
	 * Returns a <code>StatusDisplayer</code> that displays the status of the
	 * current game.
	 * <p>
	 * The <code>StatusDisplayer</code> is created by the <code>Mainframe</code>
	 * , but it is the controller who calls messages to the displayer.
	 * 
	 * @return
	 */
	public StatusDisplayer getStatusDisplay() {
		return new StatusDisplay(heroPanel, villainPanel);
	}

	/**
	 * Initializes a new <code>GameDisplay</code> and returns it.
	 * <p>
	 * This method can not be called before a <code>Game</code> object has been
	 * created by the controller.
	 * 
	 * @param game
	 *            the game
	 * @param gameDisplayAdapter
	 *            an adapter for the <code>GameDisplay</code>
	 * @return a new <code>GameDisplay</code>
	 */
	public GameDisplay initGameDisplay(GameObservable game,
			GameDisplayAdapter gameDisplayAdapter) {
		return new GameDisplay(game, board, gameDisplayAdapter);
	}

	/**
	 * Tell the Mainframe to display a warning dialogue.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	public void displayWarning(String message) {
		JOptionPane.showMessageDialog(this, message, "Kulami",
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Tell the Mainframe to display a confirmation dialogue with a yes and and
	 * a no button.
	 * 
	 * @param message
	 *            The message to be displayed.
	 * @param title
	 *            the title
	 * @return <code>true</code> if the user clicked yes, <code>false</code> if
	 *         the user clicked no
	 */
	public boolean yesNoQuestion(String message, String title) {
		int ans = JOptionPane.showConfirmDialog(this, message, title,
				JOptionPane.YES_NO_OPTION);
		return ans == JOptionPane.YES_OPTION;

	}

	/**
	 * Enable or disable the game options.
	 * 
	 * @param enabled
	 */
	public void enableOptions(boolean enabled) {
		optionsPanel.setEnabled(enabled);
		previousMovesCheckBox.setEnabled(enabled);
		possibleMovesCheckBox.setEnabled(enabled);
		boardPossessionCheckBox.setEnabled(enabled);
	}

	/**
	 * Displays a message with the results of the game and ask the user whether
	 * he/she wants to have a rematch.
	 * 
	 * @param pointsHero
	 *            local player's points
	 * @param pointsVillain
	 *            opponent's points
	 * @return <code>true</code> if user wants rematch, <code>false</code> otherwise
	 */
	public boolean displayResults(int pointsHero, int pointsVillain) {
		String message;
		if (pointsHero > pointsVillain)
			message = "Das Spiel ist zu Ende. Sie haben mit %d zu %d Punkten gewonnen.\nNoch ein Spiel?";
		else if (pointsHero < pointsVillain)
			message = "Das Spiel ist zu Ende. Sie haben mit %d zu %d Punkten verloren.\nNoch ein Spiel?";
		else
			message = "Das Spiel endet unentschieden mit %d zu %d PUnkten.\nNoch ein Spiel?";

		int ans = JOptionPane.showConfirmDialog(this,
				String.format(message, pointsHero, pointsVillain), "Spielende",
				JOptionPane.YES_NO_OPTION);
		return ans == JOptionPane.YES_OPTION;

	}

	/**
	 * Initialize the central GUI elements
	 */
	private void initGUI() {
		setLayout(new BorderLayout(5, 10));

		JMenuBar mainMenu = initMainMenu();

		JPanel leftPanel = initLeftPanel();
		enableOptions(false);

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

		setResizable(false);

	}

	/**
	 * Initialize a large panel for displaying the game.
	 * 
	 * @return
	 */
	private JPanel initGameBoard() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(10, 10, 0, 0));
		boardPanel.setPreferredSize(new Dimension(600, 600));
		return boardPanel;
	}

	/**
	 * Initialize a narrow panel for displaying the game status and text
	 * messages.
	 * 
	 * @return
	 */
	private JPanel initLeftPanel() {
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(300, 600));

		JPanel statsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		statsPanel.setPreferredSize(new Dimension(300, 400));

		heroPanel = new JPanel();
		villainPanel = new JPanel();

		optionsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		optionsPanel.setBorder(new TitledBorder("Anzeigeoptionen"));

		previousMovesCheckBox = new JCheckBox("letzte Züge");
		previousMovesCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					mainframeAdapter.previousMovesActivated();
				else
					mainframeAdapter.previousMovesDeactivated();
			}
		});

		possibleMovesCheckBox = new JCheckBox("mögliche Züge");
		possibleMovesCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					mainframeAdapter.possibleMovesActivated();
				else
					mainframeAdapter.possibleMovesDeactivated();
			}
		});

		boardPossessionCheckBox = new JCheckBox("Plattenbesitz");
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

		JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
		messagePanel.setPreferredSize(new Dimension(300, 200));

		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		messageTextArea.setLineWrap(true);
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

		statsPanel.add(heroPanel);
		statsPanel.add(villainPanel);
		statsPanel.add(optionsPanel);

		leftPanel.add(statsPanel, BorderLayout.CENTER);
		leftPanel.add(messagePanel, BorderLayout.SOUTH);
		return leftPanel;
	}

	/**
	 * Initialize the main menu.
	 * 
	 * @return
	 */
	private JMenuBar initMainMenu() {
		JMenuBar mainMenu = new JMenuBar();

		JMenu gameMenu = new JMenu("Spiel");
		gameMenu.setMnemonic(KeyEvent.VK_S);

		JMenuItem startGame = new JMenuItem("Spiel starten");
		startGame.setMnemonic(KeyEvent.VK_S);
		startGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Event.CTRL_MASK));
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.startGameClicked();
			}
		});
		gameMenu.add(startGame);

		JMenuItem abortGame = new JMenuItem("Spiel abbrechen");
		abortGame.setMnemonic(KeyEvent.VK_B);
		abortGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.abortGameClicked();
			}
		});
		gameMenu.add(abortGame);

		JMenuItem exitGame = new JMenuItem("Beenden");
		exitGame.setMnemonic(KeyEvent.VK_E);
		exitGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.exitClicked();
			}
		});
		gameMenu.add(exitGame);

		JMenu boardMenu = new JMenu("Spielfeld");
		boardMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem newGameMap = new JMenuItem("Spielfeld erstellen");
		newGameMap.setMnemonic(KeyEvent.VK_E);
		newGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.newGameMapClicked();
			}
		});
		boardMenu.add(newGameMap);

		JMenuItem editGameMap = new JMenuItem("Spielfeld bearbeiten");
		editGameMap.setMnemonic(KeyEvent.VK_B);
		editGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.editGameMapClicked();
			}
		});
		boardMenu.add(editGameMap);

		JMenuItem loadGameMap = new JMenuItem("Spielfeld laden");
		loadGameMap.setMnemonic(KeyEvent.VK_L);
		loadGameMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainframeAdapter.loadGameMapClicked();
			}
		});
		boardMenu.add(loadGameMap);

		mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(boardMenu);

		return mainMenu;
	}

}
