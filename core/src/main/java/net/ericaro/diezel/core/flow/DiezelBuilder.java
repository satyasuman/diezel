package net.ericaro.diezel.core.flow;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.Graph.S;
import net.ericaro.diezel.core.graph.Graph.T;

/** Diezel parser delegates almost any call to this class. It is in charge to build the graph, and then return a FlowGenerator fully configured.
 * 
 * @author eric
 *
 */
public class DiezelBuilder implements DiezelSax<Graph<StateGenerator, TransitionGenerator>> {

	public Graph<StateGenerator, TransitionGenerator> bang(
			Deque<Graph<StateGenerator, TransitionGenerator>> v) {
		return Graph.bang((Graph<StateGenerator, TransitionGenerator>[]) v.toArray(new Graph[]{}));
	}

	public void end() {
		
	}

	public FlowGenerator getFlow(){return flow;}
	
	FlowGenerator flow;
	public void flow(Graph<StateGenerator, TransitionGenerator> g) {
		g.reduce();
		finalize(g); //prepare the graph, tune transitions and states
		
		flow = new FlowGenerator(g.getStates(), g.getTransitions());
		//flow.host(hostName);
		//flow.header(header);
		//flow.packageName(packageName); //init flow with default value
		//flow.setDsl(dsl);
		
	}

	//ALGS
	private void finalize(Graph<StateGenerator,TransitionGenerator> g){
		// name states
		int i = 0;
		for (S<StateGenerator,TransitionGenerator> s : g.states) {
			if (s.state == null)
				s.state = new StateGenerator().name("DiezelState" + ++i);

		}
		for (T<StateGenerator,TransitionGenerator> t : g.transitions) {
			if (t.transition== null)
				t.transition= new TransitionGenerator().name( t.name).varTypes(t.types).capture(t.capture);
			
		}
		// gen all interfaces
		for (S<StateGenerator,TransitionGenerator> s : g.states) {
			for (T<StateGenerator,TransitionGenerator> t : s.outs) {
				//TODO recursively follow symbolic t, and append all transitions.
				appendAll(s, t, new HashSet<T<StateGenerator,TransitionGenerator>>());
			}

		}
}


private void appendAll(S<StateGenerator, TransitionGenerator> s, T<StateGenerator, TransitionGenerator> t, Set<T<StateGenerator, TransitionGenerator>> followed) {
	if (!t.implicit){ //implicit are nameless transition, created by the graph operators. It is not possible to remove them all!
	s.state.transgen(t.transition);
	t.transition.returnState(t.out.state);
	}
	else if (!followed.contains(t)){
		followed.add(t); //mark as visited
		for(T<StateGenerator, TransitionGenerator> next: t.out.outs )
			appendAll(s, next, followed);
	}
}
	
	
	
	
	public Graph<StateGenerator, TransitionGenerator> opt(
			Graph<StateGenerator, TransitionGenerator> v) {
		return Graph.opt(v);
	}

	public Graph<StateGenerator, TransitionGenerator> plus(
			Graph<StateGenerator, TransitionGenerator> v) {
		return Graph.iter_once(v);
	}

	public Graph<StateGenerator, TransitionGenerator> sel(
			Graph<StateGenerator, TransitionGenerator> v1,
			Graph<StateGenerator, TransitionGenerator> v2) {
		return Graph.sel(v1,v2);
	}

	public Graph<StateGenerator, TransitionGenerator> seq(
			Graph<StateGenerator, TransitionGenerator> v1,
			Graph<StateGenerator, TransitionGenerator> v2) {
		return Graph.seq(v1,v2);
	}

	public Graph<StateGenerator, TransitionGenerator> star(
			Graph<StateGenerator, TransitionGenerator> v) {
		return Graph.iter(v);
	}

	public Graph<StateGenerator, TransitionGenerator> terminal(
			String annotations, String image, String[] types) {
		return Graph.term(annotations, image, types);
	}

	

	
	
	
}
