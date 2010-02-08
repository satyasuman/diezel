

package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.ericaro.diezel.core.dsl.DiezelHostAdapter;
import net.ericaro.diezel.core.flow.DiezelBuilder;
import net.ericaro.diezel.core.flow.FlowGenerator;
import net.ericaro.diezel.core.flow.StateGenerator;
import net.ericaro.diezel.core.flow.TransitionGenerator;
import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.ParseException;
import net.ericaro.diezel.core.graph.RegExp;
import net.ericaro.diezel.core.graph.Graph.S;
import net.ericaro.diezel.core.graph.Graph.T;


/** The main class to handle Diezel code. 
 * Diezel is a Embedded DSL generator. This is the main class to define and generate the DSL.
 * 
 * It is not possible to handle recursive DSL in an EDSL :
 * this DSL <code>a+b+(b+(c+d)))</code> is described in BNF as 
 * 
 * <pre>
 * exp= single , "+" , single ;
 * single = identifier | "(" , exp , ")" ;
 * indentifier = [a-z] ; 
 * </pre>
 * 
 * "exp" depends on single that in turn depends on exp. There is a loop. Any good parser generator will count the loops
 * and knows when to stop ( matching brace).
 * 
 * in an EDSL this is not possible "as is". b
 * 
 * @author eric
 *
 */
public class DiezelHost  extends DiezelHostAdapter{
	public static final String HEADER= " _________________________________\n" + 
			" ))                              (( \n" + 
			"((   __    o     ___        _     ))\n" + 
			" ))  ))\\   _   __  ))   __  ))   (( \n" + 
			"((  ((_/  ((  ((- ((__ ((- ((     ))\n" + 
			" ))        )) ((__     ((__ ))__  (( \n" + 
			"((                                ))\n" + 
			" ))______________________________(( \n" + 
			"Diezel 1.0.0 Generated.\n";
	
	String hostName = "DSLHost"; 
	String parserName = "FlowManager";
	String header = "/*\n"+HEADER+"\n*/"; //add an ascii art old schoolie
	String packageName = "diezel";

	// following are "transient language stuff", made public for until the dsl is fully functional
	//public Graph<StateGenerator, TransitionGenerator> graph;
	public FlowGenerator flow;
	private TransitionGenerator currentTransition;

	private String dsl;

	private StateGenerator currentState;

	
	
	
	public void chain(String dsl)  {
		try {
			synchronized (Graph.class) {
					this.dsl = dsl;
					DiezelBuilder db = new DiezelBuilder() ;
					RegExp.parse(dsl, db) ;
					flow = db.getFlow();
					flow.name(parserName);
					flow.host(hostName);
					flow.header(header);
					flow.packageName(packageName); //init flow with default value
					flow.setDsl(dsl); //init flow with default value
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inPackage(String packageName) {
		this.packageName = packageName ;
		flow.packageName(packageName);
	}
	
	public void withHostName(String target) {
		this.hostName=  target;
		flow.host(hostName);
	}
	
	public void confTransition(String name){
		currentTransition=null;
		currentTransition = flow.getTransition(name);
		if (currentTransition==null)
			throw new RuntimeException("Transition "+name+" is not a valid Transition name in the flow");
	}
	
	public void withParameterType(String... type){
		currentTransition.varTypes(type);
	}
	public void withException(String... type){
		currentTransition.exceptions(type);
	}
	
	public void generateToDir(File f){
		try {
			flow.toFile(f);
		} catch (IOException e) { //to be promote when the dsl will permit it
			e.printStackTrace();
		}
	}

	@Override
	public void withHostMethodName(String hostMethodName) {
		currentTransition.targetName(hostMethodName);
	}

	@Override
	public void withHostReturnType(String returnType) {
		// TODO Auto-generated method stub
		currentTransition.returnType(returnType);
	}

	@Override
	public void withJavadoc(String javadoc) {
		// TODO Auto-generated method stub
		currentTransition.doc(javadoc);
	}

	/* later I'll see how to conf states
	@Override
	public void atEndOfTransition(String transitionName) {
		T<StateGenerator, TransitionGenerator> t = getT(transitionName);
		this.currentState = t.out.state ;
	}


	@Override
	public void atStartOfTransition(String transitionName) {
		T<StateGenerator, TransitionGenerator> t = getT(transitionName);
		this.currentState = t.in.state ;
	}/**/

	@Override
	public void asExitState(boolean exit) {
		currentState.exiting(exit);
	}

	@Override
	public void asStartingState(boolean starting) {
		currentState.starting = starting;
	}

	@Override
	public void withName(String name) {
		currentState.name(name);
	}

	
	
	
	
	
}
