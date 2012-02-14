package net.ericaro.diezel.core.parser;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import net.ericaro.diezel.core.Diezel;
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
public class GraphBuilderTest  extends TestCase {
	
	public void testIssue13() throws IOException, ParseException {
		String issue = "issue13" ;
		File target = new File("./target/test/"+issue);
		
		
		Graph g = GraphBuilder.build("(a)+, c");
		File src = new File("./src/test/resources/");
		g.graph( new File(target, issue ).getPath());
		
		
		for( T t: g.in.outs)
			assert t.out != g.out: "in that case, we do not expect out to be directly achievable from in";
		
		
	}
	
}
