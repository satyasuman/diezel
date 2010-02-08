package net.ericaro.diezel.core.graph;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import net.ericaro.diezel.core.DiezelHost;
import net.ericaro.diezel.core.flow.FlowGenerator;
import net.ericaro.diezel.core.flow.StateGenerator;
import net.ericaro.diezel.core.flow.TransitionGenerator;
import net.ericaro.diezel.core.gen.CompilationUnitGen;

/**
 * This is not a test. It's real ! It's the general (hand written for now) for
 * Diezel itself
 * 
 * @author eric
 * 
 */
public class Diezel4Diezel {

	public static void main(String[] args) throws IOException {
		DiezelHost d = new DiezelHost();

		// <=> tr start
		String diezel = " chain, inPackage, withTarget?,"
			+ "withTransitions, ("
			+ "confTransition, (withParameterType | withException)*, withHostReturnType?, withJavadoc?"
			+ ")*|skipTransitions" 
			+ ",  ("
			+ "confState, atEndOfTransition|atStartOfTransition, andConfigureIt, withName, asExitState?" 
			+ ")*|skipStates" + ", generateToDir";
		
		/**/
		d.chain(diezel);
		d.inPackage("net.ericaro.diezel.core.dsl");
		d.withHostName("DiezelHost"); // <=> target
		
		d.confTransition("chain");d.withParameterType("String");
		d.confTransition("inPackage");d.withParameterType("String");
		d.confTransition("withTarget");d.withParameterType("String");
		d.confTransition("named");d.withParameterType("String");
		d.confTransition("withParameterType");d.withParameterType("String...");
		d.confTransition("toDir");d.withParameterType("java.io.File");
		
		
		d.graph.graph("target/diezel");
 
		// <=> generate
		d.toDir(new File("./src/main/java") );
		// the line above is the minimal subset (I guess) to generate a dsl that will be able to generate diezel one 
		/**/
		
		String diezel = " chain, inPackage, withTarget?,"
			+ "withTransitions, ("
			+ "confTransition, (withParameterType | withException)*, withHostReturnType?, withJavadoc?"
			+ ")*|skipTransitions" 
			+ ",  ("
			+ "confState, atEndOfTransition|atStartOfTransition, andConfigureIt, withName, asExitState?" 
			+ ")*|skipStates" + ", generateToDir";
		
		new net.ericaro.diezel.core.dsl.FlowManager(d).chain(diezel).inPackage("net.ericaro.diezel.core.dsl2").withHostName("DiezelHost")
		.selectTransition().confTransition("chain").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("inPackage").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("withTarget").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("confTransition").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("withParameterType").andDefineIt()
			.withParameterType("String...")
			
		.selectTransition().confTransition("withException").andDefineIt()
			.withParameterType("String...")
			
		.selectTransition().confTransition("withHostReturnType").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("withHostMethodName").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("withJavadoc").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("atEndOfTransition").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("atStartOfTransition").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("withName").andDefineIt()
			.withParameterType("String")
			
		.selectTransition().confTransition("asExitState").andDefineIt()
			.withParameterType("boolean")
		
			
		.selectTransition().confTransition("generateToDir").andDefineIt()
			.withParameterType("java.io.File")
		
		.andNow()
		.nowGenerate().toDir(new File("./src/main/java") );
		
		
		
		

	}

}
