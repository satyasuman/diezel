package net.ericaro.diezel.builder.impl;

import java.util.Collection;
import java.util.List;

import net.ericaro.diezel.builder.lang.DiezelLanguage;
import edu.uci.ics.jung.graph.Graph;

public class DiezelImplementation {

	String packageName; //package
	public String guideName; // class 
	String extendClass; // extends 
	DiezelLanguage language; // implements 
	Graph<StateImplementation, TransitionImplementationInstance> graph;
	List<TransitionImplementation> transitions;
	StateImplementation start;
	public String getPackageName() {
		return packageName;
	}

	public StateImplementation getStartState() {
		return start;
	}

	public Collection<StateImplementation> getStates(){
		return graph.getVertices();
	}
	
	public DiezelLanguage getParent(){
		return language ;
	}

	public String getExpression() {
		return language.getExpression();
	}

	public String getHeader() {
		return language.getHeader();
	}
	
	public String getExtends(){
		return extendClass ;
	}
	
	
}
