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
 * The Mainframe is the central GUI element. It depends on a GameController to
 * take care of all functionality. The Mainframe does not have a direct
 * reference to the controller, but forwards all user actions to a
 * MainframeAdapter. Displaying the current game is delegated to a GameDisplay
 * and displaying messages is delegated to a MessageDisplay.
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
	 * Construct a new Mainframe with a MainframeAdapter provided by a
	 * controller and initialize all GUI elements.
	 * 
	 * @param gameController
	 */
	public Mainframe(MainframeAdapter mainframeAdapter) {
		this.mainframeAdapter = mainframeAdapter;
		initGUI();
	}

	/**
	 * Return a MessagePager that can be used to display messages to the user.
	 * The MessagePager is created by the Mainframe, because only the Mainframe
	 * knows where it wants messages to be displayed. The controller takes care
	 * of sending messages to the pager.
	 * 
	 * @return A MessagePager that can be used to display messages.
	 */
	public MessagePager getMessageDisplay() {
		return new MessageDisplay(messageTextArea);
	}
	
	public StatusDisplayer getStatusDisplay() {
		return new StatusDisplay(heroPanel, villainPanel);
	}

	/**
	 * @param game
	 * @return
	 */
	public GameDisplay initGameDisplay(GameObservable game, GameDisplayAdapter gameDisplayAdapter) {
		return new GameDisplay(game, board, gameDisplayAdapter);
	}

	/**
	 * Tell the Mainframe to display a warning dialogue.
	 * 
	 * @param message The message to be displayed.
	 */
	public void displayWarning(String message) {
		JOptionPane.showMessageDialog(this, message, "Kulami",
				JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * @param string
	 * @return
	 */
	public boolean yesNoQuestion(String message, String title) {
		int ans = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
		return ans == JOptionPane.YES_OPTION;
			
	}

	public void enableOptions(boolean enabled) {
		optionsPanel.setEnabled(enabled);
		previousMovesCheckBox.setEnabled(enabled);
		possibleMovesCheckBox.setEnabled(enabled);
		boardPossessionCheckBox.setEnabled(enabled);
	}

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
		
//		setResizable(false);

	}

	private JPanel initGameBoard() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(10, 10, 0, 0));
		boardPanel.setPreferredSize(new Dimension(600, 600));
		return boardPanel;
	}

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

	private JMenuBar initMainMenu() {
		JMenuBar mainMenu = new JMenuBar();
		
		JMenu gameMenu = new JMenu("Spiel");
		gameMenu.setMnemonic(KeyEvent.VK_S);

		JMenuItem startGame = new JMenuItem("Spiel starten");
		startGame.setMnemonic(KeyEvent.VK_S);
		startGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
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
