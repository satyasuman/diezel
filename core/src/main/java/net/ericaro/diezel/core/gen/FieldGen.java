package net.ericaro.diezel.core.gen;

public class FieldGen extends Gen {

	private String modifiers;
	private String type;
	private String name;
	private String initialiser;

	public FieldGen type(String type) {
		this.type = type;
		return this;
	}
	
	public FieldGen name(String name) {
		this.name = name;
		return this;
	}
	
	public FieldGen initialiser(String initialiser) {
		this.initialiser = initialiser;
		return this;
	}
	
	public FieldGen mod(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}
	
	@Override
	protected void genImpl() {
		_(modifiers)._()._(type)._()._(name)._("=", initialiser)._(";\n");
	}

}
