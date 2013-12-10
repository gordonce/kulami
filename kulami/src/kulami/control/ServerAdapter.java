/**
 * 
 */
package kulami.control;

/**
 * The ServerAdapter knows the Kulami client/server protocol and delegates to
 * the appropriate methods.
 * 
 * @author gordon
 * 
 */
public class ServerAdapter implements MessageObserver {

	private GameController gameController;

	/**
	 * The ServerAdapter constructor needs a GameController that the
	 * ServerAdapter can delegate to.
	 * 
	 * @param gameController
	 */
	public ServerAdapter(GameController gameController) {
		this.gameController = gameController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.control.MessageObserver#inform(java.lang.String)
	 */
	@Override
	public void inform(String message) {
		System.out.println("Received from server: " + message);
		if (message.startsWith("Kulami?"))
			gameController.sendName();
		else if (message.startsWith("message("))
			gameController.displayMessage();
		else if (message.startsWith("spielparameter?"))
			gameController.sendParameters();
		else if (message.startsWith("spielparameter("))
			gameController.receiveParameters();
		else if (message.startsWith("name("))
			gameController.playerTwoConnected();
		else if (message.startsWith("farbe("))
			gameController.assignColour();
		else if (message.startsWith("spielstart("))
			gameController.startGame();
		else if (message.startsWith("ungültig("))
			gameController.illegalMove();
		else if (message.startsWith("gültig("))
			gameController.legalMove();
		else if (message.startsWith("zug("))
			gameController.opponentMoved();
		else if (message.startsWith("spielende("))
			gameController.endGame();
		else if (message.startsWith("playerMessage("))
			gameController.displayPlayerMessage();
		else
			gameController.unknownMessage();
	}

}
