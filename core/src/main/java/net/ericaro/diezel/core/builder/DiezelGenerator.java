package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.InconsistentTypePathException;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.Graph.S;
import net.ericaro.diezel.core.parser.Graph.T;
import net.ericaro.diezel.core.parser.GraphBuilder;
import net.ericaro.diezel.core.parser.ParseException;
import net.ericaro.diezel.core.parser.RegExp;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class DiezelGenerator {

	String expression; // the regexp defining the workfow
	List<Generic> rootTypes = new ArrayList<Generic>(); // the root state generics, always usefull to start with
	String packageName; // global conf: the target package name
	Set<Transition> transitions = new HashSet<Transition>();
	String guideBaseName = "Guide";
	private List<String> states = new ArrayList<String>();

	String header = " _________________________________\n" + " ))                              (( \n" + "((   __    o     ___        _     ))\n" + " ))  ))\\   _   __  ))   __  ))   (( \n" + "((  ((_/  ((  ((- ((__ ((- ((     ))\n" + " ))        )) ((__     ((__ ))__  (( \n" + "((                                ))\n" + " ))______________________________(( \n" + "Diezel 2.0.0 Generated.\n";;

	transient Map<String, Transition> index; // map to find transition based on their name
	transient Map<Integer, State> stateTypes;
	transient Graph graph; // graph computed from the expression expression
	transient STGroup templates = new STGroupFile("net/ericaro/diezel/core/builder/interface.stg");

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getGuideName() {
		return guideBaseName;
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

	public boolean containsRootType(String o) {
		return rootTypes.contains(o);
	}

	public boolean addTransition(Transition e) {
		return transitions.add(e);
	}

	public boolean addAllTransition(Collection<? extends Transition> c) {
		return transitions.addAll(c);
	}

	public boolean containsTransition(Object o) {
		return transitions.contains(o);
	}

	public List<String> getStateNames() {
		return states;
	}

	public void addStateName(String name) {
		this.states.add(name);
	}

	void buildGraph() throws ParseException {

		this.graph = GraphBuilder.build(expression);

		// build a faster index for transition by name
		index = new HashMap<String, Transition>();
		for (Transition t : transitions)
			index.put(t.alias, t);

		// build an index S.id -> Type for transition generation
		stateTypes = new HashMap<Integer, State>();
		int i = 0;
		for (S s : graph.states) {
			State state = new State();
			if (s.equals(graph.out))
				state.isOutput = true;
			stateTypes.put(s.id, state);
		}

	}

	private void nameStates() {

		List<S> knownStates = new ArrayList<S>();
		knownStates.add(graph.in);
		State root = stateTypes.get(graph.in.id);
		root.setName(guideBaseName);

		int stateNumber = graph.states.size();
		int i = 0;
		while (knownStates.size() != stateNumber)
			for (S s : new ArrayList<S>(knownStates))
				for (T t : s.outs)
					if (!knownStates.contains(t.out)) {
						// it's a new, unknown state from a known one.
						String stateName = generateName(i++);
						State target = stateTypes.get(t.out.id);
						target.setName(stateName);
						knownStates.add(t.out);
					}

		State out = stateTypes.get(graph.out.id);
		out.setName("Out");

	}

	public String generateName(int i) {
		if (i>=0 && i< states.size())
			return states.get(i);
		return guideBaseName + i;
	}
	
	private void solveStates() throws InconsistentTypePathException {
		// fill the root state with the default states
		stateTypes.get(graph.in.id).generics.addAll(rootTypes);
		// compute every state generics
		Set<S> knownStates = new HashSet<S>();
		knownStates.add(graph.in);

		int stateNumber = graph.states.size();
		while (knownStates.size() != stateNumber)
			for (S s : new HashSet<S>(knownStates))
				for (T t : s.outs){
					
					// computes the next state types
					State source = stateTypes.get(s.id);
					Transition trans = index.get(t.name);
					List<Generic> types = new ArrayList<Generic>(source.generics);// clone the source
					types.addAll(trans.generics);
					types.removeAll(trans.lessTypes);
					State target = stateTypes.get(t.out.id);
					
					if (!knownStates.contains(t.out)) {
						// it's a new, unknown state from a known one.
						target.generics = types;
						knownStates.add(t.out);
					}
					else if (!	target.generics.equals(types) ) // check for states 
						throw new InconsistentTypePathException("State "+target.getName()+" has generic types "+ target.generics + " but from the transition "+trans.getAlias()+" it should have generic types "+ types );
				}
	}

	

	public void generate(File targetDirectory) throws ParseException, IOException, InconsistentTypePathException {
		buildGraph(); // parses the expression, build a graph from it, associated the State and Transition objects
		nameStates();
		solveStates(); // solve generics for every state

		// pars the graph from states
		for (S s : graph.states) {
			State fromState = stateTypes.get(s.id);
			if (s == graph.out)
				continue;
			List<Transition> transitions = new ArrayList<Transition>();
			// delegate to the state the job to declare the type
			// append transitions
			for (T t : s.outs) {
				State toState = stateTypes.get(t.out.id);
				Transition trans = index.get(t.name);
				transitions.add(trans.clone(fromState, toState));

			}
			fromState.setTransitions(transitions);
			FileUtil.toDir(targetDirectory, packageName, fromState.getName() + ".java", compile(fromState));

		}
		FileUtil.toDir(targetDirectory, packageName, "guide-graph.dot", toString());
	}

	public String compile(State state) {

		ST compileUnit = templates.getInstanceOf("compileUnit");
		compileUnit.add("generator", this);
		compileUnit.add("state", state);
		String result = compileUnit.render();
		// System.out.println("-------------------------------------------");
		// System.out.println(result);
		return result;
	}

	
	/**
	 *   print the current graph in a graphviz format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n");
		for (State s : stateTypes.values()) 
			sb.append(s.toString()).append(";\n");
		
		for (State s : stateTypes.values()) 
			for (Transition  t: s.getTransitions())
				sb.append(t.toString()).append(";\n");
		sb.append("}\n");
		return sb.toString();
	}
	
}
