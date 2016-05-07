package kulami.gui;

/**
 * Classes that wish to receive messages intended for the user must implement
 * the MessagePager interface.
 * 
 * @author gordon
 * 
 */
public interface MessagePager {
	/**
	 * Display a text message
	 * 
	 * @param message
	 *            the message
	 */
	public void display(String message);
}
