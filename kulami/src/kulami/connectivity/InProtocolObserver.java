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
	public void kulamiQ();

	public void message(String msg);

	public void spielparameterQ();

	public void spielparameter(String boardCode, int level, char colour,
			String opponentName);

	public void name(String opponentName);

	public void farbe(char colour);

	public void spielstart(char colour);

	public void ungueltig(String msg);

	public void gueltig(String boardCode);

	public void zug(String boardCode);

	public void spielende(int pointsRed, int pointsBlack);

	public void playerMessage(String msg);

	public void unknownMessage(String msg);
}
