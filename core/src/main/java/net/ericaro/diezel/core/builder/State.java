package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

/** represent a state in the EDSL, i.e. an interface with methods
 * 
 * @author eric
 *
 */
public class State {

	
	String name;
	List<Generic> generics = new ArrayList<Generic>();
	boolean isOutput = false;
	private DirectedGraph<State, TransitionInstance> graph;
	private boolean input;
	
	
	State(DirectedGraph<State, TransitionInstance> graph, boolean input, boolean output) {
		super();
		this.graph = graph;
		this.isOutput = output;
		this.input = input;
	}
	
	public String getName() {
		return name;
	}

	String asJavaType(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (generics!=null && generics.size() > 0 ){
			sb.append("<");
			StringUtils.join(",", generics, sb);
			sb.append(">");
		}
		return sb.toString() ;
	}

	public List<Generic> getGenerics() {
		return generics;
	}

	

	public boolean isInput() {
		return input;
	}

	public boolean isOutput() {
		return isOutput;
	}
	
	/** to dot graphiz protocol
	 * 
	 * @return
	 */
	public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(getName()).append("[shape=component,label=\"" + asJavaType() + "\"]");
			return sb.toString();
	}
	
	public Collection<TransitionInstance> getTransitions(){
		return graph.getOutEdges(this);
	}
	
	
}
