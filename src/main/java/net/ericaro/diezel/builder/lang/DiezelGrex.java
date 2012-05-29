package net.ericaro.diezel.builder.lang;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedGraph;

import net.ericaro.neogrex.Grex;

public class DiezelGrex extends Grex<State, TransitionInstance> {

	
	
	public DiezelGrex() {
		super();
	}

	public DiezelGrex(TransitionInstance t) {
		super(t, new State(), new State());
	}

	@Override
	protected State newState() {
		return new State();
	}

	

	@Override
	protected TransitionInstance cloneTransition(TransitionInstance t) {
		return new TransitionInstance(t);
	}

	@Override
	protected DiezelGrex newInstance() {
		return new DiezelGrex();
	}

	public Collection<Transition> getTransitions() {
		Set<Transition> transitions = new HashSet<Transition>();
		for(TransitionInstance ti : g.getEdges())
			transitions.add(ti.getTransition() );
		return transitions;
	}

	public State getStartState() {
		return in;
	}

	public DirectedGraph<State, TransitionInstance> getGraph() {
		return g;
	}

	public boolean isOutput(State state) {
		return outs.contains(state);
	}

	
}
