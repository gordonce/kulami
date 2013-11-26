/**
 * 
 */
package kulami.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ServerAdapter establishes a connection to a Kulami server and distributes
 * server messages. It follows the Observable-Observer pattern. Observers must
 * implement the MessageObserver interface and register with the
 * addObserver(MessageObserver) method.
 * 
 * @author gordon
 * 
 */
public class ServerAdapter {

	private ConnectionData serverConnectionData;
	private List<MessageObserver> observers;
	private boolean listening;

	/**
	 * Create a ServerAdapter object that can be used to connect to a Kulami
	 * server and distribute server messages. The argument ConnectionData must
	 * contain the address of a running Kulami server.
	 * 
	 * @param serverConnectionData
	 */
	public ServerAdapter(ConnectionData serverConnectionData) {
		this.serverConnectionData = serverConnectionData;
		observers = new ArrayList<>();
		listening = false;
	}

	/**
	 * Establish connection with the server given in the constructor and start a
	 * new thread that runs an infinite loop that listens for Kulami server
	 * messages and distributes them to all registered MessageObservers.
	 * 
	 */
	// TODO needs to throw if connection fails
	public void connectAndListen() {
		// TODO establish connection to server

		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {
				listening = true;
				listen();
			}
		});
		clientThread.start();
	}

	/**
	 * Stop the thread listening to a Kulami server.
	 */
	public void disconnect() {
		listening = false;
	}

	private void listen() {
		try {
			Socket kulamiSocket = new Socket(
					serverConnectionData.getHostName(),
					serverConnectionData.getPort());
			BufferedReader socketReader = new BufferedReader(
					new InputStreamReader(kulamiSocket.getInputStream()));
			String message;
			while (listening) {
				message = socketReader.readLine();
				if (message != null)
					informObservers(message);
				message = null;
			}
			kulamiSocket.close();
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}
	}

	public void sendTestMessage() {
		informObservers("hello observers :-)");
	}

	/**
	 * Register a MessageObserver that wants to be sent Kulami server messages.
	 * 
	 * @param observer
	 */
	public void addObserver(MessageObserver observer) {
		observers.add(observer);
	}

	/**
	 * Remove a MessageObserver from the list of observers.
	 * 
	 * @param observer
	 */
	public void removeObserver(MessageObserver observer) {
		observers.remove(observer);
	}

	private void informObservers(String message) {
		for (MessageObserver observer : observers)
			observer.inform(message);
	}

}
