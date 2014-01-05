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
 * A <code>ServerProxy</code> establishes a connection to a Kulami server and
 * receives server messages and sends messages to the server.
 * <p>
 * It implements the observable part of the observable/observer pattern.
 * Observers must implement the <code>MessageObserver</code> interface and
 * register with the <code>{@link #addObserver(MessageObserver)}</code> method.
 * 
 * @author gordon
 * 
 */
public class ServerProxy {

	private String host;
	private int port;

	private Socket kulamiSocket;

	private boolean listening;
	private Queue<String> sendBuffer;

	private List<MessageObserver> observers;

	private static final Logger logger = Logger
			.getLogger("kulami.control.ServerProxy");

	/**
	 * Constructs a <code>ServerProxy</code> object that can be used to connect
	 * to a Kulami server.
	 * 
	 * @param host
	 *            the host name (e.g. <code>localhost</code> or
	 *            <code>127.0.0.1</code>
	 * @param port
	 *            the port number
	 */
	public ServerProxy(String host, int port) {
		this.host = host;
		this.port = port;
		observers = new ArrayList<>();
		sendBuffer = new LinkedList<>();
		listening = false;
	}

	/**
	 * Establishes a socket connection with the server given in the constructor.
	 * <p>
	 * Start a new thread that runs an infinite loop that listens for Kulami
	 * server messages and distributes them to all registered
	 * <code>MessageObserver</code>s and another thread that sends messages to
	 * the server.
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

	/**
	 * Listens to incoming server messages.
	 * <p>
	 * This method should run in a separate thread.
	 */
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

	/**
	 * Sends messages that are added to the message queue.
	 * <p>
	 * This method should run in a separate thread.
	 */
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
				Thread.sleep(100);
			}
			kulamiSocket.close();
		} catch (IOException | InterruptedException e) {
			logger.severe("Exception sending message: " + e.getMessage());
			connectionError();
		}
	}

	/**
	 * Register a <code>MessageObserver</code> that wants to be sent Kulami
	 * server messages.
	 * 
	 * @param observer
	 */
	public void addObserver(MessageObserver observer) {
		observers.add(observer);
	}

	/**
	 * Remove a <code>MessageObserver</code> from the list of observers.
	 * 
	 * @param observer
	 */
	public void removeObserver(MessageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Send a message received from the server to all registered
	 * <code>MessageObserver</code>s.
	 * 
	 * @param message
	 */
	private void informObservers(String message) {
		for (MessageObserver observer : observers)
			observer.inform(message);
	}

	/**
	 * Notify all registered <code>MessageObserver</code> that a connection
	 * error occurred.
	 */
	private void connectionError() {
		for (MessageObserver observer : observers)
			observer.connectionError();

	}

}
