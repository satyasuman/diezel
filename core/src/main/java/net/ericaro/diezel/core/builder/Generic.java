package net.ericaro.diezel.core.builder;

public class Generic {

	
	String name;
	String superType;
	String extendsType;
	
	
	
	public Generic(String name) {
		super();
		this.name = name;
	}
	public Generic(String name, String extendsType) {
		super();
		this.name = name;
		this.extendsType = extendsType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuper() {
		return superType;
	}
	public void setSuperType(String superType) {
		this.superType = superType;
	}
	public String getExtends() {
		return extendsType;
	}
	public void setExtendsType(String extendsType) {
		this.extendsType = extendsType;
	}
	
	
	
}
