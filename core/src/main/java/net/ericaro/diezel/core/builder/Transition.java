package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.List;


/** A transition is an edsl call.
 * 
 * @author eric
 *
 */
public class Transition {

	String alias;
	String javadoc;
	List<Generic> generics = new ArrayList<Generic>();
	List<Generic> lessTypes= new ArrayList<Generic>();

	String signature ="";
	String returnType;
	private State nextState;
	
	
	
	
	public Transition(String alias, String afterTypeDeclaration) {
		super();
		this.alias = alias;
		this.signature = afterTypeDeclaration;
	}
	public Transition(String alias) {
		super();
		this.alias = alias;
	}




	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAfterTypeDeclaration() {
		return signature;
	}
	public void setAfterTypeDeclaration(String afterTypeDeclaration) {
		this.signature = afterTypeDeclaration;
	}
	public String getOptionalReturnType() {
		return returnType;
	}
	public void setOptionalReturnType(String optionalReturnType) {
		this.returnType = optionalReturnType;
	}
	
	
	
	public String getJavadoc() {
		return javadoc;
	}
	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}
	public void addPush(Generic... pushes){
		for(Generic push: pushes)
			generics.add(push);
	}
	public void addPull(Generic... pulls){
		for(Generic pull: pulls)
			lessTypes.add(pull);
	}
	
	
	
	
	public List<Generic> getGenerics() {
		return generics;
	}
	public String toJava(State out){
		StringBuilder builder = new StringBuilder();
		builder.append(" ");
		if (out.isOutput)
			builder.append(returnType==null?"void":returnType);
		else
			builder.append(out.asJavaType());
		
		builder.append(" ").append(signature);
		return builder.toString();
	}
	public String getSignature() {
		return signature;
	}
	
	
	
	public State getNextState() {
		return nextState;
	}
	Transition clone(State nextState){
		Transition that = new Transition(this.alias);
		that.alias = this.alias ; 
		that.generics = this.generics ;
		that.javadoc =		this.javadoc ;
		that.returnType = this.returnType;
		that.signature  =this.signature  ;
		that.nextState = nextState;
		return that;
	}
	public String getReturnType() {
		return returnType;
	}
	
	
	
	
}
