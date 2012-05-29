package net.ericaro.diezel.builder.lang;

import net.ericaro.diezel.DiezelException;

public class InconsistentTypePathException extends DiezelException {

	public InconsistentTypePathException() {
	}

	public InconsistentTypePathException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InconsistentTypePathException(String arg0) {
		super(arg0);
	}

	public InconsistentTypePathException(Throwable arg0) {
		super(arg0);
	}

}
