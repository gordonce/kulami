/**
 * 
 */
package kulami.control;

/**
 * ConnectionData represents the data for a socket connection to a Kulami server
 * 
 * @author gordon
 * 
 */
public class ConnectionData {
	private String hostName;
	private int port;

	/**
	 * Host name and port number of a Kulami server
	 * 
	 * @param hostName
	 * @param port
	 */
	public ConnectionData(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}

	/**
	 * Get the host name
	 * 
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Get the port number
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

}
