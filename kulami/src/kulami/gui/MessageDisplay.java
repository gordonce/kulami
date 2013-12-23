/**
 * 
 */
package kulami.gui;

import javax.swing.JTextArea;

/**
 * MessageDisplay handles displaying of messages in a text panel shown to the
 * user. This can be used for program messages, server messages, and chat
 * messages. The panel should also display the user's own messages.
 * 
 * @author gordon
 * 
 */
public class MessageDisplay implements MessagePager {

	private JTextArea textPager;

	/**
	 * The constructor takes a ServerProxy object and a text panel.
	 * 
	 * @param textPager
	 */
	public MessageDisplay(JTextArea textPager) {
		this.textPager = textPager;
	}

	/* (non-Javadoc)
	 * @see kulami.gui.MessagePager#display(java.lang.String)
	 */
	public void display(String message) {
		textPager.append(textPager.getText() + "\n" + message);
		textPager.setCaretPosition(textPager.getDocument().getLength());
	}

}
