package net.ericaro.diezel.core.gen;


public class Modifier extends Gen {
	
	public static final Modifier Public= new Modifier("public");
	public static final Modifier Protected= new Modifier("protected");
	public static final Modifier Private= new Modifier("private");
	
	public static final Modifier Static= new Modifier("static");
	public static final Modifier Final= new Modifier("final");
	public static final Modifier Synchronized= new Modifier("synchronized");

	private String name;
	
	
	public Modifier(){}
	
	protected Modifier(String name){
		this.name= name;
	}
	

	@Override
	protected void genImpl() {
		_(name);
	}
	
	
	
}
