/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author gordon
 * 
 */
public class ChooseBoardDialog extends JDialog {

	private File boardFile;

	private ChooseBoardDialogAdapter chooseBoardDialogAdapter;
	private ButtonGroup levelGroup;
	private JTextField fileLabel;

	public ChooseBoardDialog(Frame mainframe,
			ChooseBoardDialogAdapter chooseBoardDialogAdapter) {
		super(mainframe, true);
		this.chooseBoardDialogAdapter = chooseBoardDialogAdapter;

		setTitle("Spielfeldauswahl");

		setLayout(new BorderLayout(5, 10));

		add(initGUI(), BorderLayout.CENTER);
		add(initButtons(), BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(mainframe);
	}

	public void clearAndHide() {
		// TODO clear inputs
		setVisible(false);
	}

	/**
	 * Return the board code chosen by the user or null if no board was chose.
	 * 
	 * @return The board code or null.
	 */
	public String getBoardCode() {
		if (boardFile != null) {
			try (FileInputStream fin = new FileInputStream(boardFile);
					Reader ir = new InputStreamReader(fin);
					BufferedReader in = new BufferedReader(ir);) {
				return in.readLine();
			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								this,
								"Datei konnte nicht geöffnet werden: "
										+ e.getMessage(), "Fehler",
								JOptionPane.ERROR_MESSAGE);
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * Return the chosen level.
	 * 
	 * @return The level.
	 */
	public int getLevel() {
		return Integer.parseInt(levelGroup.getSelection().getActionCommand());
	}

	public void invalidBoardCode() {
		JOptionPane.showMessageDialog(this,
				"Die gewählte Datei enthält kein gültiges Kulami-Spielfeld",
				"Fehler", JOptionPane.ERROR_MESSAGE);
	}


	private Component initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseBoardDialogAdapter.okClicked();
			}
		});
		
		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseBoardDialogAdapter.cancelClicked();
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
	}

	private Component initGUI() {
		JPanel mainPanel = new JPanel();
		int width = 400;
		mainPanel.setPreferredSize(new Dimension(width, 130));

		JPanel boardPanel = new JPanel();
		boardPanel.setPreferredSize(new Dimension(width, 58));
		boardPanel.setBorder(new TitledBorder("Spielfeld"));
		
		fileLabel = new JTextField(20);
		fileLabel.setEditable(false);
		boardPanel.add(fileLabel);
		
		JButton fileButton = new JButton("Datei auswählen");
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		boardPanel.add(fileButton);

		mainPanel.add(boardPanel);

		JPanel levelPanel = new JPanel();
		levelPanel.setPreferredSize(new Dimension(width, 58));
		levelPanel.setBorder(new TitledBorder("Level für die Punktezählung"));
		
		JRadioButton level0Button = new JRadioButton("0");
		level0Button.setActionCommand("0");
		level0Button.setSelected(true);
		JRadioButton level1Button = new JRadioButton("1");
		level1Button.setActionCommand("1");
		JRadioButton level2Button = new JRadioButton("2");
		level2Button.setActionCommand("2");

		levelGroup = new ButtonGroup();
		levelGroup.add(level0Button);
		levelGroup.add(level1Button);
		levelGroup.add(level2Button);
		
		levelPanel.add(level0Button);
		levelPanel.add(level1Button);
		levelPanel.add(level2Button);
		
		mainPanel.add(levelPanel);
		
		return mainPanel;
	}

	private void showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		try {
			boardFile = chooser.getSelectedFile();
			fileLabel.setText(boardFile.getName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Konnte Datei nicht laden: "
					+ e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void main(String[] args) {
		new ChooseBoardDialog(new javax.swing.JFrame(), null).setVisible(true);;
	}
}
