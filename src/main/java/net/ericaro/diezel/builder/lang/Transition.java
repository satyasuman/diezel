package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;


/** A transition is an edsl call, i.e an edge in the graph, or a method in a state interface.
 * 
 * @author eric
 *
 */
public class Transition {

	String alias;
	String javadoc;
	List<Generic> pushes = new ArrayList<Generic>();
	List<Generic> pulls= new ArrayList<Generic>();

	String signature ="";
	String returnType;
	
	
	
	
	
	public Transition(String alias) {
		super();
		this.alias = alias;
	}




	public String getAlias() {
		return alias;
	}
	
	
	
	
	public String getJavadoc() {
		return javadoc;
	}
	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}
	public void addPush(Generic... pushes){
		for(Generic push: pushes)
			this.pushes.add(push);
	}
	public void addPull(Generic... pulls){
		for(Generic pull: pulls)
			this.pulls.add(pull);
	}
	
	
	public List<Generic> getPushes() {
		return pushes;
	}
	public List<Generic> getPulls() {
		return pulls;
	}


	public void setSignature(String signature) {
		this.signature = signature;
		
	}


	public void setReturnType(String str) {
		this.returnType = str;
		
	}
	
	public String getSignature() {
		return signature;
	}
	
	public String getReturnType() {
		return returnType;
	}
}
