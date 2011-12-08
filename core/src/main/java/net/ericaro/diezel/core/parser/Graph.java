package net.ericaro.diezel.core.parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.builder.FileUtil;


/**
 * Graph manages a graph of T (edge that "handle" a Transition object) and S
 * (nodes that handle a State Object).
 * 
 * It provides some basic operation, to make it possible to have a BNF
 * definition of a workflow. Handled object are moved during graph operations.
 * 
 * @author eric
 * 
 */
public class Graph {
	static int ids = 0; // dead simple id generator

	/** S stands for "state", it's a node.
	 * 
	 * @author eric
	 *
	 */
	public static class S {
		public int id;
		List<T> ins = new ArrayList<T>();
		public List<T> outs = new ArrayList<T>();

		public S() {
			id = ++ids;
		}

		/** to dot graphiz protocol
		 * 
		 * @param shape
		 * @return
		 */
		public String toNode(String shape) {
			StringBuilder sb = new StringBuilder();
			sb.append("S").append(id).append("[shape=" + shape + "]");
			return sb.toString();
		}
		/** also part of the graphiz protocol: return the .dot name for this node
		 * 
		 * @return
		 */
		public String ref() {
			return "S" + id;
		}

		public String toString() {
			return ref();
		}
	}

	/** t stands for Transition, it's in fact an edge.
	 * 
	 * @author eric
	 *
	 */
	public static class T {
		int id; 
		public S in; // in state
		public S out; // out state
		public String name; // transition name
		public boolean implicit = true; // some transition are created "implicit".
		/* An implicit transition is a an artificial transition created during the graph creation process,
		 * because for some operations it's impossible to create the graph without "artifical", and transient edges.
		 * ther are intended to be "reduce" at the end.
		 * */

		public T() {
			id = ++ids; // dead simple unique id handler.
		}

		/** part of the graph viz protocol
		 * 
		 * @return
		 */
		public String toEdge() {
			StringBuilder sb = new StringBuilder();
			sb.append(in.ref()).append(" -> ").append(out.ref()).append("[")
					.append("label=\"").append(
							implicit ? "*"
									: name ).append("\"").append("]");// .;
			return sb.toString();
		}

		

		public String toString() {
			return toEdge();
		}
		
		/** copy all fields (but the id) of this into that
		 * 
		 * @param that
		 */
		public void copyTo(T that){
			that.name = name;
			that.implicit= implicit ;
			that.in = in;
			that.out = out;
		}
	}

	/**
	 *   print the current graph in a graphviz format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n");
		for (S s : states) {
			String shape = "circle"; // "box" "circle"
			if (s == in || s == out)
				shape = "point";
			sb.append(s.toNode(shape)).append(";\n");
		}
		for (T t : transitions)
			sb.append(t.toEdge()).append(";\n");
		sb.append("}\n");
		return sb.toString();
	}

	// a graph is a set of transitions, states, a start node, and an end node.
	public Set<T> transitions = new HashSet<T>();
	public Set<S> states = new HashSet<S>();
	public S in;
	public S out;
	int id;

	public Graph()
	{
		this.id = ++ids;
	}
	/** use the id as graph identifier
	 * 
	 */
	@Override
	public int hashCode() {
		return id;
	}

	
	/** clone this graph
	 * 
	 */
	public Graph clone() {
		Graph g = new Graph();
		Map<S, S> old2new = new HashMap<S, S>();
		for (S s : states)
			g.clone(s, old2new);
		for (T t : transitions)
			g.clone(t, old2new);

		g.in = g.clone(in, old2new);
		g.out = g.clone(out, old2new);
		return g;
	}

	/** create and append a node, unconnected, to this graph
	 * 
	 * @return
	 */
	public S newS() {
		S s = new S();
		states.add(s);
		return s;
	}

	/** create and append a transition, unconnected, to this graph
	 * 
	 * @return
	 */
	public T newT() {
		T t = new T();
		transitions.add(t);
		return t;
	}

	/** connect two nodes, and return the corresponding transition.
	 * 
	 * @param in
	 * @param out
	 * @return
	 */
	public T connect(S in, S out) {
		T t = newT();
		t.in = in;
		t.out = out;
		in.outs.add(t);
		out.ins.add(t);
		return t;
	}

	/** clone, or get the already clone node, in the old2new map.
	 * 
	 * @param s
	 * @param old2new
	 * @return
	 */
	private S clone(S s, Map<S, S> old2new) {
		if (!old2new.containsKey(s)) {
			S s2 = newS();
			old2new.put(s, s2);
		}
		return old2new.get(s);

	}

	/** clone, or get the already clone edge, in the old2new map.
	 * 
	 * @param t
	 * @param old2new
	 * @return
	 */
	private T clone(T t, Map<S, S> old2new) {
		T t2 = connect(clone(t.in, old2new), clone(t.out, old2new));
		t2.implicit = t.implicit;
		t2.name= t.name;
		return t;
	}

	/** remove a node, and disconnect (nullate) every transition linked to it.
	 * 
	 * @param s
	 */
	private void remove(S s) {
		for (T t : s.ins)
			if (t.out == s)
				t.out = null;
		for (T t : s.outs)
			if (t.in == s)
				t.in = null;
		states.remove(s);
		if (in == s)
			in = null;
		if (out == s)
			out = null;
	}

	/** remove an edge, and also remove it from the node it is attached to.
	 * 
	 * @param t
	 */
	public void remove(T t) {
		S in = t.in;
		S out = t.out;
		in.outs.remove(t);
		out.ins.remove(t);
		transitions.remove(t);
	}

	/** remove an implicit transition, so that the graph remain logically equivalent.
	 * 
	 * @param t
	 */
	public void shortcut(T t) {
		S in = t.in;
		S out = t.out;
		
		assert in.outs.size() == 1 || out.ins.size() == 1 : "cannot simplify graph using a Transition that is not a singleton";
		assert t.implicit : "cannot simplify graph using a Transition that is not implicit";
		// start removing the transition
		remove(t);
		
		// and then merge both states

		if (in.outs.size() == 0) { // remove in
			out.ins.addAll(in.ins);// copy all incoming
			for (T u : in.ins)
				u.out = out;// and move their target to out
			in.ins.clear();
			if (this.in == in)
				this.in = out;
			if (this.out == in)
				this.out = out;
			assert in.outs.size() == 0 && in.outs.size() == 0 : "Failed to shortcut, removable node is not empty after operation.";
			remove(in);
		} else {
			assert (out.ins.size() == 0) : "There is a problem with the extremity nodes.";
			in.outs.addAll(out.outs);// copy all outgoing
			for (T u : out.outs)
				// move transition
				u.in = in;// and move their source to in
			out.outs.clear();
			if (this.in == out)
				this.in = in;
			if (this.out == out)
				this.out = in;
			assert out.ins.size() == 0 && out.outs.size() == 0 : "Failed to shortcut, removable node is not empty after operation.";
			remove(out);
		}

	}

	
	/** append all states, and transitions from another graph. id are unique because the unique id generator is static.
	 * 
	 * @param g
	 */
	public void addAll(Graph g) {
		states.addAll(g.states);
		transitions.addAll(g.transitions);
	}

	/**
	 * Remove all possible implicit links
	 */
	public void reduce() {

		Set<T> shortcutable = new HashSet<T>();
		for (T t : transitions)
			if (t.implicit && (t.in.outs.size() == 1 || t.out.ins.size() == 1 ))
				shortcutable.add(t);
		for (T t : shortcutable)
			shortcut(t);
	}
	
	private T nextImplicit(){
		for(T t: transitions)
			if (t.implicit) return t;
		return null;
	}
	
	/** totally remove all implicit links, if it is not possible (like in reduce) then every transition are moved around to make it possible.
	 * 
	 */
	public void unimplicit(){
		T t= nextImplicit();
		while(t!=null){
			remove(t);
			S in = t.in;
			S out = t.out;
			if (in == out ){
				t=nextImplicit();
				continue;
			}
			
			// alg: I'm going to remove out: false
			// evry incoming to out will be transferred to in,
			// every outgoing will be transfered from in
			/*for(T incoming: out.ins)
				incoming.out=in;
			
			for(T outgoing: out.outs)
				outgoing.in=in;
			remove(out);
			*/
			//alg: simply copy the out out interface into the in out interface
			for(T outout: out.outs){
				T copied = newT();
				outout.copyTo(copied);
				// now changes the connections
				copied.in = in;
				in.outs.add(copied);
			}
			
			
			
			
			t= nextImplicit();
			//log(this);
		}
	}

	

	
	//static int debugCount=0;
	public static void log(Graph g){
		//try {	g.graph("target/debug"+ ++debugCount);		} catch (IOException e) {}
	}
	
// STARTS THE TOP OPERATORS
	
	/** simply add a null link between in and out, so that the whole graph is made "optional"
	 * 
	 * @param g
	 * @return
	 */
	public static  Graph opt(Graph g) {
		// simply add a null link between in and out
		g.connect(g.in, g.out);
		log(g);
		return g;
	}

	/** sequentially connect graphs togethers
	 * 
	 * @param graphs
	 * @return
	 */
	public static  Graph seq(Graph... graphs) {
		if( graphs.length==1) return graphs[0]; // smart shortcut
		
		Graph g = new Graph(); // the resulting graph
		
		int last = graphs.length - 1;
		T[] ts = new T[last];
		for (int i = 0; i < graphs.length; i++)
			g.addAll(graphs[i]);
		g.in = graphs[0].in;
		g.out = graphs[last].out;

		for (int i = 0; i < last; i++)
			ts[i] = g.connect(graphs[i].out, graphs[i + 1].in);
		log(g);
		return g;
	}
	
	
	
	

	/**
	 * simply add a null link between out to in making it repeatable, and a link, just like opt, making it avoidable
	 * @param g
	 * @return
	 */
	public static  Graph iter(Graph g) {
		// simply add a null link between in and out
		g.connect(g.in, g.out);		// removing this line forces a ()+
		g.connect(g.out, g.in);
		log(g);
		return g;
	}
	/** simply add a null link between out to in making it repeatable, 
	 * 
	 * @param g
	 * @return
	 */
	public static  Graph iter_once(Graph g) {
		// simply add a null link between in and out
		g.connect(g.out, g.in);
		log(g);
		return g;
	}

	/** connect graphs in parallel
	 * 
	 * @param graphs
	 * @return
	 */
	public static  Graph sel(Graph... graphs) {
		if( graphs.length==1) return graphs[0];
		Graph g = new Graph( );
		g.in = g.newS();
		g.out = g.newS();
		for (int i = 0; i < graphs.length; i++) {
			g.addAll(graphs[i]);
			g.connect(g.in, graphs[i].in);
			g.connect(graphs[i].out, g.out);
		}
		log(g);
		return g;
	}
	
	/** A bang is explosive, use with care, propose a choice of graphs, and, then a choice of the same graphs minus the one just made.
	 * it means that it forces the user to pass through all the transitions of the bang, but in the order it wants. 
	 * 
	 * @param result
	 * @return
	 */
	public static  Graph bang(Graph... graphs) {
		if( graphs.length==1) return graphs[0];
		HashSet<Graph> subgraph = new HashSet<Graph>();
		subgraph.addAll(Arrays.asList(graphs));
		return bang(subgraph);
	}
	/** A bang is explosive, use with care, propose a choice of graphs, and, then a choice of the same graphs minus the one just made.
	 * it means that it forces the user to pass through all the transitions of the bang, but in the order it wants. 
	 * 
	 * @param graphs
	 * @return
	 */
	static  Graph bang(Set<Graph> graphs) {
		if (graphs.size() ==1) return graphs.iterator().next();
		System.out.println("bang "+ graphs.size());
		Graph g = new Graph( );
		g.in = g.newS();
		g.out = g.newS();
		for (Graph graph : graphs) {
			//graph = graph.clone();
			HashSet<Graph> subgraph = new HashSet<Graph>();
			for (Graph subs: graphs)
				if (subs.id!=graph.id)
					subgraph.add(subs.clone() );
			Graph banged = bang(subgraph);
			g.addAll(graph);
			g.addAll(banged);
			g.connect(g.in, graph.in);			
			g.connect(graph.out, banged.in);
			g.connect(banged.out, g.out);
		}
		g.reduce();
		log(g);
		return g;
	}
	
	/** creates a new graph, with a single transition, named "name"
	 * 
	 */
	public static   Graph term(String name) {
		Graph g = new Graph( );
		g.in = g.newS();
		g.out = g.newS();
		T t = g.connect(g.in, g.out);
		t.implicit = false;
		t.name= name;
		log(g);
		return g;
	}
	
	public void graph(String name) throws IOException {
		File f = new File("./" + name + ".dot");
		FileUtil.printFile(f, toString(), true);
		dot(name);
	}
	public static void dot(String name) throws IOException {
		PrintStream pout = System.out;
		try {
			Process r = Runtime.getRuntime().exec(
					"dot " + name + ".dot -Grankdir=LR -Tpng -o " + name + ".png");
			System.setOut(new PrintStream(r.getOutputStream()));
			r.waitFor();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			System.setOut(pout);
		}
	}

	

}
