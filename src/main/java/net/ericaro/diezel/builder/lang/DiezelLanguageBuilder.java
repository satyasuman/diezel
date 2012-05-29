package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ericaro.diezel.DiezelException;
import net.ericaro.diezel._2_0.Diezel;
import net.ericaro.neogrex.RegExpGraphBuilder;
import net.ericaro.parser.RegExpParser;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.util.Graphs;

/**
 *  Builder for a Diezel Language.
 * 
 * @author eric
 * 
 */
public class DiezelLanguageBuilder {

	private String		header	= "*   _________________________________\n" + "*   ))                              (( \n" + "*  ((   __    o     ___        _     ))\n" + "*   ))  ))\\   _   __  ))   __  ))   (( \n" + "*  ((  ((_/  ((  ((- ((__ ((- ((     ))\n" + "*   ))        )) ((__     ((__ ))__  (( \n" + "*  ((                                ))\n" + "*   ))______________________________(( \n" + "*        Diezel 2.0.0 Generated.\n*";

	// result of compilation
	private Diezel		src;
	private DiezelGrex	grex;

	public DiezelLanguageBuilder(Diezel src) {
		this.src = src;
	}

	public DiezelLanguage build() throws DiezelException {
		buildGraph(); // parses the expression, build a graph from it,
						// associated the State and Transition objects
		buildStates();

		// and copy
		DiezelLanguage that = new DiezelLanguage();

		that.header = this.header;
		that.expression = src.getExpression();
		that.packageName = src.getPackage();
		that.guideBaseName = src.getName();
		that.transitions = new ArrayList<Transition>(grex.getTransitions());
		// that.states = Collections.unmodifiableList(this.states );
		that.graph = Graphs.unmodifiableDirectedGraph(this.grex.getGraph());
		that.start = this.grex.getStartState();

		return that;

	}

	/**
	 * build a jung graph based on the expression
	 * 
	 * @param expression
	 * @return
	 * @throws ParseException
	 * @throws UndefinedTransitionException
	 */
	private void buildGraph() {

		// build the transition singletons, as defined in the XML
		// keep them in a map for later load
		final HashMap<String, Transition> index = new HashMap<String, Transition>();
		for (net.ericaro.diezel._2_0.Transition t : src.getTransitions().getTransition())
			index.put(t.getName(), buildTransition(t));

		// parse the expression
		try {
			grex = RegExpParser.parse(src.getExpression(), new RegExpGraphBuilder<DiezelGrex, State, TransitionInstance>() {
				public DiezelGrex terminal(String name) {
					return new DiezelGrex(new TransitionInstance(index.get(name)));
				}
			});
		} catch (net.ericaro.parser.ParseException e) {
			throw new RuntimeException("failed to generate the Graph for expression " + src.getExpression(), e);
		}
	}

	/**
	 * build the inner representation of a transition
	 * 
	 * @param t
	 * @return
	 */
	private Transition buildTransition(net.ericaro.diezel._2_0.Transition t) {
		Transition tr = new Transition(t.getName());
		tr.setJavadoc(t.getJavadoc());
		tr.setReturnType(t.getReturn());
		tr.setSignature(t.getSignature());
		return tr;
	}

	/**
	 * read a generic from the XML
	 * 
	 * @param xg
	 * @return
	 */
	private Generic buildGeneric(net.ericaro.diezel._2_0.Generic xg) {
		Generic g = new Generic();
		g.setName(xg.getName());
		g.setExtends(xg.getExtends());
		g.setSuper(xg.getSuper());
		return g;
	}

	/**
	 * prepare the states
	 * @throws InconsistentTypePathException 
	 * 
	 */
	private void buildStates() throws InconsistentTypePathException {
		DirectedGraph<State, TransitionInstance> graph = grex.getGraph();
		// passes the grex to transition and states
		for(TransitionInstance t: graph.getEdges())
			t.init(grex);
		
		int i = 0;
		// force a default name for all states
		for (State s : graph.getVertices())
			s.init(grex, src.getName() + (++i)); // little trick to rename the states by default

		// the starting point has an actual name
		grex.getStartState().setName(src.getName());
		// parses the states declaration to force some names
		if (src.getStates() != null)
			for (net.ericaro.diezel._2_0.States.State s : src.getStates().getState()) {
				State state = getStateByPath(s.getPath());
				state.setName(s.getValue());
			}
		
		solveStates() ;// computes the generic states for every states...
	}


	/**
	 * turn a dot-separated string into a path, and return the state at this path
	 * 
	 * @param key
	 * @return
	 */
	private State getStateByPath(String key) {
		String[] elements = key.split("\\.");
		State current = grex.getStartState();
		for (String next : elements) {
			for (TransitionInstance t : grex.getGraph().getOutEdges(current)) {
				if (next.equals(t.getAlias())) {
					current = grex.getGraph().getDest(t);
					break; // get out of the transition loop
				}
			}
		}
		// now the whole path has been parsed, and current is our goal
		return current;
	}

	/**
	 * computes the generics values of each state. The first state to be known is the starting one.
	 * every transition can capture, or drop a generic, or generics, hence, every path must lead to a consistent generic
	 * state
	 * 
	 * @throws InconsistentTypePathException
	 */
	private void solveStates() throws InconsistentTypePathException {
		// fill the root state with the default states
		if (src.getCapture() != null)
			grex.getStartState().generics.add(buildGeneric(src.getCapture()));

		// compute every state generics
		Set<State> knownStates = new HashSet<State>();
		knownStates.add(grex.getStartState());

		DirectedGraph<State, TransitionInstance> graph = grex.getGraph();
		int stateNumber = graph.getVertexCount();
		while (knownStates.size() != stateNumber)
			for (State source : new ArrayList<State>(knownStates))
				for (TransitionInstance trans : graph.getOutEdges(source)) {
					State target = graph.getOpposite(source, trans);
					List<Generic> types = new ArrayList<Generic>(source.generics);// clone the source
					types.addAll(trans.getPush());
					types.removeAll(trans.getPull());
					// types contains the generics target should have
					if (!knownStates.contains(target)) {
						// it's a new, unknown state from a known one.
						target.generics = types;
						knownStates.add(target);
					} else if (!target.generics.equals(types)) // check for
																// states
						throw new InconsistentTypePathException("State " + target.getName() + " has generic types " + target.generics + " but from the transition " + trans.getAlias() + " it should have generic types " + types);
				}
	}

	public String getQualifiedName() {
		return src.getPackage() + "." + src.getName();
	}
}
