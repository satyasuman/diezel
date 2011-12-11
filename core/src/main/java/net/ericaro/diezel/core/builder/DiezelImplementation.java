package net.ericaro.diezel.core.builder;

import java.io.File;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;

public class DiezelImplementation implements Compilable{

	DiezelLanguage language;
	String extendClass;
	Graph<StateImplementation, TransitionImplementationInstance> graph;
	List<TransitionImplementation> transitions;
	String packageName;
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
