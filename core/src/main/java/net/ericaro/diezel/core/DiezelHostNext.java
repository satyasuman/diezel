

package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.ericaro.diezel.core.dsl.DiezelHostAdapter;
import net.ericaro.diezel.core.flow.FlowGenerator;
import net.ericaro.diezel.core.flow.StateGenerator;
import net.ericaro.diezel.core.flow.TransitionGenerator;
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
public class DiezelHostNext  extends DiezelHostAdapter{
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

	// following are "transient language stuff", made public for until the dsl is fully functional
	public Graph<StateGenerator, TransitionGenerator> graph;
	public FlowGenerator flow;
	public  TransitionGenerator currentTransition;
	
	
	
	public void chain(String s) {
		synchronized (Graph.class) {
			try {
				this.graph = Graph.parse(s) ;
				flow = new FlowGenerator(this.graph).name(parserName).host(hostName).header(
						header).packageName(packageName); //init flow with default value
			} catch (ParseException e) { //to be removed and throw (but yet the dsl4dsl does not permit it
				e.printStackTrace();
			}
		}
	}
	
	public void inPackage(String packageName) {
		this.packageName = packageName ;
		flow.packageName(packageName);
	}
	
	public void withTarget(String target) {
		this.hostName=  target;
		flow.host(hostName);
	}
	
	public void named(String name){
		for( T<StateGenerator, TransitionGenerator> t: graph.transitions ){
			if (name.equals(t.name)) this.currentTransition = t.transition;
		}
	}
	
	public void withParameterType(String... type){
		currentTransition.varTypes(type);
	}
	
	public void toDir(File f){
		try {
			flow.toFile(f);
		} catch (IOException e) { //to be promote when the dsl will permit it
			e.printStackTrace();
		}
	}
	
	
}
