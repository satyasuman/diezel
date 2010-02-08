package net.ericaro.diezel.core.graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.FileUtil;

/**
 * Graph manages a graph of T (edge that "handle" a Transition object) and S
 * (nodes that handle a State Object).
 * 
 * It provides some basic operation, to make it possible to have a BNF
 * definition of a workflow. Handled object are moved during graph operations.
 * At the end we provide a way to generate
 * 
 * @author eric
 * 
 *@TODO need cleaning/commenting
 */
public class Graph<State,Transition> {
	static int ids = 0;

	public static class S<State,Transition> {
		int id;
		public State state;
		List<T<State,Transition>> ins = new ArrayList<T<State,Transition>>();
		public List<T<State,Transition>> outs = new ArrayList<T<State,Transition>>();

		public S() {
			id = ++ids;
		}

		public String toNode(String shape) {
			StringBuilder sb = new StringBuilder();
			sb.append("S").append(id).append("[shape=" + shape + "]");
			return sb.toString();
		}

		public String ref() {
			return "S" + id;
		}

		public String toString() {
			return ref();
		}

	}

	public static class T<State,Transition> {
		public S<State,Transition> in;
		public S<State,Transition> out;
		int id;
		public String name;
		public Transition transition;
		public boolean implicit = true;
		public String capture;
		public String[] types;

		public T() {
			id = ++ids;
		}

		public String toEdge() {
			StringBuilder sb = new StringBuilder();
			sb.append(in.ref()).append(" -> ").append(out.ref()).append("[")
					.append("label=\"").append(
							implicit ? "*"
									: (transition==null?name:transition) ).append("\"").append("]");// .;
			return sb.toString();
		}

		

		public String toString() {
			return toEdge();
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n");
		for (S<State,Transition> s : states) {
			String shape = "circle"; // "box" "circle"
			if (s == in || s == out)
				shape = "point";
			sb.append(s.toNode(shape)).append(";\n");
		}
		for (T<State,Transition> t : transitions)
			sb.append(t.toEdge()).append(";\n");
		sb.append("}\n");

		return sb.toString();
	}

	public Set<T<State,Transition>> transitions = new HashSet<T<State,Transition>>();
	public Set<S<State,Transition>> states = new HashSet<S<State,Transition>>();
	S<State,Transition> in;
	S<State,Transition> out;
	State state;
	int id;

	public Graph()
	{
		this.id = ++ids;
	}
	@Override
	public int hashCode() {
		return id;
	}


	public Graph<State,Transition> clone() {
		Graph<State,Transition> g = new Graph<State,Transition>();
		g.state = state;
		Map<S<State,Transition>, S<State,Transition>> old2new = new HashMap<S<State,Transition>, S<State,Transition>>();
		for (S<State,Transition> s : states)
			g.clone(s, old2new);
		for (T<State,Transition> t : transitions)
			g.clone(t, old2new);

		g.in = g.clone(in, old2new);
		g.out = g.clone(out, old2new);
		return g;
	}

	public S<State,Transition> newS() {
		S<State,Transition> s = new S<State,Transition>();
		states.add(s);
		return s;
	}

	public T<State,Transition> newT() {
		T<State,Transition> t = new T<State,Transition>();
		transitions.add(t);
		return t;
	}

	public T<State,Transition> connect(S<State,Transition> in, S<State,Transition> out) {
		T<State,Transition> t = newT();
		t.in = in;
		t.out = out;
		in.outs.add(t);
		out.ins.add(t);
		return t;
	}

	private S<State,Transition> clone(S<State,Transition> s, Map<S<State,Transition>, S<State,Transition>> old2new) {
		if (!old2new.containsKey(s)) {
			S<State,Transition> s2 = newS();
			old2new.put(s, s2);
			s2.state = s.state;
		}
		return old2new.get(s);

	}

	private T<State,Transition> clone(T<State,Transition> t, Map<S<State,Transition>, S<State,Transition>> old2new) {
		T<State,Transition> t2 = connect(clone(t.in, old2new), clone(t.out, old2new));
		t2.implicit = t.implicit;
		t2.transition = t.transition;
		t2.name= t.name;
		return t;
	}

	private void remove(S<State,Transition> s) {
		for (T<State,Transition> t : s.ins)
			if (t.out == s)
				t.out = null;
		for (T<State,Transition> t : s.outs)
			if (t.in == s)
				t.in = null;
		states.remove(s);
		if (in == s)
			in = null;
		if (out == s)
			out = null;
	}

	public void remove(T<State,Transition> t) {
		S<State,Transition> in = t.in;
		S<State,Transition> out = t.out;
		in.outs.remove(t);
		out.ins.remove(t);
		transitions.remove(t);
	}

	
	public void shortcut(T<State,Transition> t) {
		S<State,Transition> in = t.in;
		S<State,Transition> out = t.out;
		
		assert in.outs.size() == 1 || out.ins.size() == 1 : "cannot simplify graph using a Transition that is not a singleton";
		assert t.implicit : "cannot simplify graph using a Transition that is not implicit";
		// simple remove
		remove(t);

		if (in.outs.size() == 0) { // remove in
			out.ins.addAll(in.ins);// copy all incoming
			for (T<State,Transition> u : in.ins)
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
			for (T<State,Transition> u : out.outs)
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

	public void addAll(Graph<State,Transition> g) {
		states.addAll(g.states);
		transitions.addAll(g.transitions);
	}

	

	

	/**
	 * Remove all possible implicit links
	 * 
	 * @param <TransitionGenerator>
	 * @param <StateGenerator>
	 * @param term
	 * @param g
	 */
	public void reduce() {

		Set<T<State,Transition>> shortcutable = new HashSet<T<State,Transition>>();
		for (T<State,Transition> t : transitions)
			if (t.implicit && (t.in.outs.size() == 1 || t.out.ins.size() == 1 ))
				shortcutable.add(t);
		for (T<State,Transition> t : shortcutable)
			shortcut(t);
	}

	

	

	

	public static <State,Transition> Graph<State,Transition> opt(Graph<State,Transition> g) {
		// simply add a null link between in and out
		g.connect(g.in, g.out);
		return g;
	}

	public static <State,Transition> Graph<State,Transition> seq(Graph<State,Transition>... graphs) {
		if( graphs.length==1) return graphs[0];
		Graph<State,Transition> g = new Graph<State,Transition>();
		int last = graphs.length - 1;
		T<State,Transition>[] ts = new T[last];
		for (int i = 0; i < graphs.length; i++)
			g.addAll(graphs[i]);
		g.in = graphs[0].in;
		g.out = graphs[last].out;

		for (int i = 0; i < last; i++)
			ts[i] = g.connect(graphs[i].out, graphs[i + 1].in);
		/*
		for (int i = 0; i < last; i++)
			g.shortcut(ts[i]);/**/
		return g;
	}
	
	
	
	

	public static <State,Transition> Graph<State,Transition> iter(Graph<State,Transition> g) {
		// simply add a null link between in and out
		g.connect(g.in, g.out);		// removing this line forces a ()+
		g.connect(g.out, g.in);
		return g;
	}
	public static <State,Transition> Graph<State,Transition> iter_once(Graph<State,Transition> g) {
		// simply add a null link between in and out
		g.connect(g.out, g.in);
		return g;
	}

	public static <State,Transition> Graph<State,Transition> sel(Graph<State,Transition>... graphs) {
		if( graphs.length==1) return graphs[0];
		Graph<State,Transition> g = new Graph<State,Transition>( );
		g.in = g.newS();
		g.out = g.newS();
		for (int i = 0; i < graphs.length; i++) {
			g.addAll(graphs[i]);
			g.connect(g.in, graphs[i].in);
			g.connect(graphs[i].out, g.out);
		}
		return g;
	}
	
	/** A bang is explosive, use with care, propose a choice of graphs, and, then a choice of the same graphs minus the one just made.
	 * it means that it forces the user to pass through all the transitions of the bang, but in the order it wants. 
	 * 
	 * @param result
	 * @return
	 */
	public static <State,Transition> Graph<State,Transition> bang(Graph<State,Transition>... graphs) {
		if( graphs.length==1) return graphs[0];
		HashSet<Graph<State,Transition>> subgraph = new HashSet<Graph<State,Transition>>();
		subgraph.addAll(Arrays.asList(graphs));
		return bang(subgraph);
	}
	static <State,Transition> Graph<State,Transition> bang(Set<Graph<State,Transition>> graphs) {
		if (graphs.size() ==1) return graphs.iterator().next();
		System.out.println("bang "+ graphs.size());
		Graph<State,Transition> g = new Graph<State,Transition>( );
		g.in = g.newS();
		g.out = g.newS();
		for (Graph<State,Transition> graph : graphs) {
			//graph = graph.clone();
			HashSet<Graph<State,Transition>> subgraph = new HashSet<Graph<State,Transition>>();
			for (Graph<State,Transition> subs: graphs)
				if (subs.id!=graph.id)
					subgraph.add(subs.clone() );
			Graph<State,Transition> banged = bang(subgraph);
			g.addAll(graph);
			g.addAll(banged);
			g.connect(g.in, graph.in);			
			g.connect(graph.out, banged.in);
			g.connect(banged.out, g.out);
		}
		g.reduce();
		return g;
	}
	
	public static  <State,Transition> Graph<State,Transition> term(String capture, String name, String[] types) {
		System.out.println("new Term "+name);
		Graph<State,Transition> g = new Graph<State,Transition>( );
		g.in = g.newS();
		g.out = g.newS();
		T<State,Transition> t = g.connect(g.in, g.out);
		t.implicit = false;
		t.name= name;
		t.capture  =capture;
		t.types = types;
		return g;
	}
	
	
		  
	

	public Set<Transition> getTransitions() {
		Set<Transition> set = new HashSet<Transition>();
		for (T<State,Transition> t : transitions)
			set.add(t.transition);
		return set;
	}

	public Set<State> getStates() {
		Set<State> set = new HashSet<State>();
		for (S<State,Transition> s : states)
			set.add(s.state);
		return set;
	}
	
	public void graph(String name) throws IOException {
		File f = new File("./" + name + ".dot");
		FileUtil.printFile(f, toString(), true);
		Runtime.getRuntime().exec(
				"dot " + name + ".dot -Tpng -o " + name + ".png");
	}

	

}
