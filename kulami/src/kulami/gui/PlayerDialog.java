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
	private JPanel levelPanel;

	private JLabel levelLabel;

	public PlayerDialog(Frame mainframe,
			PlayerDialogAdapter playerDialogAdapter) {
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

	public String getName() {
		return nameField.getText();
	}
	
	public boolean getHuman() {
		return typeButtonGroup.getSelection().getActionCommand().equals(humanCommand);
	}
	
	public int getCompLevel() {
		return levelModel.getNumber().intValue();
	}
	
	public void clearAndHide() {
		nameField.setText(null);
		setVisible(false);
	}
	
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
				}
				else if (event == ItemEvent.DESELECTED) {
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

	private void enableLevelSelection(boolean b) {
		levelLabel.setEnabled(b);
		levelSpinner.setEnabled(b);
	}
	
	public static void main(String[] args) {
		new PlayerDialog(new javax.swing.JFrame(), null).setVisible(true);
	}
	
}
