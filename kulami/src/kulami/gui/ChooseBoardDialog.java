/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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

/**
 * @author gordon
 * 
 */
public class ChooseBoardDialog extends JDialog {

	private File boardFile;

	private ChooseBoardDialogAdapter chooseBoardDialogAdapter;
	private ButtonGroup levelGroup;
	private JLabel fileLabel;

	public ChooseBoardDialog(Frame mainframe,
			ChooseBoardDialogAdapter chooseBoardDialogAdapter) {
		super(mainframe, true);
		this.chooseBoardDialogAdapter = chooseBoardDialogAdapter;

		setTitle("Spielfeldauswahl");

		setLayout(new BorderLayout(5, 10));

		add(initGUI(), BorderLayout.CENTER);
		add(initButtons(), BorderLayout.SOUTH);

		initListeners();

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

	private void initListeners() {




	}

	private Component initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("OK");
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

		mainPanel.setLayout(new GridLayout(0, 2, 10, 5));

		
		JRadioButton level0Button = new JRadioButton("0");
		level0Button.setActionCommand("0");
		level0Button.setSelected(true);
		JRadioButton level1Button = new JRadioButton("1");
		level1Button.setActionCommand("1");
		JRadioButton level2Button = new JRadioButton("2");
		level2Button.setActionCommand("2");

		JPanel levelPanel = new JPanel();
		levelPanel.add(level0Button);
		levelPanel.add(level1Button);
		levelPanel.add(level2Button);

		levelGroup = new ButtonGroup();
		levelGroup.add(level0Button);
		levelGroup.add(level1Button);
		levelGroup.add(level2Button);

		JPanel boardPanel = new JPanel();
		fileLabel = new JLabel();
		JButton fileButton = new JButton("Datei auswählen");
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		boardPanel.add(fileLabel);
		boardPanel.add(fileButton);

		mainPanel.add(new JLabel("Spielfeld", JLabel.RIGHT));
		mainPanel.add(boardPanel);
		mainPanel.add(new JLabel("Level", JLabel.RIGHT));
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
}
