/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

/**
 * A dialog prompting the user for a player name, a player type, and, in case of
 * a computer player, a difficulty level.
 * <p>
 * User input is delegated to a <code>PlayerDialogAdapter</code> object.
 * 
 * @author gordon
 * 
 */
public class PlayerDialog extends JDialog {

	private PlayerDialogAdapter playerDialogAdapter;

	private JTextField nameField;
	private ButtonGroup typeButtonGroup;
	private SpinnerNumberModel levelModel;
	private final static String humanCommand = "human";
	private final static String compCommand = "comp";

	private JSpinner levelSpinner;
	private JLabel levelLabel;

	/**
	 * Constructs a new <code>PlayerDialog</code>.
	 * 
	 * @param mainframe
	 *            the parent frame
	 * @param playerDialogAdapter
	 *            the adapter
	 */
	public PlayerDialog(Frame mainframe, PlayerDialogAdapter playerDialogAdapter) {
		super(mainframe, true);
		this.playerDialogAdapter = playerDialogAdapter;

		setTitle("Neuer Spieler");

		setLayout(new BorderLayout(5, 10));

		add(initGUI(), BorderLayout.CENTER);
		add(initButtons(), BorderLayout.SOUTH);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				nameField.requestFocusInWindow();
			}
		});

		pack();
		setLocationRelativeTo(mainframe);
	}

	/**
	 * Returns the player name that the user entered.
	 * 
	 * @return player name
	 */
	public String getPlayerName() {
		return nameField.getText();
	}

	/**
	 * @return <code>true</code> if the user selected human, <code>false</code>
	 *         if the user selected computer
	 */
	public boolean getHuman() {
		return typeButtonGroup.getSelection().getActionCommand()
				.equals(humanCommand);
	}

	/**
	 * Returns the selected computer difficulty level.
	 * 
	 * @return 1..10
	 */
	public int getCompLevel() {
		return levelModel.getNumber().intValue();
	}

	/**
	 * Clear and close the dialog
	 */
	public void clearAndHide() {
		nameField.setText(null);
		setVisible(false);
	}

	/**
	 * Initialize the OK and cancel buttons
	 * 
	 * @return the JPanel
	 */
	private JPanel initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerDialogAdapter.okPressed();
			}
		});

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerDialogAdapter.cancelPressed();
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
	}

	/**
	 * Initialize the user input components
	 * 
	 * @return the JPanel
	 */
	private JPanel initGUI() {
		int width = 250;

		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(width, 190));

		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(width, 57));
		namePanel.setBorder(new TitledBorder("Name"));
		nameField = new JTextField(20);
		namePanel.add(nameField);

		JPanel typePanel = new JPanel();
		typePanel.setPreferredSize(new Dimension(width, 78));
		typePanel.setBorder(new TitledBorder("Modus"));

		typeButtonGroup = new ButtonGroup();

		JRadioButton humanButton = new JRadioButton("Mensch");
		humanButton.setActionCommand(humanCommand);
		typeButtonGroup.add(humanButton);
		typePanel.add(humanButton);
		humanButton.setSelected(true);

		JRadioButton compButton = new JRadioButton("Computer");
		compButton.setActionCommand(compCommand);
		compButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int event = e.getStateChange();
				if (event == ItemEvent.SELECTED) {
					enableLevelSelection(true);
				} else if (event == ItemEvent.DESELECTED) {
					enableLevelSelection(false);
				}
			}
		});
		typeButtonGroup.add(compButton);
		typePanel.add(compButton);

		levelModel = new SpinnerNumberModel(1, 1, 10, 1);
		levelSpinner = new JSpinner(levelModel);
		levelLabel = new JLabel("Spielstärke: ");
		typePanel.add(levelLabel);
		typePanel.add(levelSpinner);

		mainPanel.add(namePanel);
		mainPanel.add(typePanel);

		enableLevelSelection(false);

		return mainPanel;
	}

	/**
	 * Enable or disable difficulty level selection
	 * 
	 * @param enabled
	 */
	private void enableLevelSelection(boolean enabled) {
		levelLabel.setEnabled(enabled);
		levelSpinner.setEnabled(enabled);
	}

	public static void main(String[] args) {
		new PlayerDialog(new javax.swing.JFrame(), null).setVisible(true);
	}

}
