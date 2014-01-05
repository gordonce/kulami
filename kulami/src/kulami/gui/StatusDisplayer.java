package kulami.gui;

/**
 * A <code>StatusDisplayer</code> displays information about the current game state.
 * 
 * @author gordon
 *
 */
/**
 * @author gordon
 * 
 */
public interface StatusDisplayer {
	/**
	 * @param heroName
	 *            the local player's name
	 */
	public void setHeroName(String heroName);

	/**
	 * @param villainName
	 *            the opponent's name
	 */
	public void setVillainName(String villainName);

	/**
	 * @param colour
	 *            the local player's colour ('r' or'b')
	 */
	public void setHeroColour(char colour);

	/**
	 * @param colour
	 *            the opponent's colour ('r' or'b')
	 */
	public void setVillainColour(char colour);

	/**
	 * @param heroMarbles
	 *            the local player's remaining marbles
	 */
	public void setHeroMarbles(int heroMarbles);

	/**
	 * @param villainMarbles
	 *            the opponent's remaining marbles
	 */
	public void setVillainMarbles(int villainMarbles);

	/**
	 * @param heroPoints
	 *            the local player's points
	 */
	public void setHeroPoints(int heroPoints);

	/**
	 * @param villainPoints
	 *            the opponent's points
	 */
	public void setVillainPoints(int villainPoints);

	/**
	 * @param colour
	 *            the current player ('r' or 'b')
	 */
	public void setCurrentPlayer(char colour);

	/**
	 * @param display
	 *            the points?
	 */
	public void displayPoints(boolean b);
}
