package kulami.control;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class serves as a starter for the Kulami client application.
 * 
 * @author gordon
 * 
 */
public class Kulami {

	private static final Logger logger;

	static {
		System.setProperty("java.util.logging.config.file",
				"logging.properties");
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger = Logger.getLogger("kulami.control.Kulami");
	}

	/**
	 * Start the Kulami client application by constructing a
	 * <code>GameController</code>.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Handler console = new ConsoleHandler();
		// console.setLevel(Level.FINE);
		// logger.addHandler(console);
		// logger.setLevel(Level.FINE);

		logger.fine("Kulami started");
		new GameController();
	}

}
