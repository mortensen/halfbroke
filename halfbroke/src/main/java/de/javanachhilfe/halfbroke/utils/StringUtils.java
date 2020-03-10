package de.javanachhilfe.halfbroke.utils;

import java.util.List;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class StringUtils {

	/**
	 * 
	 * @param separator
	 * @param strings
	 * @return
	 */
	public static String concat(char separator, List<String> strings) {
		//TODO: use streaming api
		StringBuffer resultString = new StringBuffer();
		for(String value : strings) {
			resultString.append(value);
			resultString.append(separator);
		}
		return resultString.substring(0, resultString.length() - 1);
	}

}
