package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ericaro.diezel.core.gen.CompilationUnitGen;
import net.ericaro.diezel.core.gen.ConstructorGen;
import net.ericaro.diezel.core.gen.FieldGen;
import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.Graph.S;
import net.ericaro.diezel.core.graph.Graph.T;

public class FlowManager {

	private Target target;
	private List<State> states = new LinkedList<State>();

	
	public FlowManager(Graph<State,Transition> g){
		// gen parser
		g.reduce();
		finalize(g); //prepare the graph, tune transitions and states
		
		Set<State> states = g.getStates();
		Set<Transition> transitions = g.getTransitions();
		states(states);
	} 
	
	
	private void finalize(Graph<State,Transition> g){
			// name states
			int i = 0;
			for (S<State,Transition> s : g.states) {
				if (s.state == null)
					s.state = new State().name("S" + ++i);

			}
			for (T<State,Transition> t : g.transitions) {
				if (t.transition== null)
					t.transition= new Transition().name( t.name);
				
			}
			// gen all interfaces
			for (S<State,Transition> s : g.states) {
				for (T<State,Transition> t : s.outs) {
					//TODO recursively follow symbolic t, and append all transitions.
					appendAll(s, t, new HashSet<T<State,Transition>>());
				}

			}
	}


	private void appendAll(S<State, Transition> s, T<State, Transition> t, Set<T<State, Transition>> followed) {
		if (!t.implicit){
		s.state.transgen(t.transition);
		t.transition.returnState(t.out.state);
		}
		else if (!followed.contains(t)){
			followed.add(t); //mark as visited
			for(T<State, Transition> next: t.out.outs )
				appendAll(s, next, followed);
		}
	}
	
	public FlowManager target(Target target) {
		this.target = target;
		return this;
	}

	public FlowManager states(Set<State> states) {
		this.states.addAll(states);
		return this;
	}

	public FlowManager states(State... states) {
		this.states.addAll(Arrays.asList(states));
		return this;
	}

	
	

	private String header = "/*\n"+DiezelHost.HEADER+"\n*/";
	private String packageName;
	private String name;
	private String targetType;
	private String targetName = "host";
	private Set<String> superInterfaces = new HashSet<String>();

	public FlowManager header(String header) {
		this.header = header;
		return this;
	}

	public FlowManager packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public FlowManager name(String name) {
		this.name = name;
		return this;
	}

	public FlowManager host(String host) {
		this.targetType = host;
		return this;
	}

	public FlowManager superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}

	public void toFile(File dir) throws IOException {
		for (CompilationUnitGen u : generate())
			u.toDir(dir);
	}

	public Set<CompilationUnitGen> generate() {
		
		target( new Target().name(targetType).packageName(packageName) );
		for(State state: states) {
			state.packageName(packageName);
			if (!state.isExit()) superInterfaces(state.getName());
			for (Transition t : state.transgen) {
				target.transgen(t);
				t.targetName(targetName);
			}
		}
		
		
		Set<CompilationUnitGen> cu = new HashSet<CompilationUnitGen>();
		cu.add(target.getInterface());
		cu.add(getCompilationUnit());
		for (State s : states) {
			if (!s.isExit())
				cu.add(s.getInterface());
		}
		return cu;
	}

	public CompilationUnitGen getCompilationUnit() {
		CompilationUnitGen c = new CompilationUnitGen();
		c.header(header).packageName(packageName).mod("public class")
				.name(name);
		c.interfaces(superInterfaces);

		ConstructorGen cons = new ConstructorGen().container(name);
		cons.mod("public").param(targetType).body(
				"this." + targetName + " = " + cons.arg(0) + ";");
		c.constructors(cons);

		c.fields(new FieldGen().type(targetType).name(targetName));

		Set<Transition> tr = new HashSet<Transition>();
		for(State state: states)
			for (Transition t : state.transgen) 
				tr.add(t);
		
		for (Transition t : tr) 
			c.methods(t.getMethodImplementation());
		return c;
	}

	public String getName() {
		return name;
	}

}
