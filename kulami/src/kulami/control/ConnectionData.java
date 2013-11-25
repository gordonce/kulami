/**
 * 
 */
package kulami.control;

/**
 * @author gordon
 * 
 */
public class ConnectionData {
	private String hostName;
	private int port;

	/**
	 * @param hostName
	 * @param port
	 */
	public ConnectionData(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

}
