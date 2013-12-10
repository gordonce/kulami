/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

	private JTextField nameField;
	private ButtonGroup typeButtonGroup;
	private SpinnerNumberModel levelModel;
	private final static String humanCommand = "human";
	private final static String compCommand = "comp";
	private boolean human;

	private JButton okButton;

	private JButton cancelButton;

	private JRadioButton humanButton;

	private JRadioButton compButton;

	private JSpinner levelSpinner;

	private JLabel levelLabel;

	public PlayerDialog(Frame mainframe,
			PlayerDialogAdapter playerDialogAdapter) {
		super(mainframe, true);
		this.playerDialogAdapter = playerDialogAdapter;

		setTitle("Neuer Spieler");

		setLayout(new BorderLayout(5, 10));

		add(initGUI(), BorderLayout.CENTER);
		add(initButtons(), BorderLayout.SOUTH);
		
		initListeners();

		pack();
		setLocationRelativeTo(mainframe);
	}

	public String getName() {
		return nameField.getText();
	}
	
	public boolean getHuman() {
		return human;
	}
	
	public int getCompLevel() {
		return levelModel.getNumber().intValue();
	}
	
	public void clearAndHide() {
		nameField.setText(null);
		// TODO clear other compnents
		setVisible(false);
	}
	
	private void initListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				nameField.requestFocusInWindow();
			}
		});

		compButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int event = e.getStateChange();
				if (event == ItemEvent.SELECTED) {
					enableLevelSelection(true);
					human = false;
				}
				else if (event == ItemEvent.DESELECTED) {
					enableLevelSelection(false);
					human = true;
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerDialogAdapter.okPressed();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerDialogAdapter.cancelPressed();
			}
		});
	}
	
	private JPanel initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		okButton = new JButton("OK");
		cancelButton = new JButton("Abbrechen");
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		return buttonPanel;
	}

	private JPanel initGUI() {
		
		JPanel mainPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		
		nameField = new JTextField(12);
		
		typeButtonGroup = new ButtonGroup();
		humanButton = new JRadioButton("Mensch");
		humanButton.setActionCommand(humanCommand);
		humanButton.setSelected(true);
		human = true;
		
		compButton = new JRadioButton("Computer");
		compButton.setActionCommand(compCommand);
		
		typeButtonGroup.add(humanButton);
		typeButtonGroup.add(compButton);

		JPanel radioPanel = new JPanel();
		radioPanel.add(humanButton);
		radioPanel.add(compButton);
		
		levelModel = new SpinnerNumberModel(1, 1, 10, 1);
		levelSpinner = new JSpinner(levelModel);
		
		mainPanel.add(new JLabel("Name:"));
		mainPanel.add(nameField);
		mainPanel.add(new JLabel("Modus:"));
		mainPanel.add(radioPanel);
		levelLabel = new JLabel("Schwierigkeitsstufe:");
		mainPanel.add(levelLabel);
		mainPanel.add(levelSpinner);
		
		enableLevelSelection(false);
		
		return mainPanel;
	}

	private void enableLevelSelection(boolean b) {
		levelLabel.setEnabled(b);
		levelSpinner.setEnabled(b);
	}
	
}
