/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author gordon
 *
 */
public class Mainframe extends JFrame {

	private JMenu playerMenu;
	private JMenu gameMenu;
	private JMenu boardMenu;
	private JMenuBar mainMenu;
	private JCheckBox previousMovesCheckBox;
	private JCheckBox possibleMovesCheckBox;
	private JCheckBox boardPossessionCheckBox;
	private JLabel heroStats;
	private JLabel villainStats;
	private JTextArea messageDisplay;
	private JTextField chatTextField;

	public Mainframe() {
		initGUI();
	}
	private void initGUI() {
		setLayout(new BorderLayout(5,10));
		
		
		playerMenu = new JMenu("Spieler");
		JMenuItem addPlayer = new JMenuItem("Neuer Spieler");
		addPlayer.addActionListener(new PlayerDialog(this));
		playerMenu.add(addPlayer);
		
		gameMenu = new JMenu("Server");
		gameMenu.add(new JMenuItem("Spiel starten"));
		gameMenu.add(new JMenuItem("Spiel abbrechen"));
		
		boardMenu = new JMenu("Spielfeld");
		boardMenu.add(new JMenuItem("Spielfeld erstellen"));
		boardMenu.add(new JMenuItem("Spielfeld bearbeiten"));
		boardMenu.add(new JMenuItem("Spielfeld laden"));
		
		mainMenu = new JMenuBar();
		mainMenu.add(playerMenu);
		mainMenu.add(gameMenu);
		mainMenu.add(boardMenu);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(200,600));
		leftPanel.setLayout(new GridLayout(0, 1, 5, 5));
		
		heroStats = new JLabel("Hero");
		villainStats = new JLabel("Villain");
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		previousMovesCheckBox = new JCheckBox("letzte Züge");
		possibleMovesCheckBox = new JCheckBox("mögliche Züge");
		boardPossessionCheckBox = new JCheckBox("Plattenbesitz");
		
		optionsPanel.add(previousMovesCheckBox);
		optionsPanel.add(possibleMovesCheckBox);
		optionsPanel.add(boardPossessionCheckBox);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout(5, 5));
		
		messageDisplay = new JTextArea();
		messageDisplay.setEditable(false);
		chatTextField = new JTextField();
		
		messagePanel.add(messageDisplay, BorderLayout.CENTER);
		messagePanel.add(chatTextField, BorderLayout.SOUTH);
		
		leftPanel.add(heroStats);
		leftPanel.add(villainStats);
		leftPanel.add(optionsPanel);
		leftPanel.add(messagePanel);
		
		JLabel board = new JLabel("Spielfeld", JLabel.CENTER);
		board.setPreferredSize(new Dimension(600, 600));
		
		setJMenuBar(mainMenu);
		add(board, BorderLayout.CENTER);
		add(leftPanel, BorderLayout.WEST);
		
		pack();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame mainframe = new Mainframe();
				mainframe.setTitle("Kulami");
				mainframe.setLocationRelativeTo(null);
				mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainframe.setVisible(true);
			}
		});
	}

}
