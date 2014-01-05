/**
 * 
 */
package kulami.connectivity;

/**
 * The <code>MessageSender</code> class contains methods that correspond to
 * messages that the Kulami server expects from clients.
 * <p>
 * This class formats messages correctly and forwards them to a
 * <code>ServerProxy</code> for transmission to the server.
 * 
 * @author gordon
 * 
 */
public class MessageSender {
	private ServerProxy server;

	/**
	 * Constructs a new message sender and connects it to a
	 * <code>ServerProxy</code>.
	 * 
	 * @param serverProxy
	 */
	public MessageSender(ServerProxy serverProxy) {
		this.server = serverProxy;
	}

	/**
	 * Registers a new client: neuerClient\<Name\>).
	 * 
	 * @param name
	 */
	public void newClient(String name) {
		server.sendMessage(String.format("neuerClient(%s).", name));
	}

	/**
	 * Sends game parameters: spielparameter(\<Board\>,\<Level\>):
	 * 
	 * @param board
	 * @param level
	 */
	public void sendParameters(String board, int level) {
		server.sendMessage(String
				.format("spielparameter(%s,%d).", board, level));
	}

	/**
	 * Make a move: zug(x,y).
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 */
	public void makeMove(int x, int y) {
		server.sendMessage(String.format("zug(%d,%d).", x, y));
	}

	/**
	 * Send a message to the other player: message(\<Nachricht\>).
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		server.sendMessage(String.format("message(%s).", message));
	}

	/**
	 * Quit the current game: spielaufgabe.
	 */
	public void quitGame() {
		server.sendMessage("spielaufgabe.");
	}

	/**
	 * Start a rematch: neuesspiel.
	 */
	public void newGame() {
		server.sendMessage("neuesspiel.");
	}
}
