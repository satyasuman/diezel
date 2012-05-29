package net.ericaro.diezel.builder.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ericaro.diezel.builder.lang.DiezelLanguage;
import net.ericaro.diezel.builder.lang.State;
import net.ericaro.diezel.builder.lang.TransitionInstance;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;

/**
 * A Diezel Implementation provides implementations code for a Diezel Language
 * 
 * @author eric
 * 
 */
public class DiezelImplementationBuilder {

	DiezelLanguage																			language;
	private DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance>	graph;
	private net.ericaro.diezel._2_0.DiezelImplementation									src;
	private List<TransitionImplementation>													transitions	= new ArrayList<TransitionImplementation>();
	private StateImplementation																start;

	public DiezelImplementationBuilder(net.ericaro.diezel._2_0.DiezelImplementation src) {
		this.src = src;
	}

	public void setLanguage(DiezelLanguage language) {
		this.language = language;
	}

	public DiezelImplementation build() {

		buildGraph();
		DiezelImplementation impl = new DiezelImplementation();
		impl.language = language;
		impl.graph = Graphs.unmodifiableGraph(graph);
		impl.transitions = Collections.unmodifiableList(transitions);
		impl.packageName = src.getPackage();
		impl.start = start;
		impl.extendClass = src.getExtends();
		impl.guideName = src.getName();
		return impl;
	}

	/**
	 * creates a derivative graph
	 * 
	 */
	private void buildGraph() {

		for (net.ericaro.diezel._2_0.TransitionImplementation ti : src.getTransitions().getTransitionImplementation())
			transitions.add(buildTransition(ti));

		DirectedGraph<State, TransitionInstance> pg = language.getGraph();
		graph = new DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance>(); // the new graph

		// build an alias map
		Map<String, TransitionImplementation> transitionMap = new HashMap<String, TransitionImplementation>();
		for (TransitionImplementation ti : transitions)
			transitionMap.put(ti.getAlias(), ti);

		// build a map for st to st impl, and add st impl into the graph
		Map<State, StateImplementation> stateMap = new HashMap<State, StateImplementation>();
		for (State st : pg.getVertices()) {
			StateImplementation impl = new StateImplementation(st, src.getName());
			stateMap.put(st, impl);
			graph.addVertex(impl);
			if (st == language.getStartState())
				start = impl;
		}

		for (TransitionInstance ti : pg.getEdges()) {

			// get the three data : edge, src, and dest edges
			TransitionImplementation impl = transitionMap.get(ti.getAlias());
			State src = pg.getSource(ti);
			State dest = pg.getDest(ti);
			// add a new edge
			StateImplementation srcImpl = stateMap.get(src);
			StateImplementation destImpl = stateMap.get(dest);
			graph.addEdge(new TransitionImplementationInstance(impl, ti), srcImpl, destImpl);
		}

		// connect the graph
		// now loop the graph to connect transitions to their child
		for (TransitionImplementationInstance t : graph.getEdges()) {
			StateImplementation s = graph.getSource(t);
			StateImplementation dest = graph.getDest(t);
			s.getTransitions().add(t);
			t.setNextState(dest);
		}
	}

	private TransitionImplementation buildTransition(net.ericaro.diezel._2_0.TransitionImplementation ti) {
		TransitionImplementation t = new TransitionImplementation(ti.getName());
		t.setBody(ti.getBody());
		return t;
	}

	public Object getImplements() {
		return src.getImplements();
	}

}
