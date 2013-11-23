/**
 * 
 */
package kulami.control;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gordon
 *
 */
public class ServerMessages {
	
	private ConnectionData serverConnectionData;
	private List<MessageObserver> observers;

	/**
	 * @param serverConnectionData
	 */
	public ServerMessages(ConnectionData serverConnectionData) {
		this.serverConnectionData = serverConnectionData;
		observers = new ArrayList<>();
	}
	
	public void connectAndListen() {
		// TODO implement connection
		// TODO start thread with infinite loop for listening on socket
	}
	
	public void sendTestMessage() {
		informObservers("hello observers :-)");
	}
	/**
	 * @param observer
	 */
	public void addObserver(MessageObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * @param observer
	 */
	public void removeObserver(MessageObserver observer) {
		observers.remove(observer);
	}
	
	private void informObservers(String message) {
		for (MessageObserver observer: observers)
			observer.inform(message);
	}

}
