/**
 * 
 */
package kulami.gui;

import javax.swing.text.JTextComponent;

import kulami.control.MessageObserver;
import kulami.control.ServerMessages;

/**
 * @author gordon
 *
 */
public class MessageDisplay implements MessageObserver {

	private JTextComponent textPager;

	
	/**
	 * @param serverMessages
	 * @param textPager
	 */
	public MessageDisplay(ServerMessages serverMessages,
			JTextComponent textPager) {
		this.textPager = textPager;
		serverMessages.addObserver(this);
	}




	/* (non-Javadoc)
	 * @see kulami.control.MessageObserver#inform(java.lang.String)
	 */
	@Override
	public void inform(String message) {
		textPager.setText(textPager.getText() + "\n" + message);
	}

}
