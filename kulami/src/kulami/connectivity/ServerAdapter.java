package kulami.connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code>ServerAdapter</code> decomposes Kulami client/server protocol
 * messages and delegates to the corresponding methods of an
 * <code>InProtocolObserver</code>.
 * <p>
 * <code>ServerAdapter</code> receives messages from a <code>ServerProxy</code>.
 * 
 * @author gordon
 * 
 */
public class ServerAdapter implements MessageObserver {

	private List<Pattern> patterns;
	private List<InProtocolObserver> observers;

	/**
	 * Constructs a <code>ServerAdapter</code> and initializes regular
	 * expression patterns.
	 * 
	 */
	public ServerAdapter() {
		patterns = initPatterns();
		observers = new ArrayList<>();
	}

	/**
	 * Register an <code>InProtocolObserver</code> to handle incoming server
	 * messages.
	 * 
	 * @param observer
	 */
	public void registerObserver(InProtocolObserver observer) {
		observers.add(observer);
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
			for (InProtocolObserver observer : observers)
				observer.kulamiQ();
		} else if (matchers.get(1).matches()) {
			// message(<Nachricht>).
			String msg = matchers.get(1).group(1);
			for (InProtocolObserver observer : observers)
				observer.message(msg);
		} else if (matchers.get(2).matches()) {
			// spielparameter?
			for (InProtocolObserver observer : observers)
				observer.spielparameterQ();
		} else if (matchers.get(3).matches()) {
			// spielparameter(<Board>,<Level>,<Farbe>,<Name>).
			String mapCode = matchers.get(3).group(1);
			int level = Integer.parseInt(matchers.get(3).group(2));
			char colour = matchers.get(3).group(3).charAt(0);
			String name = matchers.get(3).group(4);
			for (InProtocolObserver observer : observers)
				observer.spielparameter(mapCode, level, colour, name);
		} else if (matchers.get(4).matches()) {
			// name(<Name>).
			String name = matchers.get(4).group(1);
			for (InProtocolObserver observer : observers)
				observer.name(name);
		} else if (matchers.get(5).matches()) {
			// farbe(<Farbe>).
			char colour = matchers.get(5).group(1).charAt(0);
			for (InProtocolObserver observer : observers)
				observer.farbe(colour);
		} else if (matchers.get(6).matches()) {
			// spielstart(<Farbe>).
			char colour = matchers.get(6).group(1).charAt(0);
			for (InProtocolObserver observer : observers)
				observer.spielstart(colour);
		} else if (matchers.get(7).matches()) {
			// ungültig(<Nachricht>).
			String msg = matchers.get(7).group(1);
			for (InProtocolObserver observer : observers)
				observer.ungueltig(msg);
		} else if (matchers.get(8).matches()) {
			// gültig(<Board>).
			String mapCode = matchers.get(8).group(1);
			for (InProtocolObserver observer : observers)
				observer.gueltig(mapCode);
		} else if (matchers.get(9).matches()) {
			// zug(<Board>).
			String mapCode = matchers.get(9).group(1);
			for (InProtocolObserver observer : observers)
				observer.zug(mapCode);
		} else if (matchers.get(10).matches()) {
			// spielende(<punkteRot>,<punkteSchwarz>).
			int pointsRed = Integer.parseInt(matchers.get(10).group(1));
			int pointsBlack = Integer.parseInt(matchers.get(10).group(2));
			for (InProtocolObserver observer : observers)
				observer.spielende(pointsRed, pointsBlack);
		} else if (matchers.get(11).matches()) {
			// playerMessage(<Nachricht>).
			String msg = matchers.get(11).group(1);
			for (InProtocolObserver observer : observers)
				observer.playerMessage(msg);
		} else
			// ???
			for (InProtocolObserver observer : observers)
				observer.unknownMessage(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kulami.connectivity.MessageObserver#connectionError()
	 */
	@Override
	public void connectionError() {
		for (InProtocolObserver observer : observers)
			observer.connectionError();
	}

	/**
	 * Initializes a <code>List</code> of <code>Pattern</code>s to identify and
	 * decompose server messages.
	 * 
	 * @return patterns
	 */
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

	/**
	 * Given a server message creates a <code>List</code> of
	 * <code>Matcher</code>s from the list <code>patterns</code>.
	 * <p>
	 * The <code>patterns</code> list has to be initialized before calling this
	 * message.
	 * 
	 * @param message
	 *            the server message
	 * @return list of matchers
	 */
	private List<Matcher> initMatchers(String message) {
		List<Matcher> matchers = new ArrayList<>();
		for (Pattern pattern : patterns)
			matchers.add(pattern.matcher(message));
		return matchers;
	}

}
