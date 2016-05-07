package kulami.connectivity;

/**
 * Each object that wants to handle incoming server messages has to implement
 * the <code>InProtocolObserver</code> interface.
 * <p>
 * The names of the methods roughly correspond to server messages and the
 * parameters to server message parameters. Question marks in server message
 * names are replaced by the letter 'Q' in method names.
 * 
 * @author gordon
 * 
 */
public interface InProtocolObserver {
	/**
	 * Server sent "Kulami?" Connection was successfully established. Continue
	 * protocol by sending the user name.
	 */
	public void kulamiQ();

	/**
	 * Server sent a message. The message is displayed to the user.
	 * 
	 * @param msg
	 *            the message
	 */
	public void message(String msg);

	/**
	 * Server asked for game parameters. (We are player 1.)
	 */
	public void spielparameterQ();

	/**
	 * Server sent the game parameters. (We are player 2.) Create a
	 * <code>GameMap</code>, a <code>Player</code>, and a <code>Game</code> and
	 * start displaying the game.
	 * 
	 * @param boardCode
	 *            the 200-character map code.
	 * @param level
	 *            the game level (0, 1, or 2)
	 * @param colour
	 *            the player's colour ('b' for black or 'r' for red).
	 * @param opponentName
	 *            the name of the opponent.
	 */
	public void spielparameter(String boardCode, int level, char colour,
			String opponentName);

	/**
	 * Server sent name of player 2. (We are player 1). Set the display
	 * accordingly.
	 * 
	 * @param opponentName
	 *            The name of the opponent.
	 */
	public void name(String opponentName);

	/**
	 * Server sent colour. (We are player 1.) The <code>Player</code> object and
	 * the <code>Game</code> object can now be created and the display adjusted.
	 * 
	 * @param colour
	 *            'b' for black or 'r' for red.
	 */
	public void farbe(char colour);

	/**
	 * Server sent signal to start the game. The argument is the colour of the
	 * player who begins.
	 * 
	 * @param colour
	 *            'b' for black or 'r' for red.
	 */
	public void spielstart(char colour);

	/**
	 * Server complained about an illegal move. The argument indicates the
	 * reason.
	 * 
	 * @param msg
	 *            Reason for illegal move.
	 */
	public void ungueltig(String msg);

	/**
	 * Server sent new board in response to a legal move.
	 * 
	 * @param boardCode
	 *            new board code
	 */
	public void gueltig(String boardCode);

	/**
	 * Server sent opponent's move.
	 * 
	 * @param boardCode
	 *            new board code
	 */
	public void zug(String boardCode);

	/**
	 * Server signaled that the game is over and sends the final points.
	 * 
	 * @param pointsRed
	 * @param pointsBlack
	 */
	public void spielende(int pointsRed, int pointsBlack);

	/**
	 * Opponent sent a message via the server.
	 * 
	 * @param msg
	 */
	public void playerMessage(String msg);

	/**
	 * Server sent an unknown message. The parameter contains the message.
	 * 
	 * @param msg
	 *            unknown message text
	 */
	public void unknownMessage(String msg);

	/**
	 * An error occurred while communicating with the server.
	 */
	public void connectionError();
}
