package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

public class DiezelLanguage  {

	String header ;
	String expression; // the regexp defining the workfow
	String packageName; // global conf: the target package name
	String guideBaseName = "Guide";
	// lazy code, this should not be a field but a query to the underlying graph
	List<Transition> transitions = new ArrayList<Transition>();

	// result of compilation
	transient DirectedGraph<State, TransitionInstance> graph; // graph computed from the expression expression
	transient State start;
	
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



	public State getStartState() {
		return start;
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
