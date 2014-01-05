/**
 * 
 */
package kulami.connectivity;

/**
 * Each Object that wants to handle incoming server messages has to implement
 * this protocol interface.
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
	 */
	public void message(String msg);

	/**
	 * Server asked for game parameters. (We are player 1.)
	 */
	public void spielparameterQ();

	/**
	 * Server sent the game parameters. (We are player 2.) Create a GameMap, a
	 * Player, and a Game and start displaying the game.
	 * 
	 * @param opponentName
	 *            The name of the opponent.
	 * @param colour
	 *            The player's colour ('b' for black or 'r' for red).
	 * @param level
	 *            The game level (1, 2, or 3)
	 * @param mapCode
	 *            The 200-character map code.
	 * 
	 */
	public void spielparameter(String boardCode, int level, char colour,
			String opponentName);

	/**
	 * Server sent name of player 2. (We are player 1). Set the display
	 * accordingly.
	 * 
	 * @param name
	 *            The name of the opponent.
	 * 
	 */
	public void name(String opponentName);

	/**
	 * Server sent colour. (We are player 1.) The Player object and the Game
	 * object can now be created and the display adjusted.
	 * 
	 * @param colour
	 *            'b' for black or 'r' for red.
	 * 
	 */
	public void farbe(char colour);

	/**
	 * Server sent signal to start the game. The argument is the colour of the
	 * player who begins.
	 * 
	 * @param startingPlayer
	 *            'b' for black or 'r' for red.
	 * 
	 */
	public void spielstart(char colour);

	/**
	 * Server complained about an illegal move. The String indicates the reason.
	 * 
	 * @param msg
	 *            Reason for illegal move.
	 * 
	 */
	public void ungueltig(String msg);

	/**
	 * Server sent new board in response to a legal move.
	 * 
	 * @param mapCode
	 * 
	 */
	public void gueltig(String boardCode);

	/**
	 * Server sent opponent's move.
	 * 
	 * @param mapCode
	 * 
	 */
	public void zug(String boardCode);

	/**
	 * Server signaled that the game is over and sends the final points.
	 * 
	 * @param pointsBlack
	 * @param pointsRed
	 * 
	 */
	public void spielende(int pointsRed, int pointsBlack);

	/**
	 * Opponent sent a message via the server.
	 * 
	 * @param msg
	 * 
	 */
	public void playerMessage(String msg);

	public void unknownMessage(String msg);

	/**
	 * 
	 */
	public void connectionError();
}
