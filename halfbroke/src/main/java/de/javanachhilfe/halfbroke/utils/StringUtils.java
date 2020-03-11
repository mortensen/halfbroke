package de.javanachhilfe.halfbroke.utils;

import java.util.List;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class StringUtils {

	/**
	 * Melting down the list of strings to a single string with values separated by the separator sign.
	 * @param separator enables the method to concat with commas or semicolons etc.
	 * @param strings the list of strings to melt down to one target string, separated by the separator
	 * @return
	 */
	public static String concat(char separator, List<String> strings) {
		return strings.stream().reduce(null, (reducedString, column) -> {
			return reducedString != null ? reducedString + separator + column : column;
		});
	}

}