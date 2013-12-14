/**
 * 
 */
package kulami.gui;

/**
 * @author gordon
 *
 */
public interface StatusDisplayer {
	public void setHeroName(String heroName);
	public void setVillainName(String villainName);
	public void setHeroColour(char colour);
	public void setVillainColour(char colour); 
	public void setHeroMarbles(int heroMarbles);
	public void setVillainMarbles(int villainMarbles);
	public void setHeroPoints(int heroPoints);
	public void setVillainPoints(int villainPoints);
	public void setCurrentPlayer(char colour);
	public void displayPoints(boolean b);
}
