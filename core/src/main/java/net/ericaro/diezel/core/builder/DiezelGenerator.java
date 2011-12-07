package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	String workflow; // the regexp defining the workfow
	Set<Generic> rootTypes = new HashSet<Generic>(); // the root state generics, always usefull to start with
	String packageName; // global conf: the target package name
	Set<Transition> transitions =new HashSet<Transition>();
	String guideBaseName = "Guide";

	String header = " _________________________________\n" + " ))                              (( \n" + "((   __    o     ___        _     ))\n" + " ))  ))\\   _   __  ))   __  ))   (( \n" + "((  ((_/  ((  ((- ((__ ((- ((     ))\n" + " ))        )) ((__     ((__ ))__  (( \n" + "((                                ))\n" + " ))______________________________(( \n" + "Diezel 2.0.0 Generated.\n";;

	transient Map<String, Transition> index; // map to find transition based on their name
	transient Map<Integer, State> stateTypes;
	transient Graph graph; // graph computed from the workflow expression
	transient STGroup templates = new STGroupFile("net/ericaro/diezel/stl/interface.stg");
	
	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getGuideBaseName() {
		return guideBaseName;
	}

	public void setGuideBaseName(String guideBaseName) {
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

	public boolean addAllRootType(Collection<? extends Generic> c) {
		return rootTypes.addAll(c);
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

	void buildGraph() throws ParseException {
		GraphBuilder db = new GraphBuilder();
		RegExp.parse(workflow, db);
		this.graph = db.getGraph();

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
	
	private void nameStates(){
		
		List<S> knownStates = new ArrayList<S>();
		knownStates.add(graph.in);
		State root = stateTypes.get(graph.in.id);
		root.setName(guideBaseName); 
		
		int stateNumber = graph.states.size();
		int i=0;
		while (knownStates.size() != stateNumber)
			for (S s : new ArrayList<S>(knownStates))
				for (T t : s.outs)
					if (!knownStates.contains(t.out)) {
						// it's a new, unknown state from a known one.
						String stateName = guideBaseName + ++i;
						State target = stateTypes.get(t.out.id);
						target.setName(stateName);
						knownStates.add(t.out);
					}
		
		
		State out= stateTypes.get(graph.out.id);
		out.setName("Out");
		
	}

	private void solveStates() {
		// fill the root state with the default states
		stateTypes.get(graph.in.id).generics.addAll(rootTypes);
		// compute every state generics
		Set<S> knownStates = new HashSet<S>();
		knownStates.add(graph.in);
		
		int stateNumber = graph.states.size();
		while (knownStates.size() != stateNumber)
			for (S s : new HashSet<S>(knownStates))
				for (T t : s.outs)
					if (!knownStates.contains(t.out)) {
						// it's a new, unknown state from a known one.
						Transition trans = index.get(t.name);
						State source = stateTypes.get(s.id);
						State target = stateTypes.get(t.out.id);
						Set<Generic> types = new HashSet<Generic>(source.generics);// clone the source
						types.addAll(trans.generics);
						types.removeAll(trans.lessTypes);
						target.generics = types;
						knownStates.add(t.out);
					}

	}

	public void generate(File targetDirectory) throws ParseException, IOException {
		buildGraph(); // parses the workflow, build a graph from it, associated the State and Transition objects
		solveStates(); // solve generics for every state
		nameStates();

		
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
				transitions.add(trans.clone(toState));
				
			}
			fromState.setTransitions(transitions);
			FileUtil.toDir(targetDirectory, packageName, fromState.getName()+".java", compile(fromState));
			
		}
		FileUtil.toDir(targetDirectory, packageName, "guide-graph.dot", graph.toString());
	}
	
	
	
	public String compile(State state ) {
		
		ST compileUnit = templates.getInstanceOf("compileUnit");
		compileUnit.add("generator", this);
		compileUnit.add("state", state);
		String result = compileUnit.render() ;
//		System.out.println("-------------------------------------------");
//		System.out.println(result);
		return result;
	}

}
