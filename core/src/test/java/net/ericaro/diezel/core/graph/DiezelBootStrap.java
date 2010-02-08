package net.ericaro.diezel.core.graph;

import java.io.File;
import java.io.IOException;

import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelHost;

public class DiezelBootStrap {
	
	public static void main(String[] args) throws IOException {

		DiezelHost d = new DiezelHost() ;
			
		/*
		
		chain(String), inPackage(String), withHostName(String)?, withTransitions, 
			(
			   confTransition(String), (withParameterType(String...) | withException(String...))*
			   , withHostReturnType(String)?, withHostMethodName(String), withJavadoc(String)?
			)*
		|
			skipTransitions,
		
		(
			confState, atEndOfTransition(String)|atStartOfTransition(String), withName(String)?, asExitState(boolean)?, asStartingState(boolean)? )*
		|
			skipStates, generateToDir(java.io.File)
		
		*/
		// USES RAWly the host to "generate" the dsl for dsl.
		System.out.println(Diezel.DIEZEL_DSL);
		d.chain(Diezel.DIEZEL_DSL);
		d.inPackage("net.ericaro.diezel.core.dsl");
		d.withHostName("DiezelHost"); // <=> target
		
		d.confTransition("chain");
			d.withParameterType("String");
			d.withException("net.ericaro.diezel.core.graph.ParseException");
		/*	
		d.confTransition("inPackage");d.withParameterType("String");
		d.confTransition("withHostName");d.withParameterType("String");
		d.confTransition("withTransitions");
		d.confTransition("confTransition");d.withParameterType("String");
		d.confTransition("withParameterType");d.withParameterType("String...");
		d.confTransition("withException");d.withParameterType("String...");
		d.confTransition("withHostReturnType");d.withParameterType("String");
		d.confTransition("withHostMethodName");d.withParameterType("String");
		d.confTransition("withJavadoc");d.withParameterType("String");
		d.confTransition("skipTransitions");
		d.confTransition("confState");
		d.confTransition("atEndOfTransition");d.withParameterType("String");
		d.confTransition("atStartOfTransition");d.withParameterType("String");
		d.confTransition("withName");d.withParameterType("String");
		d.confTransition("asExitState");d.withParameterType("boolean");
		d.confTransition("asStartingState");d.withParameterType("boolean");
		d.confTransition("skipStates");
		
			d.confTransition("generateToDir");
			d.withParameterType("java.io.File");
		*/
		d.confTransition("generateToDir");
		d.withException("java.io.IOException");
		
		
		//d.graph.graph("target/diezelBootstrap");
		d.generateToDir(new File("./src/main/java") );
 
}
}