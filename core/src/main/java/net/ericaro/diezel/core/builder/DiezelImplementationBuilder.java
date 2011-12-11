package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;



/** A Diezel Implementation provides implementations code for a Diezel Language
 * 
 * @author eric
 *
 */
public class DiezelImplementationBuilder implements DiezelBuilder<DiezelImplementation> {

	
	
	DiezelLanguage language;
	String packageName;
	List<TransitionImplementation> transitions = new ArrayList<TransitionImplementation>();
	DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance> graph;
	private String guideName;
	private String languageName;
	private StateImplementation start;
	

	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public void setImplements(String str) {
		this.languageName = str;
	}

	public void setGuideName(String str) {
		this.guideName = str;
		
	}
	
	
	public void addTransitionImplementation(TransitionImplementation impl){
		this.transitions.add(impl);
	}
	
	
	public String getImplements() {
		return languageName;
	}
	
	public void setLanguage(DiezelLanguage language) {
		this.language = language;
	}
	public DiezelImplementation build(){
		findParent();
		
		buildGraph();
		DiezelImplementation impl = new DiezelImplementation();
		impl.language = language;
		impl.graph = Graphs.unmodifiableGraph(graph);
		impl.transitions = Collections.unmodifiableList(transitions);
		impl.packageName = packageName;
		impl.start = start;
		return impl;
	}
	




	private void findParent() {
		// TODO Auto-generated method stub
		
	}
	/** creates a derivative graph
	 * 
	 */
	private void buildGraph(){
		
		DirectedGraph<State, TransitionInstance> pg = language.getGraph();
		graph = new DirectedSparseMultigraph<StateImplementation, TransitionImplementationInstance>(); // the new graph
		
		// build an alias map
		Map<String, TransitionImplementation> transitionMap = new HashMap<String, TransitionImplementation>();
		for (TransitionImplementation ti : transitions) 
			transitionMap.put(ti.getAlias(), ti);

		// build a map for st to st impl, and add st impl into the graph
		Map<State, StateImplementation> stateMap = new HashMap<State, StateImplementation>();
		for( State st: pg.getVertices()){
			StateImplementation impl = new StateImplementation(graph, st);
			stateMap.put(st, impl);
			graph.addVertex(impl);
			if (st == language.start)
				start = impl;
			
			
		}
			
		for( TransitionInstance ti: pg.getEdges()){
			
			// get the three data : edge, src, and dest edges
			TransitionImplementation impl = transitionMap.get(ti.getAlias() );
			State src = pg.getSource(ti);
			State dest = pg.getDest(ti);
			// add a new edge
			StateImplementation srcImpl =stateMap.get(src); 
			StateImplementation destImpl = stateMap.get(dest);
			graph.addEdge(new TransitionImplementationInstance(graph, impl, ti), srcImpl, destImpl );
			
		}
	}
	




	
	
}
