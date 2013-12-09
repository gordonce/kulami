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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import kulami.game.Player;

/**
 * @author gordon
 * 
 */
public class PlayerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private Player player;
	private JTextField textName;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JLabel lblLevel;

	private JSpinner spinnerLevel;

	/**
	 * Returns a new Player or null if no Player was created
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Create the dialog.
	 */
	public PlayerDialog(Frame frame) {
		super(frame, true);
		setResizable(false);
		setTitle("Neuer Spieler");

		setBounds(100, 100, 450, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);
			{
				JLabel lblName = new JLabel("Name");
				panel.add(lblName);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel);
			{
				textName = new JTextField();
				panel.add(textName);
				textName.setColumns(10);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);
			{
				JLabel lblModus = new JLabel("Modus");
				panel.add(lblModus);
			}
		}
		{
			JPanel panel_1 = new JPanel();
			contentPanel.add(panel_1);
			panel_1.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JRadioButton rdbtnHuman = new JRadioButton("Mensch");
				rdbtnHuman.setSelected(true);
				buttonGroup.add(rdbtnHuman);
				panel_1.add(rdbtnHuman);
			}
			{
				JRadioButton rdbtnComp = new JRadioButton("Computer");
				rdbtnComp.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
							setLevelSelectionActive(true);
						else if (e.getStateChange() == ItemEvent.DESELECTED)
							setLevelSelectionActive(false);
					}

				});
				buttonGroup.add(rdbtnComp);
				panel_1.add(rdbtnComp);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout fl_panel = (FlowLayout) panel.getLayout();
			fl_panel.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);
			{
				lblLevel = new JLabel(
						"Schwierigkeitsstufe");
				lblLevel.setEnabled(false);
				panel.add(lblLevel);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout fl_panel = (FlowLayout) panel.getLayout();
			fl_panel.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel);
			{
				spinnerLevel = new JSpinner();
				spinnerLevel.setEnabled(false);
				panel.add(spinnerLevel);
				spinnerLevel.setModel(new SpinnerNumberModel(1, 1, 10, 1));
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("ok clicked");
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("cancel clicked");
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}
	}

	private void setLevelSelectionActive(boolean b) {
		lblLevel.setEnabled(b);
		spinnerLevel.setEnabled(b);
		
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame("Player Dialog test");
			frame.getContentPane().add(new JLabel("testing PlayerDialog ..."));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			PlayerDialog dialog = new PlayerDialog(frame);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
