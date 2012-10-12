package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

/** Compilation result for a Diezel language file, ready to be generated.
 * 
 * @author eric
 *
 */
public class DiezelLanguage  {

	String header ; // file header
	String expression; // the regexp defining the workflow
	String packageName; // target package name
	String guideBaseName = "Guide"; // defaulted to Guide, but in fact set by the XML

	// result of compilation
	transient DirectedGraph<State, TransitionInstance> graph; // graph computed from the regexp expression
	transient State start;
	
	/** Should only be created using the Builder
	 * 
	 */
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
