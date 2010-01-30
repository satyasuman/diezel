package net.ericaro.diezel.core.gen;

public class ConstructorGen extends MethodGen {

	String containingClass;
	
	public ConstructorGen container(String containingClass) {
		this.containingClass = containingClass;
		return this;
	}
	
	@Override
	protected void returnType() {
		return; //ignore return type for construct
	}
	@Override
	protected void methodName() {
		_(containingClass);
	}
	
	

	
	
	
}
