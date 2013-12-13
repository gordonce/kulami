/**
 * 
 */
package kulami.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	 */
	// TODO needs to throw if connection fails
	public void connectAndListen() {
		// TODO establish connection to server
		try {
			kulamiSocket = new Socket(host, port);

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
		} catch (IOException e) {
			System.err.println("Couldn't connect to socket");
		}
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
		listening = false;
	}

	private void listen() {
		try {
			BufferedReader socketReader = new BufferedReader(
					new InputStreamReader(kulamiSocket.getInputStream()));
			String inMessage;
			while (listening) {
				inMessage = socketReader.readLine();
				if (inMessage != null)
					informObservers(inMessage);
				inMessage = null;
			}
		} catch (IOException e) {
			System.err.println("Couldn't read from server");
		}
	}
	
	private void send() {
		try {

			BufferedWriter socketWriter = new BufferedWriter(
					new OutputStreamWriter(kulamiSocket.getOutputStream()));
			String outMessage;
			while (listening) {
				outMessage = sendBuffer.poll();
				if (outMessage != null) {
					socketWriter.write(outMessage + '\n');
					socketWriter.flush();
					outMessage = null;
				}
				Thread.sleep(1000);
			}
			kulamiSocket.close();
		} catch (IOException | InterruptedException e) {
			System.err.println("Couldn't write to server");
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