package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import edu.uci.ics.jung.graph.DirectedGraph;

public class DiezelLanguage  implements Compilable{

	String header ;
	String expression; // the regexp defining the workfow
	String packageName; // global conf: the target package name
	String guideBaseName = "Guide";
	List<Generic> rootTypes = new ArrayList<Generic>(); // the root state generics, always usefull to start with
	List<Transition> transitions = new ArrayList<Transition>();
	//List<String> states = new ArrayList<String>();

	// result of compilation
	transient DirectedGraph<State, TransitionInstance> graph; // graph computed from the expression expression
	transient State start, end;
	
	DiezelLanguage() {
		super();
	}

	public String getExpression() {
		return expression;
	}


	public String getPackageName() {
		return packageName;
	}


	public String getGuideName() {
		return guideBaseName;
	}


	public String getHeader() {
		return header;
	}



	public List<Transition> getTransitions() {
		return transitions;
	}
	public Collection<State> getStates(){
		return graph.getVertices();
	}
	
	public DirectedGraph<State, TransitionInstance> getGraph() {
		return graph;
	}

	/**
	 *   print the current graph in a graphviz format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n");
		for (State s : graph.getVertices()) 
			sb.append(s.toString()).append(";\n");
		
		for (TransitionInstance t: graph.getEdges()) 
			sb.append(t.toString()).append(";\n");
		sb.append("}\n");
		return sb.toString();
	}

	
	
	
}
