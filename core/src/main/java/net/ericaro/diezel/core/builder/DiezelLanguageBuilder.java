package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.InconsistentTypePathException;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.Graph.S;
import net.ericaro.diezel.core.parser.Graph.T;
import net.ericaro.diezel.core.parser.GraphBuilder;
import net.ericaro.diezel.core.parser.ParseException;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;

/** built by a parser, and compiled, ready to be a DiezelLanguage instance 
 * 
 * @author eric
 *
 */
public class DiezelLanguageBuilder implements DiezelBuilder<DiezelLanguage> {

	private String header = "*   _________________________________\n" + 
			"*   ))                              (( \n" + 
			"*  ((   __    o     ___        _     ))\n" + 
			"*   ))  ))\\   _   __  ))   __  ))   (( \n" + 
			"*  ((  ((_/  ((  ((- ((__ ((- ((     ))\n" + 
			"*   ))        )) ((__     ((__ ))__  (( \n" + 
			"*  ((                                ))\n" + 
			"*   ))______________________________(( \n" + 
			"*        Diezel 2.0.0 Generated.\n*";
	private String expression; // the regexp defining the workfow
	private String packageName; // global conf: the target package name
	private String guideBaseName = "Guide";
	private List<Generic> rootTypes = new ArrayList<Generic>(); // the root state generics, always usefull to start with
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<String> states = new ArrayList<String>();

	// result of compilation
	transient DirectedGraph<State, TransitionInstance> graph; // graph computed from the expression expression
	transient State start, end;
	
	
	public DiezelLanguage build() throws ParseException, DiezelException{
		buildGraph();
		buildGraph(); // parses the expression, build a graph from it, associated the State and Transition objects
		nameStates();
		solveStates(); 
		
		// and copy
		DiezelLanguage that = new DiezelLanguage();
		that.header         = this.header       ;
		that.expression     = this.expression   ;
		that.packageName    = this.packageName  ;
		that.guideBaseName  = this.guideBaseName;
		that.rootTypes      = Collections.unmodifiableList(this.rootTypes    );
		that.transitions    = Collections.unmodifiableList(this.transitions  );
		that.states         = Collections.unmodifiableList(this.states       );
		that.graph          = Graphs.unmodifiableDirectedGraph(this.graph    );
		that.start          = this.start        ;
		that.end            = this.end          ;
		
		
		return that;
		
		
		
	}
	
	/**
	 * build a jung graph based on the expression
	 * 
	 * @param expression
	 * @return
	 * @throws ParseException
	 */
	public void buildGraph() throws ParseException {

		Graph g = GraphBuilder.build(expression);
		graph = new DirectedSparseMultigraph<State, TransitionInstance>();
		// build a faster index for transition by name
		HashMap<String, Transition> index = new HashMap<String, Transition>();
		for (Transition t : transitions)
			index.put(t.getAlias(), t);
		
		// build a map for states, and append vertices
		Map<Integer, State> stateTypes = new HashMap<Integer, State>();
		for (S s : g.states) {
			State state = new State(graph, s.equals(g.in), s.equals(g.out));
			if (s.equals(g.out))
				end = state;
			if (s.equals(g.in))
				start = state;
			
			stateTypes.put(s.id, state);
			graph.addVertex(state);
		}
		// now append transitions
		for (S s : g.states) 
			for(T t: s.outs)
				graph.addEdge(index.get(t.name).newInstance(graph), stateTypes.get(t.in.id),stateTypes.get(t.out.id) );
	}
	

	/** parse the graph, and set a unique name for each state.
	 * 
	 */
	private void nameStates() {

		List<State> knownStates = new ArrayList<State>();
		knownStates.add(start);
		start.name = guideBaseName;

		int stateNumber = graph.getVertexCount();
		int i = 0;
		while (knownStates.size() != stateNumber)
			for (State s : new ArrayList<State>(knownStates))
				for (TransitionInstance t : graph.getOutEdges(s) ){
					State target = graph.getOpposite(s, t);
					if (!knownStates.contains(target)) {
						// it's a new, unknown state from a known one.
						String stateName = generateName(i++);
						target.name = stateName;
						knownStates.add(target);
					}
				}
		end.name = "Out";
	}

	private String generateName(int i) {
		if (i>=0 && i< states.size())
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
				for (TransitionInstance trans : graph.getOutEdges(source) ){
					State target = graph.getOpposite(source, trans);
					List<Generic> types = new ArrayList<Generic>(source.generics);// clone the source
					types.addAll(trans.getPush());
					types.removeAll(trans.getPull());
					// types contains the generics target should have
					if (!knownStates.contains(target)) {
						// it's a new, unknown state from a known one.
						target.generics = types;
						knownStates.add(target);
					}
					else if (!	target.generics.equals(types) ) // check for states 
						throw new InconsistentTypePathException("State "+target.getName()+" has generic types "+ target.generics + " but from the transition "+trans.getAlias()+" it should have generic types "+ types );
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
		return getPackageName()+"."+getGuideName();
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

	// TODO improve state modelling: use path -> state instead.

	public void addStateName(String name) {
		this.states.add(name);
	}

	
	
	
	
}
