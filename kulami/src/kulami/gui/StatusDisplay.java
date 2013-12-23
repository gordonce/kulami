/**
 * 
 */
package kulami.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import kulami.game.GameObservable;

/**
 * @author gordon
 * 
 */
public class StatusDisplay implements StatusDisplayer, GameObserver {

	private JPanel heroPanel;
	private JPanel villainPanel;
	private String heroName;
	private String villainName;
	private char heroColour;
	private char villainColour;
	private int heroMarbles;
	private int villainMarbles;
	private int heroPoints;
	private int villainPoints;
	private char currentPlayer;
	private boolean pointsVisible;

	private JLabel heroNameLabel;
	private JLabel villainNameLabel;
	private JLabel heroMarblesLabel;
	private JLabel villainMarblesLabel;
	private JLabel heroPointsLabel;
	private JLabel villainPointsLabel;
	
	private static final Logger logger = Logger
			.getLogger("kulami.gui.StatusDisplay");

	public StatusDisplay(JPanel heroPanel, JPanel villainPanel) {
		this.heroPanel = heroPanel;
		this.villainPanel = villainPanel;

		initPanels();
	}

	private void initPanels() {
		heroPanel.setLayout(new GridLayout(0, 1));
		villainPanel.setLayout(new GridLayout(0, 1));
		heroNameLabel = new JLabel("Local player");
		heroPanel.add(heroNameLabel);
		villainNameLabel = new JLabel("Opponent");
		villainPanel.add(villainNameLabel);

		heroMarblesLabel = new JLabel("0 marbles");
		heroPanel.add(heroMarblesLabel);
		villainMarblesLabel = new JLabel("0 marbles");
		villainPanel.add(villainMarblesLabel);

		heroPointsLabel = new JLabel("0 points");
		heroPanel.add(heroPointsLabel);
		villainPointsLabel = new JLabel("0 points");
		villainPanel.add(villainPointsLabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.GameObserver#gameChanged(kulami.game.GameObservable)
	 */
	@Override
	public void gameChanged(GameObservable game) {
		updateHeroPanel();
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.GameObserver#boardChanged(kulami.game.GameObservable)
	 */
	@Override
	public void boardChanged(GameObservable game) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setHeroName(java.lang.String)
	 */
	@Override
	public void setHeroName(String heroName) {
		this.heroName = heroName;
		updateHeroPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setVillainName(java.lang.String)
	 */
	@Override
	public void setVillainName(String villainName) {
		this.villainName = villainName;
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setHeroColour(char)
	 */
	@Override
	public void setHeroColour(char colour) {
		this.heroColour = colour;
		updateHeroPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setVillainColour(char)
	 */
	@Override
	public void setVillainColour(char colour) {
		this.villainColour = colour;
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setHeroMarbles(int)
	 */
	@Override
	public void setHeroMarbles(int heroMarbles) {
		this.heroMarbles = heroMarbles;
		updateHeroPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setVillainMarbles(int)
	 */
	@Override
	public void setVillainMarbles(int villainMarbles) {
		this.villainMarbles = villainMarbles;
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setHeroPoints(int)
	 */
	@Override
	public void setHeroPoints(int heroPoints) {
		this.heroPoints = heroPoints;
		updateHeroPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setVillainPoints(int)
	 */
	@Override
	public void setVillainPoints(int villainPoints) {
		this.villainPoints = villainPoints;
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#setCurrentPlayer(char)
	 */
	@Override
	public void setCurrentPlayer(char colour) {
		this.currentPlayer = colour;
		logger.fine("Current player is now " + currentPlayer);
		updateHeroPanel();
		updateVillainPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.gui.StatusDisplayer#displayPoints(boolean)
	 */
	@Override
	public void displayPoints(boolean b) {
		this.pointsVisible = b;
		updateHeroPanel();
		updateVillainPanel();
	}

	private void updateHeroPanel() {
		if (currentPlayer == heroColour && currentPlayer != 0)
			heroNameLabel.setText(String.format("<html><i>%s</i>", heroName));
		else
			heroNameLabel.setText(heroName);
		if (heroColour == 'r')
			heroNameLabel.setForeground(Color.RED);
		else
			heroNameLabel.setForeground(Color.BLACK);
		heroMarblesLabel.setText(String.format("%d marbles", heroMarbles));
		if (pointsVisible)
			heroPointsLabel.setText(String.format("%d points", heroPoints));
		heroPanel.repaint();
		logger.fine("Updated hero stats");
	}

	private void updateVillainPanel() {
		if (currentPlayer == villainColour && currentPlayer != 0)
			villainNameLabel.setText(String.format("<html><i>%s</i>", villainName));
		else
			villainNameLabel.setText(villainName);
		if (villainColour == 'r')
			villainNameLabel.setForeground(Color.RED);
		else
			villainNameLabel.setForeground(Color.BLACK);
		villainMarblesLabel
				.setText(String.format("%d marbles", villainMarbles));
		if (pointsVisible)
			villainPointsLabel.setText(String
					.format("%d points", villainPoints));
		villainPanel.repaint();
		logger.fine("Updated hero stats");
	}
}
