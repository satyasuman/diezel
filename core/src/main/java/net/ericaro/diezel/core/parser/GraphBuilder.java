package net.ericaro.diezel.core.parser;

import java.util.Deque;

/**
 * Diezel parser delegates almost any call to this class. It is in charge to build the graph, and then return a GuiderGenerator fully configured.
 * 
 * @author eric
 */
public class GraphBuilder implements DiezelSax<Graph> {

	Graph graph;

	/**
	 * to retrieve a graph after the first parsing
	 * 
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}

	// SAX IMPLEMENTATION FOLLOWS
	
	public void flow(Graph g) {
		g.reduce();
		g.unimplicit();
		this.graph = g;
	}

	public Graph bang(Deque<Graph> v) {
		return Graph.bang((Graph[]) v.toArray(new Graph[] {}));
	}

	public Graph opt(Graph v) {
		return Graph.opt(v);
	}

	public Graph plus(Graph v) {
		return Graph.iter_once(v);
	}

	public Graph sel(Graph v1, Graph v2) {
		return Graph.sel(v1, v2);
	}

	public Graph seq(Graph v1, Graph v2) {
		return Graph.seq(v1, v2);
	}

	public Graph star(Graph v) {
		return Graph.iter(v);
	}

	public Graph terminal(String name) {
		return Graph.term(name);
	}

}
