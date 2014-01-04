/**
 * 
 */
package kulami.connectivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * ServerProxy establishes a connection to a Kulami server and receives server
 * messages and sends messages to the server. It follows the Observable-Observer
 * pattern. Observers must implement the MessageObserver interface and register
 * with the addObserver(MessageObserver) method.
 * 
 * @author gordon
 * 
 */
public class ServerProxy {

	private String host;
	private int port;
	private List<MessageObserver> observers;
	private boolean listening;
	private Queue<String> sendBuffer;
	private Socket kulamiSocket;

	private static final Logger logger = Logger
			.getLogger("kulami.control.ServerProxy");

	/**
	 * Create a ServerProxy object that can be used to connect to a Kulami
	 * server and distribute server messages. The argument ConnectionData must
	 * contain the address of a running Kulami server.
	 * 
	 * @param serverConnectionData
	 */
	public ServerProxy(String host, int port) {
		this.host = host;
		this.port = port;
		observers = new ArrayList<>();
		sendBuffer = new LinkedList<>();
		listening = false;
	}

	/**
	 * Establish connection with the server given in the constructor and start a
	 * new thread that runs an infinite loop that listens for Kulami server
	 * messages and distributes them to all registered MessageObservers.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 * 
	 */
	public void connectAndListen() throws UnknownHostException, IOException {
		kulamiSocket = new Socket(host, port);

		logger.info(String.format("Established connectin with %s:%d.", host,
				port));

		Thread listenThread = new Thread(new Runnable() {

			@Override
			public void run() {
				listen();
			}
		});
		Thread sendThread = new Thread(new Runnable() {

			@Override
			public void run() {
				send();
			}
		});
		listening = true;
		listenThread.start();
		sendThread.start();

	}

	/**
	 * Adds a message to the queue of messages to be sent to the Kulami server
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		sendBuffer.add(message);
	}

	/**
	 * Stop the thread listening to a Kulami server.
	 */
	public void disconnect() {
		try {
			if (kulamiSocket != null)
				kulamiSocket.close();
			listening = false;
		} catch (IOException e) {
			connectionError();
		}
	}

	private void listen() {
		try (BufferedReader socketReader = new BufferedReader(
				new InputStreamReader(kulamiSocket.getInputStream()))) {
			String inMessage;
			while (listening) {
				inMessage = socketReader.readLine();
				if (inMessage != null) {
					logger.fine(String
							.format("Received message: %s", inMessage));
					informObservers(inMessage);
				}
				inMessage = null;
			}
		} catch (IOException e) {
			logger.severe("Exception listening to server: " + e.getMessage());
			connectionError();
		}
	}

	private void send() {
		try (BufferedWriter socketWriter = new BufferedWriter(
				new OutputStreamWriter(kulamiSocket.getOutputStream()))) {
			String outMessage;
			while (listening) {
				outMessage = sendBuffer.poll();
				if (outMessage != null) {
					socketWriter.write(outMessage + '\n');
					socketWriter.flush();
					logger.fine(String.format("Sent message: %s", outMessage));
					outMessage = null;
				}
				Thread.sleep(1000);
			}
			kulamiSocket.close();
		} catch (IOException | InterruptedException e) {
			logger.severe("Exception sending message: " + e.getMessage());
			connectionError();
		}
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

	private void connectionError() {
		for (MessageObserver observer : observers)
			observer.connectionError();

	}

}
