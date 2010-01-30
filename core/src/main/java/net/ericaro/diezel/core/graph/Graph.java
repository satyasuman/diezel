package net.ericaro.diezel.core.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.State;
import net.ericaro.diezel.core.Transition;

/**
 * @author Eric.Atienza
 * 
 */
public class Graph {
	static int ids = 0;

	public static class S {
		int id;
		State state;
		List<T> ins = new ArrayList<T>();
		List<T> outs = new ArrayList<T>();

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

	}

	public static class T {
		S in;
		S out;
		int id;
		State symbolic = null;
		Transition transition;
		boolean implicit = true;

		public T() {
			id = ++ids;
		}

		public String toEdge() {
			StringBuilder sb = new StringBuilder();
			sb.append(in.ref()).append(" -> ").append(out.ref()).append("[")
					.append("label=\"").append(
							implicit ? (symbolic != null ? "!" : "*")
									: transition).append("\"").append("]");// .;
			return sb.toString();
		}

		public boolean isTerminal() {
			return symbolic == null;
		}
	}

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

	Set<T> transitions = new HashSet<T>();
	Set<S> states = new HashSet<S>();
	S in;
	S out;
	State state;

	public Graph clone() {
		Graph g = new Graph();
		g.state = state;
		Map<S, S> old2new = new HashMap<S, S>();
		for (S s : states)
			g.clone(s, old2new);
		for (T t : transitions)
			g.clone(t, old2new);

		g.in = g.clone(in, old2new);
		g.out = g.clone(out, old2new);
		return g;
	}

	public S newS() {
		S s = new S();
		states.add(s);
		return s;
	}

	public T newT() {
		T t = new T();
		transitions.add(t);
		return t;
	}

	public T connect(S in,
			S out) {
		T t = newT();
		t.in = in;
		t.out = out;
		in.outs.add(t);
		out.ins.add(t);
		return t;
	}

	private S clone(S s,
			Map<S, S> old2new) {
		if (!old2new.containsKey(s)) {
			S s2 = newS();
			old2new.put(s, s2);
			s2.state = s.state;
		}
		return old2new.get(s);

	}

	private T clone(T t,
			Map<S, S> old2new) {
		T t2 = connect(clone(t.in, old2new), clone(t.out,
				old2new));
		t2.implicit = t.implicit;
		t2.symbolic = t.symbolic;
		t2.transition = t.transition;
		return t;
	}

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

	public void remove(T t) {
		S in = t.in;
		S out = t.out;
		in.outs.remove(t);
		out.ins.remove(t);
		transitions.remove(t);
	}

	public void shortcut(T t) {
		S in = t.in;
		S out = t.out;

		assert in.outs.size() == 1 || out.ins.size() == 1 : "cannot simplify graph using a Transition that is not a singleton";
		assert t.implicit : "cannot simplify graph using a Transition that is not implicit";
		// simple remove
		remove(t);

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
			for (T u : out.outs)// move transition
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

	public void addAll(Graph g) {
		states.addAll(g.states);
		transitions.addAll(g.transitions);
	}

	/**
	 * Substitute all symbolic links. assume that the dependency (through links)
	 * HAS NO LOOP. This will only generate a static grammar. it's hard for ebnf
	 * to do otherwise. Try splitting you graph into several static ebnf.
	 * 
	 * @param graphs
	 * @return
	 */
	public static  boolean substituteAll(
			Graph... graphs) {

		// splits the graphs into three sets.
		// the one with terminal elements not substituted: non_subs
		// the one with terminal element substituted : subs
		// the one with non terminal element : non_terms
		/*
		 * ALG : the alg is straigforward while non_subs is not empty:
		 * term<-poll a non substituted element for non_term in non_terms:
		 * subs(term, non_term) if non_term is terminal: mv non_term from
		 * non_terms to non_subs
		 * 
		 * non_terms should be empty. if not the case, it's impossible to
		 * generate a "static" grammar.
		 */
		Set<Graph> non_terms = new HashSet<Graph>();
		Deque<Graph> non_subs = new ArrayDeque<Graph>();
		Set<Graph> subs = new HashSet<Graph>();

		// fill the sets
		for (Graph g : graphs)
			if (g.isTerminal())
				non_subs.add(g);
			else
				non_terms.add(g);

		while (!non_subs.isEmpty()) {
			Graph term = non_subs.poll();
			subs.add(term);
			for (Graph non_term : non_terms) {
				substitute(term, non_term);
				if (term.isTerminal()) {
					non_terms.remove(non_term);
					non_subs.push(non_term);
				}
			}
		}
		// no check is done with non-terms to be empty, because we "hope" that
		// we still can make something out of it
		return non_terms.isEmpty();

	}

	/**
	 * Remove all possible implicit links
	 * 
	 * @param <Transition>
	 * @param <State>
	 * @param term
	 * @param g
	 */
	public static  void reduce(Graph g) {

		Set<T> shortcutable = new HashSet<T>();
		for (T t : g.transitions)
			if (t.implicit && (t.in.outs.size() == 1 || t.out.ins.size() == 1))
				shortcutable.add(t);
		for (T t : shortcutable)
			g.shortcut(t);
	}

	private static  void substitute(
			Graph term, Graph g) {
		/*
		 * ALG for each symbolic transition that match the name: substitute a
		 * graph to a T
		 */
		Set<T> symb_trans = new HashSet<T>();
		for (T t : g.transitions)
			if (term.state.equals(t.symbolic))
				symb_trans.add(t);
		for (T t : symb_trans)
			substitute(term, g, t);

	}

	private static  void substitute(
			Graph term, Graph g,
			T t) {
		/*
		 * ALG
		 */
		g.remove(t);
		term = term.clone();
		g.addAll(term);
		T ts1 = g.connect(t.in, term.in);
		T ts2 = g.connect(term.out, t.out);
		g.shortcut(ts2);
		g.shortcut(ts1);

	}

	private boolean isTerminal() {
		for (T t : transitions)
			if (!t.isTerminal())
				return false;
		return true;
	}

	public static class BNF {

		Map<State, Graph> definitions = new HashMap<State, Graph>();

		// bnfop
		public Graph opt(Graph g) {
			// simply add a null link between in and out
			g.connect(g.in, g.out);
			return g;
		}

		public Graph seq(Graph... graphs) {
			Graph g = new Graph();
			int last = graphs.length - 1;
			T[] ts = new T[last];
			for (int i = 0; i < graphs.length; i++)
				g.addAll(graphs[i]);
			g.in = graphs[0].in;
			g.out = graphs[last].out;

			for (int i = 0; i < last; i++)
				ts[i] = g.connect(graphs[i].out, graphs[i + 1].in);

			for (int i = 0; i < last; i++)
				g.shortcut(ts[i]);
			return g;
		}

		public Graph iter(Graph g) {
			// simply add a null link between in and out
			g.connect(g.in, g.out);
			g.connect(g.out, g.in); // removing this line forces a ()+
			return g;
		}

		public Graph sel(Graph... graphs) {
			Graph g = new Graph();
			g.in = g.newS();
			g.out = g.newS();
			for (int i = 0; i < graphs.length; i++) {
				g.addAll(graphs[i]);
				g.connect(g.in, graphs[i].in);
				g.connect(graphs[i].out, g.out);
			}
			return g;
		}

		public Graph ch(Transition transition) {
			Graph g = new Graph();
			g.in = g.newS();
			g.out = g.newS();
			T t = g.connect(g.in, g.out);
			t.transition = transition;
			t.implicit = false;
			return g;
		}

		public Graph lhs(State state) {
			if (!definitions.containsKey(state)) {
				// creates an empty graph with a symbolic name
				Graph g = new Graph();
				definitions.put(state, g);

				g.in = g.newS();
				g.out = g.newS();
				T t = g.connect(g.in, g.out);
				t.symbolic = state;
			}
			return definitions.get(state).clone();

		}

		public BNF rule(State state,
				Graph graph) {
			definitions.put(state, graph);
			graph.state = state;
			return this;
		}

	}

	
}
