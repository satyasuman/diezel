package net.ericaro.diezel.core;

import net.ericaro.diezel.core.dsl.FlowManager;
import net.ericaro.diezel.core.dsl.S1;
import net.ericaro.diezel.core.dsl.S17;
import net.ericaro.diezel.core.dsl.S5;
import net.ericaro.diezel.core.dsl.S7;


/** Host for Diezel EDSL
 * 
 * @author eric
 *
 */
public class Diezel implements Runnable {

	public static final String DIEZEL_DSL = "		\n" + 
			"		chain(String), inPackage(String), withHostName(String)?, withTransitions, \n" + 
			"			(\n" + 
			"			   confTransition(String), (withParameterType(String...) | withException(String...))*\n" + 
			"			   , withHostReturnType(String)?, withHostMethodName(String), withJavadoc(String)?\n" + 
			"			)*\n" + 
			"		|\n" + 
			"			skipTransitions,\n" + 
			"		\n" + 
			"		(\n" + 
			"			confState, atEndOfTransition(String)|atStartOfTransition(String), withName(String)?, asExitState(boolean)?, asStartingState(boolean)? )*\n" + 
			"		|\n" + 
			"			skipStates, generateToDir(java.io.File)\n" + 
			"";
	
	
	DiezelHost host = new DiezelHost(); // the engine that implements all functions
	
	
	
	
	


	public void run() {
		
	}
	

}
