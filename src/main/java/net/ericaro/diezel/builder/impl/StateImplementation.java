package net.ericaro.diezel.builder.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.ericaro.diezel.builder.lang.Generic;
import net.ericaro.diezel.builder.lang.State;
import edu.uci.ics.jung.graph.DirectedGraph;


/** A state implementation add information required for the generation of an implementation
 * 
 * @author eric
 *
 */
public class StateImplementation {

	
	State parent;
	private String name;
	private List<TransitionImplementationInstance>	transitions = new ArrayList<TransitionImplementationInstance>(); 

	public StateImplementation(State prototype, String name) {
		super();
		this.parent = prototype;
		this.name = name;
	}

	public String getName() {
		return name+parent.getName();
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
		return transitions;
	}
	
	
	
}
