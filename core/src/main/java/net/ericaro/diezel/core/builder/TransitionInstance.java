package net.ericaro.diezel.core.builder;

import java.util.Collection;

import edu.uci.ics.jung.graph.DirectedGraph;

/** Transitions are instanciated several times among the graph, therefore they are in reality "instances", and this class represent them
 * 
 * @author eric
 *
 */
public class TransitionInstance {

	
	Transition prototype;
	private DirectedGraph<State, TransitionInstance> graph;

	public TransitionInstance(Transition prototype, DirectedGraph<State, TransitionInstance> graph) {
		super();
		this.prototype = prototype;
		this.graph = graph;
	}

	public Collection<? extends Generic> getPush() {
		return prototype.getPushes();
	}
	public Collection<? extends Generic> getPull() {
		return prototype.getPulls();
	}

	public String getAlias() {
		return prototype.getAlias();
	}

	public String getJavadoc() {
		return prototype.getJavadoc();
	}

	public State getNextState() {
		return graph.getDest(this);
	}

	public String getReturnType() {
		return prototype.getReturnType();
	}

	public String getSignature() {
		return prototype.getSignature();
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(graph.getSource(this).getName()).append(" -> ").append(getNextState().getName() ).append("[label=\"" +getAlias()+":"+getSignature() + "\"]");
		return sb.toString();
	}
	
}
