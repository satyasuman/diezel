package net.ericaro.diezel.core.graph;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import net.ericaro.diezel.core.DiezelHost;
import net.ericaro.diezel.core.FlowManager;
import net.ericaro.diezel.core.State;
import net.ericaro.diezel.core.Transition;
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
		String diezel = " start, withPackage, target?,"
			+ "("
			+ "configureTransition, (addParameter | addException)*, hostReturnType?, withDoc?"
			+ ")*" 
			+ ",doneWithTransitions, ("
			+ "configureState, atEndOfTransition|atStartOfTransition, named, asExit?" 
			+ ")*" + ", generate, to";
/*
 start, package, target?,(configureTransition, (addParameter | addException)*, hostReturnType?, withDoc?)*, (configureState, atEndOfTransition|atStartOfTransition, named, asExit?   )* generate, to

 */
		Graph<State, Transition> g;
		try {
							g = d.parse(diezel);
		} catch (ParseException e) {
			System.err.println(diezel);
			System.err.println(e.getMessage());
			return ;
		}
		FlowManager f = d.flowOf(g);
		g.graph("target/diezel");

		f.packageName("net.ericaro.diezel.core.dsl") // <=> package
				.host("DiezelHost") // <=> target

		;

		// <=> generate

		Set<CompilationUnitGen> s = f.generate();

		// <=> to
		f.toFile(new File("./src/main/java")); // later I'll generate in the
												// target, ass javacc does, but
												// I'm not a maven plugin yet

	}

}
