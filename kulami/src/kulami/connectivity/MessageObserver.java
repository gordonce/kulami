package kulami.connectivity;

/**
 * Each object that wants to receive incoming server messages from the
 * <code>ServerProxy</code> has to implement the <code>MessageObserver</code>
 * interface.
 * 
 * @author gordon
 * 
 */
public interface MessageObserver {

	/**
	 * Called when a new message was received from the server.
	 * 
	 * @param message
	 *            the message text
	 */
	void inform(String message);

	/**
	 * Called when an error occurred while listening to the server.
	 */
	void connectionError();

}
