package net.ericaro.diezel.core.builder;

public class StringUtils {

	/**
	 * mimics join in python.
	 * 
	 * @param delimiter
	 * @param iterable
	 * @param builder
	 * @return
	 */
	public static StringBuilder join(String delimiter, Iterable<?> iterable, StringBuilder builder) {

		boolean first = true;
		for (Object argument : iterable) {
			if (first)
				first = false;
			else
				builder.append(delimiter);
			builder.append(argument);
		}
		return builder;

	}

	/**
	 * join items from the iterable using delimiterSymbol in between, starting with openSymbol (null supported), and ending with closeSymbol
	 * Temporary used by generated code.
	 * 
	 * @param iterable
	 * @param delimiterSymbol
	 * @param openSymbol
	 * @param closeSymbol
	 * @return
	 */
	public static String toString(Iterable<?> iterable, String delimiterSymbol, String openSymbol, String closeSymbol) {

		StringBuilder sb = new StringBuilder();
		if (openSymbol != null)
			sb.append(openSymbol);
		join(delimiterSymbol, iterable, sb);
		if (closeSymbol != null)
			sb.append(closeSymbol);
		return sb.toString();

	}
}
