package github.jschmidt10.neo4j;

import com.beust.jcommander.JCommander;

/**
 * This is a java wrapper to get around varargs vs. noargs constructor issues
 * with Scala.
 * 
 * See
 * http://stackoverflow.com/questions/3313929/how-do-i-disambiguate-in-scala-
 * between-methods-with-vararg-and-without.
 */
public class JCommanderFactory {

	/**
	 * Create a new jcommander
	 * 
	 * @param o
	 * @return jcommander
	 */
	public static JCommander newInstance(Object o) {
		return new JCommander(o);
	}
}
