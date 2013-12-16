/**
 * 
 */
package kulami.control;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ServerAdapter knows the Kulami client/server protocol and delegates to
 * the appropriate methods.
 * 
 * @author gordon
 * 
 */
public class ServerAdapter implements MessageObserver {

	private GameController gameController;
	private List<Pattern> patterns;

	/**
	 * The ServerAdapter constructor needs a GameController that the
	 * ServerAdapter object can delegate to.
	 * 
	 * @param gameController
	 */
	public ServerAdapter(GameController gameController) {
		this.gameController = gameController;
		patterns = initPatterns();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.control.MessageObserver#inform(java.lang.String)
	 */
	@Override
	public void inform(String message) {
		List<Matcher> matchers = initMatchers(message);
		System.out.println("Received from server: " + message);
		if (matchers.get(0).matches()) {
			// Kulami?
			gameController.sendName();
		} else if (matchers.get(1).matches()) {
			// message(<Nachricht>).
			String msg = matchers.get(1).group(1);
			gameController.displayMessage(msg);
		} else if (matchers.get(2).matches()) {
			// spielparameter?
			gameController.serverWantsParameters();
		} else if (matchers.get(3).matches()) {
			// spielparameter(<Board>,<Level>,<Farbe>,<Name>).
			String mapCode = matchers.get(3).group(1);
			int level = Integer.parseInt(matchers.get(3).group(2));
			char colour = matchers.get(3).group(3).charAt(0);
			String name = matchers.get(3).group(4);
			gameController.receiveParameters(mapCode, level, colour, name);
		} else if (matchers.get(4).matches()) {
			// name(<Name>).
			String name = matchers.get(4).group(1);
			gameController.playerTwoConnected(name);
		} else if (matchers.get(5).matches()) {
			// farbe(<Farbe>).
			char colour = matchers.get(5).group(1).charAt(0);
			gameController.assignColour(colour);
		} else if (matchers.get(6).matches()) {
			// spielstart(<Farbe>).
			char player = matchers.get(6).group(1).charAt(0);
			gameController.startGame(player);
		} else if (matchers.get(7).matches()) {
			// ungültig(<Nachricht>).
			String msg = matchers.get(7).group(1);
			gameController.illegalMove(msg);
		} else if (matchers.get(8).matches()) {
			// gültig(<Board>).
			String mapCode = matchers.get(8).group(1);
			gameController.legalMove(mapCode);
		} else if (matchers.get(9).matches()) {
			// zug(<Board>).
			String mapCode = matchers.get(9).group(1);
			gameController.opponentMoved(mapCode);
		} else if (matchers.get(10).matches()) {
			// spielende(<punkteRot>,<punkteSchwarz>).
			int pointsRed = Integer.parseInt(matchers.get(10).group(1));
			int pointsBlack = Integer.parseInt(matchers.get(10).group(2));
			gameController.endGame(pointsRed, pointsBlack);
		} else if (matchers.get(11).matches()) {
			// playerMessage(<Nachricht>).
			String msg = matchers.get(11).group(1);
			gameController.displayPlayerMessage(msg);
		} else
			// ???
			gameController.unknownMessage(message);
	}

	private List<Pattern> initPatterns() {
		List<Pattern> patterns = new ArrayList<>();
		patterns.add(Pattern.compile("(?i)Kulami\\?"));
		patterns.add(Pattern.compile("(?i)message\\((.*)\\)\\."));
		patterns.add(Pattern.compile("(?i)spielparameter\\?"));
		patterns.add(Pattern
				.compile("(?i)spielparameter\\(([a-r0-2]+), ?(\\d), ?(r|b), ?(.*)\\)\\."));
		patterns.add(Pattern.compile("(?i)name\\((.*)\\)\\."));
		patterns.add(Pattern.compile("(?i)farbe\\((r|b)\\)\\."));
		patterns.add(Pattern.compile("(?i)spielstart\\((r|b)\\)\\."));
		patterns.add(Pattern.compile("(?i)ungültig\\((.*)\\)\\."));
		patterns.add(Pattern.compile("(?i)gültig\\(([a-z0-2]+)\\)\\."));
		patterns.add(Pattern.compile("(?i)zug\\(([a-z0-2]+)\\)\\."));
		patterns.add(Pattern.compile("(?i)spielende\\((\\d+), ?(\\d+)\\)\\."));
		patterns.add(Pattern.compile("(?i)playerMessage\\((.*)\\)\\."));
		return patterns;
	}

	private List<Matcher> initMatchers(String message) {
		List<Matcher> matchers = new ArrayList<>();
		for (Pattern pattern : patterns)
			matchers.add(pattern.matcher(message));
		return matchers;
	}

}
