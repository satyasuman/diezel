package net.ericaro.diezel.builder.lang;

import java.util.Collection;

/** Transitions are instanciated several times among the graph, therefore they are in reality "instances", and this class represent them
 * 
 * @author eric
 *
 */
public class TransitionInstance {

	
	private Transition prototype;
	private DiezelGrex	grex;

	public TransitionInstance(Transition prototype) {
		super();
		this.prototype = prototype;
	}
	
	public TransitionInstance(TransitionInstance clonee) {
		super();
		this.prototype = clonee.getTransition(); 
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
		return grex.getGraph().getDest(this);
	}

	public String getReturnType() {
		return prototype.getReturnType();
	}

	public String getSignature() {
		return prototype.getSignature();
	}



	public Transition getTransition() {
		return prototype;
	}

	void init(DiezelGrex grex) {
		this.grex = grex;
		
	}
	
	
//	public String toString(){
//		StringBuilder sb = new StringBuilder();
//		sb.append(graph.getSource(this).getName()).append(" -> ").append(getNextState().getName() ).append("[label=\"" +getAlias()+":"+getSignature() + "\"]");
//		return sb.toString();
//	}
	
}
