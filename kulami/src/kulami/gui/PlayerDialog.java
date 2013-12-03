package kulami.gui;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PlayerDialog {

	private JFrame parent;
	
	public PlayerDialog(JFrame parent) {
		this.parent = parent;
		initDialog();
	}
	
	private void initDialog() {
		String message = "Enter player name";
		JTextField userField = new JTextField();
		
		JPanel playerTypePanel = new JPanel();
		ButtonGroup playerTypeGroup = new ButtonGroup();
		JRadioButton humanButton = new JRadioButton("menschlich", true);
		playerTypeGroup.add(humanButton);
		playerTypePanel.add(humanButton);
		JRadioButton compButton = new JRadioButton("Computer");
		playerTypeGroup.add(compButton);
		playerTypePanel.add(compButton);
		
		JOptionPane.showOptionDialog(parent,
				new Object[] {message, userField, playerTypePanel},
				"New Player",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, null, null);
	}

}
