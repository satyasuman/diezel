package net.ericaro.diezel.core.parser;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import net.ericaro.diezel.core.builder.DiezelLanguage;
import net.ericaro.diezel.core.builder.State;
import net.ericaro.diezel.core.builder.Transition;
import net.ericaro.diezel.core.builder.TransitionInstance;
import net.ericaro.diezel.core.parser.Graph.S;
import net.ericaro.diezel.core.parser.Graph.T;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;

/**
 * Diezel parser delegates almost any call to this class. It is in charge to build the graph, and then return a GuiderGenerator fully configured.
 * 
 * @author eric
 */
public class GraphBuilder implements DiezelSax<Graph> {


	private Graph graph;
	
	/**
	 * build a raw graph based on a single expression
	 * 
	 * @param expression
	 * @return
	 * @throws ParseException
	 */
	public static Graph build(String expression) throws ParseException {
		GraphBuilder db = new GraphBuilder();
		RegExp.parse(expression, db);
		return db.getGraph();
	}

	
	
	/** utility to generate an expression into a dot file.
	 * @param expression
	 * @param name
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void toFile(String expression, String name) throws ParseException, IOException {
		GraphBuilder db = new GraphBuilder();
		RegExp.parse(expression, db);
		Graph g = db.getGraph();
		g.graph(name);
	}


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
		g.unDuplicate() ;
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