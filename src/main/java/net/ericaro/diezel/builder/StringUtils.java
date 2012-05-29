package net.ericaro.diezel.builder;

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
}
