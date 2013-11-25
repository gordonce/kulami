/**
 * 
 */
package kulami.gui;

import javax.swing.text.JTextComponent;

import kulami.control.MessageObserver;
import kulami.control.ServerMessages;

/**
 * MessageDisplay handles displaying of messages in a text panel shown to the
 * user. This can be used for program messages, server messages, and chat
 * messages. The panel should also display the user's own messages.
 * 
 * @author gordon
 * 
 */
public class MessageDisplay implements MessageObserver {

	private JTextComponent textPager;

	/**
	 * The constructor takes a ServerMessages object and a text panel. It
	 * registers the MessageDisplay as an observer of server messages.
	 * 
	 * @param serverMessages
	 * @param textPager
	 */
	public MessageDisplay(ServerMessages serverMessages,
			JTextComponent textPager) {
		this.textPager = textPager;
		serverMessages.addObserver(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.control.MessageObserver#inform(java.lang.String)
	 */
	@Override
	public void inform(String message) {
		textPager.setText(textPager.getText() + "\n" + message);
	}

}
