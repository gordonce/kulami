/**
 * 
 */
package kulami.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author gordon
 * 
 */
public class NewGameDialog extends JDialog {

	private NewGameDialogAdapter newGameDialogAdapter;
	private JTextField hostField;
	private JTextField portField;
	private JDialog loadingDialog;

	/**
	 * @param newGameDialogAdapter
	 */
	public NewGameDialog(Frame mainframe,
			NewGameDialogAdapter newGameDialogAdapter) {
		super(mainframe, true);
		this.newGameDialogAdapter = newGameDialogAdapter;

		setTitle("Mit Server verbinden");

		setLayout(new BorderLayout(5, 10));

		add(initGUI(), BorderLayout.CENTER);
		add(initButtons(), BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(mainframe);
	}

	public String getHost() {
		return hostField.getText();
	}

	public int getPort() {
		String text = portField.getText();
		if (Pattern.matches("\\d+", text))
			return Integer.parseInt(text);
		else
			return 0;
	}

	/**
	 * 
	 */
	public void clearAndHide() {
		if (loadingDialog != null)
			loadingDialog.setVisible(false);
		hostField.setText(null);
		portField.setText(null);
		setVisible(false);
	}

	public void displayWarning(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.WARNING_MESSAGE);
	}

	private Component initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton connectButton = new JButton("Verbinden");
		getRootPane().setDefaultButton(connectButton);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hostField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(NewGameDialog.this,
							"Bitte gültigen Host eingeben.", "Fehler",
							JOptionPane.WARNING_MESSAGE);
					hostField.selectAll();
					return;
				} else if (!Pattern.matches("\\d+", portField.getText())) {
					JOptionPane.showMessageDialog(NewGameDialog.this,
							"Bitte gültigen Port eingeben.", "Fehler",
							JOptionPane.WARNING_MESSAGE);
					portField.selectAll();
					return;
				}
				newGameDialogAdapter.connectClicked();
			}
		});

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGameDialogAdapter.cancelClicked();
			}
		});

		buttonPanel.add(connectButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
	}

	private Component initGUI() {
		JPanel mainPanel = new JPanel();

		hostField = new JTextField(20);
		portField = new JTextField(5);

		mainPanel.add(new JLabel("Host name: "));
		mainPanel.add(hostField);
		mainPanel.add(new JLabel("Port: "));
		mainPanel.add(portField);

		return mainPanel;
	}

	public static void main(String[] args) {
		new NewGameDialog(new javax.swing.JFrame(), null).setVisible(true);
	}

}
