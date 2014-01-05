/**
 * 
 */
package kulami.gui;

import javax.swing.JTextArea;

/**
 * <code>MessageDisplay</code>handles displaying of messages in a text panel
 * shown to the user.
 * <p>
 * The <code>MessageDisplay</code> can be used for program messages, server
 * messages, and chat messages. The panel should also display the user's own
 * messages.
 * 
 * @author gordon
 * 
 */
public class MessageDisplay implements MessagePager {

	private JTextArea textPager;

	/**
	 * Constructs a <code>MessageDisplay</code> that displays messages in
	 * <code>textPager</code>.
	 * 
	 * @param textPager the <code>JTextArea</code> where messages are displayed
	 */
	public MessageDisplay(JTextArea textPager) {
		this.textPager = textPager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.MessagePager#display(java.lang.String)
	 */
	public void display(String message) {
		textPager.append(message + '\n');
		textPager.setCaretPosition(textPager.getDocument().getLength());
	}

}
