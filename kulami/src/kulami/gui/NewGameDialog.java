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
import javax.swing.JPanel;
import javax.swing.JTextField;

import kulami.control.NewGameDialogAdapter;

/**
 * @author gordon
 *
 */
public class NewGameDialog extends JDialog {

	private NewGameDialogAdapter newGameDialogAdapter;
	private JButton connectButton;
	private JButton cancelButton;
	private JTextField hostField;
	private JTextField portField;

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
		
		initListeners();
		
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
		hostField.setText(null);
		portField.setText(null);
		setVisible(false);
	}
	
	private void initListeners() {
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGameDialogAdapter.connectClicked();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGameDialogAdapter.cancelClicked();
			}
		});
	}
	private Component initButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		connectButton = new JButton("Verbinden");
		cancelButton = new JButton("Abbrechen");
		
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


}
