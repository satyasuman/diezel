package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.InconsistentTypePathException;
import net.ericaro.diezel.core.exceptions.UndefinedTransitionException;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.Graph.S;
import net.ericaro.diezel.core.parser.Graph.T;
import net.ericaro.diezel.core.parser.GraphBuilder;
import net.ericaro.diezel.core.parser.ParseException;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;

/**
 * built by a parser, and compiled, ready to be a DiezelLanguage instance
 * 
 * @author eric
 * 
 */
public class DiezelLanguageBuilder implements DiezelBuilder<DiezelLanguage> {

	private String header = "*   _________________________________\n"
			+ "*   ))                              (( \n"
			+ "*  ((   __    o     ___        _     ))\n"
			+ "*   ))  ))\\   _   __  ))   __  ))   (( \n"
			+ "*  ((  ((_/  ((  ((- ((__ ((- ((     ))\n"
			+ "*   ))        )) ((__     ((__ ))__  (( \n"
			+ "*  ((                                ))\n"
			+ "*   ))______________________________(( \n"
			+ "*        Diezel 2.0.0 Generated.\n*";
	private String expression; // the regexp defining the workfow
	private String packageName; // global conf: the target package name
	private String guideBaseName = "Guide";
	private List<Generic> rootTypes = new ArrayList<Generic>(); // the root
																// state
																// generics,
																// always
																// usefull to
																// start with
	private List<Transition> transitions = new ArrayList<Transition>();
	private Map<String, String> states = new HashMap<String, String>();

	// result of compilation
	transient DirectedGraph<State, TransitionInstance> graph; // graph computed
																// from the
																// expression
																// expression
	transient State start, end;

	public DiezelLanguage build() throws ParseException, DiezelException {
		buildGraph(); // parses the expression, build a graph from it,
						// associated the State and Transition objects
		nameStates();
		solveStates();

		// and copy
		DiezelLanguage that = new DiezelLanguage();
		that.header = this.header;
		that.expression = this.expression;
		that.packageName = this.packageName;
		that.guideBaseName = this.guideBaseName;
		that.rootTypes = Collections.unmodifiableList(this.rootTypes);
		that.transitions = Collections.unmodifiableList(this.transitions);
		// that.states = Collections.unmodifiableList(this.states );
		that.graph = Graphs.unmodifiableDirectedGraph(this.graph);
		that.start = this.start;
		that.end = this.end;

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
	public void buildGraph() throws ParseException,
			UndefinedTransitionException {

		Graph g = GraphBuilder.build(expression);
		graph = new DirectedSparseMultigraph<State, TransitionInstance>();
		// build a faster index for transition by name
		HashMap<String, Transition> index = new HashMap<String, Transition>();
		for (Transition t : transitions)
			index.put(t.getAlias(), t);

		{
			// check if transitions are the sames
			Set<String> gTransitions = new HashSet<String>(); // transitions
																// from the
																// graph
			for (T t : g.transitions)
				gTransitions.add(t.name);

			// transitions from the XML definition
			Set<String> xTransitions = new HashSet<String>(index.keySet()); 
			if (!xTransitions.containsAll(gTransitions)) {
				// removed known transition, leaving only the undefined ones
				gTransitions.removeAll(xTransitions); 
				throw new UndefinedTransitionException(gTransitions);
			}
			
			if (!gTransitions.containsAll(xTransitions)) {
				xTransitions.removeAll(gTransitions);  // xtransition contains "extra transitions
				System.out.println(StringUtils.toString(xTransitions , ", ","Warning: the following transition(s) defined in the Transitions element are not used in the expression: ", ""  ) );
			}

		}

		// build a map for states, and append vertices
		Map<Integer, State> stateTypes = new HashMap<Integer, State>();
		int i = 0;
		for (S s : g.states) {
			State state = new State(graph, guideBaseName + (++i),
					s.equals(g.in), s.equals(g.out));
			if (s.equals(g.out))
				end = state;
			if (s.equals(g.in))
				start = state;

			stateTypes.put(s.id, state);
			graph.addVertex(state);
		}
		// now append transitions
		for (S s : g.states)
			for (T t : s.outs)
				graph.addEdge(index.get(t.name).newInstance(graph),
						stateTypes.get(t.in.id), stateTypes.get(t.out.id));

	}

	/**
	 * parse the graph, and set a unique name for each state.
	 * 
	 */
	private void nameStates() {

		start.name = guideBaseName;

		for (Entry<String, String> e : states.entrySet()) {
			State state = getStateByPath(e.getKey());
			state.name = e.getValue();
		}

		end.name = "Out";
	}

	private State getStateByPath(String key) {
		String[] elements = key.split(",");
		State current = start;
		for (String next : elements) {
			for (TransitionInstance t : graph.getOutEdges(current)) {
				if (next.equals(t.getAlias())) {
					current = graph.getDest(t);
					break; // get out of the transition loop
				}
			}
		}
		// now the whole path has been parsed, and current is our goal
		return current;
	}

	private String generateName(int i) {
		if (i >= 0 && i < states.size())
			return states.get(i);
		return guideBaseName + i;
	}

	private void solveStates() throws InconsistentTypePathException {
		// fill the root state with the default states
		start.generics.addAll(rootTypes);
		// compute every state generics
		Set<State> knownStates = new HashSet<State>();
		knownStates.add(start);

		int stateNumber = graph.getVertexCount();
		while (knownStates.size() != stateNumber)
			for (State source : new ArrayList<State>(knownStates))
				for (TransitionInstance trans : graph.getOutEdges(source)) {
					State target = graph.getOpposite(source, trans);
					List<Generic> types = new ArrayList<Generic>(
							source.generics);// clone the source
					types.addAll(trans.getPush());
					types.removeAll(trans.getPull());
					// types contains the generics target should have
					if (!knownStates.contains(target)) {
						// it's a new, unknown state from a known one.
						target.generics = types;
						knownStates.add(target);
					} else if (!target.generics.equals(types)) // check for
																// states
						throw new InconsistentTypePathException("State "
								+ target.getName() + " has generic types "
								+ target.generics + " but from the transition "
								+ trans.getAlias()
								+ " it should have generic types " + types);
				}
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getGuideName() {
		return guideBaseName;
	}

	public String getQualifiedName() {
		return getPackageName() + "." + getGuideName();
	}

	public void setGuideName(String guideBaseName) {
		this.guideBaseName = guideBaseName;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean addRootType(Generic e) {
		return rootTypes.add(e);
	}

	public boolean addTransition(Transition e) {
		return transitions.add(e);
	}

	public void addStatePath(String path, String name) {
		this.states.put(path, name);
	}

}
