package net.ericaro.diezel.core.builder;

import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;


/** A state implementation add information required for the generation of an implementation
 * 
 * @author eric
 *
 */
public class StateImplementation {

	
	State parent;
	private String name;
	private DirectedGraph<StateImplementation, TransitionImplementationInstance> graph;

	public StateImplementation(DirectedGraph<StateImplementation, TransitionImplementationInstance> graph, State prototype) {
		super();
		this.parent = prototype;
		this.graph = graph;
	}

	public String getName() {
		return parent.getName()+"Impl"; // TODO use a smarter rename strategy
	}

	
	
	public State getParent() {
		return parent;
	}

	public List<Generic> getGenerics() {
		return parent.getGenerics();
	}

	public boolean isOutput() {
		return parent.isOutput();
	}
	

	public boolean isInput() {
		return parent.isInput();
	}

	public Collection<TransitionImplementationInstance> getTransitions(){
		return graph.getOutEdges(this);
	}
	
	
	
}
