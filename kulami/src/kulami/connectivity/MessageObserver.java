/**
 * 
 */
package kulami.connectivity;

/**
 * @author gordon
 *
 */
public interface MessageObserver {

	/**
	 * @param message
	 */
	void inform(String message);

	/**
	 * 
	 */
	void connectionError();

}
