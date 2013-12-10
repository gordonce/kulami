/**
 * 
 */
package kulami.gui;

import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import kulami.control.PlayerDialogAdapter;

/**
 * @author gordon
 * 
 */
public class PlayerDialog extends JDialog {

	private PlayerDialogAdapter playerDialogAdapter;
	private JOptionPane optionPane;

	private JTextField nameField;
	private ButtonGroup typeButtonGroup;
	private SpinnerNumberModel levelModel;
	private final static String humanCommand = "human";
	private final static String compCommand = "comp";

	public PlayerDialog(Frame mainframe,
			PlayerDialogAdapter playerDialogAdapter) {
		super(mainframe, true);
		this.playerDialogAdapter = playerDialogAdapter;

		createOptionPane();
		
		setContentPane(optionPane);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				nameField.requestFocusInWindow();
			}
		});

		pack();
		setLocationRelativeTo(mainframe);
	}

	private void createOptionPane() {
		String nameString = "Name";
		
		nameField = new JTextField(12);

		String typeString = "Modus";
		
		typeButtonGroup = new ButtonGroup();
		JRadioButton humanButton = new JRadioButton("Mensch");
		humanButton.setActionCommand(humanCommand);
		humanButton.setSelected(true);
		
		JRadioButton compButton = new JRadioButton("Computer");
		compButton.setActionCommand(compCommand);
		
		typeButtonGroup.add(humanButton);
		typeButtonGroup.add(compButton);

		String levelString = "Schwierigkeitsstufe";
		
		levelModel = new SpinnerNumberModel(1, 1, 10, 1);
		JSpinner levelSpinner = new JSpinner(levelModel);

		Object[] elements = { nameString, nameField, typeString,
				humanButton, compButton, levelString, levelSpinner };
		optionPane = new JOptionPane(elements, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);
	}
}
