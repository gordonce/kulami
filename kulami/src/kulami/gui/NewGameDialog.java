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
	public NewGameDialog(Frame mainframe, NewGameDialogAdapter newGameDialogAdapter) {
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
		// TODO error checking
		return Integer.parseInt(portField.getText());
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
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	private Component initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton connectButton = new JButton("Verbinden");
		getRootPane().setDefaultButton(connectButton);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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

	public void displayLoading() {
		loadingDialog = new JDialog(this, "Mit Server verbinden", ModalityType.DOCUMENT_MODAL);
		loadingDialog.add(new JLabel("Verbinde...", JLabel.CENTER));
		loadingDialog.setSize(250, 150);
		loadingDialog.setLocationRelativeTo(this);
		loadingDialog.setVisible(true);
	}


}
