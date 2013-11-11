/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author gordon
 *
 */
public class Mainframe {

	private static void showGUI() {
		JFrame frame = new JFrame("Kulami");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenu playerMenu = new JMenu("Spieler");
		playerMenu.add(new JMenuItem("Neuer Spieler"));
		
		JMenu gameMenu = new JMenu("Server");
		gameMenu.add(new JMenuItem("Spiel starten"));
		gameMenu.add(new JMenuItem("Spiel abbrechen"));
		
		JMenu boardMenu = new JMenu("Spielfeld");
		boardMenu.add(new JMenuItem("Spielfeld erstellen"));
		boardMenu.add(new JMenuItem("Spielfeld bearbeiten"));
		boardMenu.add(new JMenuItem("Spielfeld laden"));
		
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(playerMenu);
		mainMenu.add(gameMenu);
		mainMenu.add(boardMenu);
//		mainMenu.setPreferredSize(new Dimension(700, 50));
		
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(700, 600));
		
		frame.setJMenuBar(mainMenu);
		frame.add(label, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGUI();
			}
		});
	}

}
