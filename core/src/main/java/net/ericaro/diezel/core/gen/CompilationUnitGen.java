package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.List;

public class CompilationUnitGen extends ClassGen {

	private String header;
	private String packageName;
	private List<ImportGen> imports;

	public CompilationUnitGen header(String header) {
		this.header = header;
		return this;
	}
	public CompilationUnitGen packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}
	
	public CompilationUnitGen imports(ImportGen... imports) {
		this.imports.addAll(Arrays.asList(imports));
		return this;
	}
	public CompilationUnitGen imports(List<ImportGen> imports) {
		this.imports.addAll(imports);
		return this;
	}
	@Override
	protected void genImpl() {
		// a class structure is
		// compilation Unit
		// header
		// package definition
		// import
		_(header)._("package ", packageName)._(packageName==null?"":";")._("\n");
		_(imports);
		super.genImpl();
	}

	
	
}
