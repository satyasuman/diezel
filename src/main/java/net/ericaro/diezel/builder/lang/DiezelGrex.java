package net.ericaro.diezel.builder.lang;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedGraph;

import net.ericaro.neogrex.Grex;


/** Diezel implementation of a grex. A Grex is a graph associated with a regular expression.
 * It handles transition-state relations. It should as far as possible hide the underlying graph object.
 * 
 * @author eric
 *
 */
public class DiezelGrex extends Grex<State, TransitionInstance> {
	
	/** Creates an empty graph
	 * 
	 */
	public DiezelGrex() {
		super();
	}
	/** creates a graph with a single transition, this is the most basic graph.
	 * 
	 * @param t
	 */
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
	
	/** return the collection of transitions involved in this graph ( remember that the graph is made of transition instance when
	 * a same "method" is used in several part of the graph.
	 * 
	 * @return
	 */
	public Collection<Transition> getTransitions() {
		Set<Transition> transitions = new HashSet<Transition>();
		for(TransitionInstance ti : g.getEdges())
			transitions.add(ti.getTransition() );
		return transitions;
	}

	/** return the starting state of this grex.
	 * 
	 * @return
	 */
	public State getStartState() {
		return in;
	}

	/** should be avoided, and the method implemented at the grex level are far better
	 * 
	 * @return
	 */
	@Deprecated
	public DirectedGraph<State, TransitionInstance> getGraph() {
		return g;
	}

	/** return true if the state is an output state.
	 * 
	 * @param state
	 * @return
	 */
	public boolean isOutput(State state) {
		return outs.contains(state);
	}

	
}
