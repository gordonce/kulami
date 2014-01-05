/**
 * 
 */
package kulami.gui;

/**
 * Adapter interface for user input from the <code>PlayerDialog</code>
 * 
 * @author gordon
 * 
 */
public interface PlayerDialogAdapter {

	/**
	 * User clicked OK button
	 */
	public void okPressed();

	/**
	 * User clicked cancel button
	 */
	public void cancelPressed();

	/**
	 * User closed dialog window
	 */
	public void windowClosed();
}
