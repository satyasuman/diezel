package net.ericaro.diezel.core.gen;

public class StaticGen extends Gen {

	private String codeBlock;

	@Override
	protected void genImpl() {
		_("static {\n")._(codeBlock)._("\n}\n");

	}

}
