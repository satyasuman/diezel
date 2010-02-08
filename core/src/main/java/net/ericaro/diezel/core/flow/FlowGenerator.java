package net.ericaro.diezel.core.flow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.core.DiezelHost;
import net.ericaro.diezel.core.gen.CompilationUnitGen;
import net.ericaro.diezel.core.gen.ConstructorGen;
import net.ericaro.diezel.core.gen.FieldGen;
import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.Graph.S;
import net.ericaro.diezel.core.graph.Graph.T;

public class FlowGenerator {

	private HostGenerator target; // host is the type that will receive all calls
	private List<StateGenerator> states = new LinkedList<StateGenerator>(); // all states in that flow
	private Map<String, TransitionGenerator> transitions;

	//private Set<T<StateGenerator, TransitionGenerator>> transitions; 
	// all "graph level transition in that flow
	//I'm not very proud with that. this type should disappear, because the flow package should not rely on it (idem for the constructor)
	// that's the "builder's job

	
	public FlowGenerator(Set<StateGenerator> states, Collection<TransitionGenerator> transitions){
		
		
		this.states = new ArrayList<StateGenerator>(states);
		this.transitions = new HashMap<String, TransitionGenerator>();
		for(TransitionGenerator t: transitions) this.transitions.put(t.getName(), t);
		
	} 
	
	public TransitionGenerator getTransition(String name){return transitions.get(name); }
	

	
	public FlowGenerator target(HostGenerator target) {
		this.target = target;
		return this;
	}


	public FlowGenerator states(StateGenerator... states) {
		this.states.addAll(Arrays.asList(states));
		return this;
	}

	
	

	private String header = "/*\n"+DiezelHost.HEADER+"\n*/";
	private String packageName;
	private String name;
	private String targetType;
	private String targetName = "host";
	private Set<String> superInterfaces = new HashSet<String>();
	private String dsl;

	public FlowGenerator header(String header) {
		this.header = header;
		return this;
	}

	public FlowGenerator packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public FlowGenerator name(String name) {
		this.name = name;
		return this;
	}

	public FlowGenerator host(String host) {
		this.targetType = host;
		return this;
	}

	public FlowGenerator superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}

	public void toFile(File dir) throws IOException {
		for (CompilationUnitGen u : generate())
			u.toDir(dir);
	}

	public Set<CompilationUnitGen> generate() {
		
		target( new HostGenerator().name(targetType).packageName(packageName) ); 
		for(StateGenerator state: states) {
			state.packageName(packageName);
			if (!state.isExit()) superInterfaces(state.getName()); // this state will be generated -> collection it in the catch all interface
			for (TransitionGenerator t : state.transgen) {
				target.transgen(t);
				t.targetName(targetName);
			}
		}
		
		
		Set<CompilationUnitGen> cu = new HashSet<CompilationUnitGen>();
		cu.add(target.getInterface()); // add the interface that the target must implement
		cu.add(target.getAdapter()); // generates an adapter too (empty class) so that it is easier to read some code
		cu.add(getCompilationUnit()); // the mains flow generator (that contains all the transitions
		for (StateGenerator s : states) {
			if (!s.isExit())
				cu.add(s.getInterface()); //as promised only non exiting state are generated (because the return type is user defined )
			// so will be the flowcall
			//there are three kind of call: internal-flow, external-flow, exiting-flow. 
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

		Set<TransitionGenerator> tr = new HashSet<TransitionGenerator>(); 
		// the set prevent identical transition to be added to the same State, and therefore, create a compil error
		for(StateGenerator state: states)
			for (TransitionGenerator t : state.transgen) 
				tr.add(t);
		
		for (TransitionGenerator t : tr) 
			c.methods(t.getMethodImplementation()); //
		//the implementation is the code that calls the host method, and return the flowmanager instance with 
		// casted to the next State.
		return c;
	}

	public String getName() {
		return name;
	}


	public void setDsl(String dsl) {
		this.dsl = dsl;
	}
	

}
