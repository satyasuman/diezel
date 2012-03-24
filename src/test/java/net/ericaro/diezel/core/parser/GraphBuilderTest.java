package net.ericaro.diezel.core.parser;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.builder.DiezelLanguage;
import net.ericaro.diezel.core.builder.FileUtils;
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
	
	
	/** testing issue 13
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void testIssue13() throws IOException, ParseException {
		String issue = "issue13" ;
		File target = new File("./target/test/"+issue);
		
		
		Graph g = GraphBuilder.build("(a)+, c");
		File src = new File("./src/test/resources/");
		g.graph( new File(target, issue ).getPath());
		
		
		for( T t: g.in.outs)
			assert t.out != g.out: "in that case, we do not expect out to be directly achievable from in";
	}
	
	public void testIssue12() throws DiezelException, IOException {
		// start with a simple case
		String issue = "issue12" ;
		File target = new File("./target/test/"+issue);
		File src = new File("./src/test/resources/");
		
		//Graph.DEBUG = true;
		Diezel.generate(target, new File(src, issue+".xml"), new File(src, issue+"-impl.xml"));
		
		File res = new File(target, "net/ericaro/diezel/MyDemoBuilder.java");
		String result = org.codehaus.plexus.util.FileUtils.fileRead(res);
		System.out.println(result);
		Matcher m = Pattern.compile("transition_a.*?\\{(.*?)\\}", Pattern.MULTILINE+Pattern.DOTALL).matcher(result);
		assert m.find() : "cannot find body" ;
		String body = m.group(1)  ;
		body = canonify(body);
		String expectedBody = canonify("\n" + 
				"			System.out.println(\"a\");\n" + 
				"			System.out.println(\"a\");\n" + 
				"			System.out.println(\"a\");\n" + 
				"			System.out.println(\"a\");\n" + 
				"			" );
		
		System.out.println(body);
		System.out.println(expectedBody);
		
		assert expectedBody.equals(body) : "expected body did not match the real one";
		
		//Graph.dot(new File(target, "org/apache/s4/core/guide-graph").getPath());
		
	}

	private String canonify(String body) {
		body =body.replaceAll("\n", " ");
		body =body.replaceAll("\t", " ");
		body =body.replaceAll(" +", " ");
                                                                                             		return body.trim();
	}
	
}
