

package net.ericaro.diezel.core;

import java.util.HashSet;
import java.util.Set;

import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.ParseException;
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
public class DiezelHost  implements net.ericaro.diezel.core.dsl.DiezelHost{
	public static final String HEADER= " _________________________________\n" + 
			" ))                              (( \n" + 
			"((   __    o     ___        _     ))\n" + 
			" ))  ))\\   _   __  ))   __  ))   (( \n" + 
			"((  ((_/  ((  ((- ((__ ((- ((     ))\n" + 
			" ))        )) ((__     ((__ ))__  (( \n" + 
			"((                                ))\n" + 
			" ))______________________________(( \n" + 
			"Diezel 1.0.0 Generated.\n";
	
	String hostName = "Target"; 
	String parserName = "FlowManager";
	String header = "/*\n"+HEADER+"\n*/"; //add an ascii art old schoolie
	String packageName = "g2";
	
	//those two first are leagacy, to be removed by interface
	public Graph<State,Transition> parse(String s) throws ParseException{
		synchronized (Graph.class) {
			return Graph.parse(s) ;
		}
	}
	
	public FlowManager flowOf(Graph<State, Transition> g){
		return  new FlowManager(g).name(parserName).host(hostName).header(
				header).packageName(packageName);
	}

	// FILL HOST METHOD HERE
	
	
	public void addException() {
		// TODO Auto-generated method stub
		
	}


	public void addParameter() {
		// TODO Auto-generated method stub
		
	}


	public void asExit() {
		// TODO Auto-generated method stub
		
	}


	public void atEndOfTransition() {
		// TODO Auto-generated method stub
		
	}


	public void atStartOfTransition() {
		// TODO Auto-generated method stub
		
	}


	public void configureState() {
		// TODO Auto-generated method stub
		
	}


	public void configureTransition() {
		// TODO Auto-generated method stub
		
	}


	public void doneWithTransitions() {
		// TODO Auto-generated method stub
		
	}


	public void generate() {
		// TODO Auto-generated method stub
		
	}


	public void hostReturnType() {
		// TODO Auto-generated method stub
		
	}


	public void named() {
		// TODO Auto-generated method stub
		
	}


	public void start() {
		// TODO Auto-generated method stub
		
	}


	public void target() {
		// TODO Auto-generated method stub
		
	}


	public void to() {
		// TODO Auto-generated method stub
		
	}


	public void withDoc() {
		// TODO Auto-generated method stub
		
	}


	public void withPackage() {
		// TODO Auto-generated method stub
		
	}
	

	
	
	
	
}
