/**
 * 
 */
package kulami.control;

/**
 * @author gordon
 *
 */
// TODO methods should throw exceptions if parameters are not correct
public class MessageSender {
	private ServerProxy server;

	public MessageSender(ServerProxy serverProxy) {
		this.server = serverProxy;
	}
	
	public void newClient(String name) {
		server.sendMessage(String.format("neuerClient(%s).", name));
	}
	
	public void sendParameters(String board, int level) {
		server.sendMessage(String.format("spielparameter(%s,%d).", board, level));
	}
	
	public void makeMove(int x, int y) {
		server.sendMessage(String.format("zug(%d,%d).", x, y));
	}
	
	public void sendMessage(String message) {
		server.sendMessage(String.format("message(%s).", message));
	}
	
	public void quitGame() {
		server.sendMessage("spielaufgabe.");
	}
}
