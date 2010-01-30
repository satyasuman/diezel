package net.ericaro.diezel.core.gen;

public class ImportGen extends Gen {

	private String importedClass;

	@Override
	protected void genImpl() {
		_("import")._(importedClass)._(";\n");

	}

}
