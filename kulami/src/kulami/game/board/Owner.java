/**
 * 
 */
package kulami.game.board;

/**
 * @author gordon
 * 
 */
public enum Owner {
	None(0), Black(1), Red(2);

	int idx;

	Owner(int idx) {
		this.idx = idx;
	}
	
	public int getIdx() {
		return idx;
	}
}
