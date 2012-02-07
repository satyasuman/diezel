package net.ericaro.diezel.core.exceptions;

import java.util.Set;

import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.builder.StringUtils;

public class UndefinedTransitionException extends DiezelException {

	private static final String MISSING_TRANSITION = "Missing Transition Definition in the XML. The follow transitions are used in the expression, but are not defined in the Transitions element: ";

	public UndefinedTransitionException() {
	}

	public UndefinedTransitionException(String message, Throwable e) {
		super(message, e);
	}

	public UndefinedTransitionException(String message) {
		super(message);
	}

	public UndefinedTransitionException(Throwable e) {
		super(e);
	}

	public UndefinedTransitionException(Set<String> undefinedTransitionNames) {
		super(StringUtils.toString(undefinedTransitionNames, ", ",MISSING_TRANSITION, ""  ) );
	}

}
