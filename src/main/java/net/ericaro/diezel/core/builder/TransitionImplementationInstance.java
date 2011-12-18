package net.ericaro.diezel.core.builder;

import java.util.Collection;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class TransitionImplementationInstance {

	TransitionImplementation prototype;
	TransitionInstance parent;
	private DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance> graph;
	public TransitionImplementationInstance(DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance> graph, TransitionImplementation prototype, TransitionInstance parent) {
		super();
		this.prototype = prototype;
		this.parent = parent;
		this.graph  = graph;
	}
	public StateImplementation getNextState() {
		return graph.getDest(this);
	}

	public String toString(){
		return getAlias();
	}
	
	// proto delegation methods
	
	public String getBody() {
		return prototype.getBody().add("transition", this).render();
	}
	
	
	// parent delegation methods
	
	public TransitionInstance getParent() {
		return parent;
	}
	public Collection<? extends Generic> getPush() {
		return parent.getPush();
	}
	public Collection<? extends Generic> getPull() {
		return parent.getPull();
	}
	public String getAlias() {
		return parent.getAlias();
	}
	public String getJavadoc() {
		return parent.getJavadoc();
	}
	
	public String getReturnType() {
		return parent.getReturnType();
	}
	public String getSignature() {
		return parent.getSignature();
	}
	
	
	
	
	
	
}
