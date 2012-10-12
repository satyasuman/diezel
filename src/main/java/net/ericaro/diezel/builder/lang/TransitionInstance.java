package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	
	public Collection<? extends Generic> getUnresolved() {
		List<Generic> gs = new ArrayList<Generic>();
		// computes the generics that are not resolved in this transactions
		for(Generic g: getPush())
			if (! g.isResolved() )
				gs.add(g);
		return gs;
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
		String prototypeValue = prototype.getReturnType();
		if (prototypeValue == null) return "void";
		return prototypeValue ;
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
	
	public String toString(){
		return getAlias();
	}
	
}
